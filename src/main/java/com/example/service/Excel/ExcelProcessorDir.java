package com.example.service.Excel;

import com.example.model.BaseTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

public class ExcelProcessorDir {

    //fetch file path from properties file
    private static String excelDirPath;
    static {
        try (InputStream input = ExcelProcessor.class.getClassLoader().getResourceAsStream("endpoint.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                excelDirPath = prop.getProperty("excel.dir");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T extends BaseTableModel> List<T> readExcelTableToModel(String directoryName, int startRowIndex, String tableType) {
        Class<T> modelClass = (Class<T>) ModelFactory.getModelClass(tableType);

        if (modelClass == null) {
            throw new IllegalArgumentException("Invalid table type: " + tableType);
        }

        List<T> dataList = new ArrayList<>();
        System.out.println("Path :"+excelDirPath+"\\"+directoryName);
        List<File> excelFiles = getExcelFiles(excelDirPath+"\\"+directoryName);

        for (File file : excelFiles) {
            try (Workbook workbook = getWorkbook(file)) {
                if (workbook == null) continue;

                Sheet sheet = workbook.getSheetAt(0);
                Map<Integer, String> columnMappings = ColumnMapper.getColumnMappings(tableType);

                for (int rowIndex = startRowIndex; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row == null) continue;

                    T modelInstance = modelClass.getDeclaredConstructor().newInstance();

                    for (Map.Entry<Integer, String> entry : columnMappings.entrySet()) {
                        int columnIndex = entry.getKey();
                        String fieldName = entry.getValue();

                        Cell cell = row.getCell(columnIndex);
                        if (cell == null) continue;

                        try {
                            setFieldValue(modelInstance, fieldName, cell);
                        } catch (Exception e) {
                            e.printStackTrace();  // Log errors properly
                        }
                    }

                    dataList.add(modelInstance);
                }

            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace(); // Handle file errors
            }
        }

        dataList.forEach(model -> {
            StringBuilder result = new StringBuilder();
            result.append(model.getClass().getSimpleName()).append(" {");

            Field[] fields = model.getClass().getDeclaredFields();
            try {
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    field.setAccessible(true);
                    result.append(field.getName())
                            .append("=")
                            .append(field.get(model));
                    if (i < fields.length - 1) {
                        result.append(", ");
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            result.append("}");
            System.out.println(result);
        });


        return dataList;
    }

    // Method to retrieve all .xlsx files from a directory
    private static List<File> getExcelFiles(String directoryPath) {
        File folder = new File(directoryPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xlsx"));

        return files != null ? Arrays.asList(files) : Collections.emptyList();
    }
    private static Workbook getWorkbook(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //cell value reading
    private static <T> void setFieldValue(T model, String fieldName, Cell cell) throws ReflectiveOperationException {
        Field field = model.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);

        switch (cell.getCellType()) {
            case STRING -> {
                if (field.getType().isEnum()) {
                    @SuppressWarnings("unchecked")
                    Class<Enum> enumType = (Class<Enum>) field.getType();
                    field.set(model, Enum.valueOf(enumType, cell.getStringCellValue()));
                } else {
                    field.set(model, cell.getStringCellValue());
                }
            }
            case NUMERIC -> {
                if (field.getType() == BigDecimal.class) {
                    field.set(model, BigDecimal.valueOf(cell.getNumericCellValue()));
                } else if (field.getType() == Double.class || field.getType() == double.class) {
                    field.set(model, cell.getNumericCellValue());
                } else if (field.getType() == Integer.class || field.getType() == int.class) {
                    field.set(model, (int) cell.getNumericCellValue());
                } else if (field.getType() == java.util.Date.class) {
                    field.set(model, cell.getDateCellValue());
                }
            }
            case BOOLEAN -> field.set(model, cell.getBooleanCellValue());
            default -> {
            }
        }
    }
}
