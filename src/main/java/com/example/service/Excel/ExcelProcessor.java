package com.example.service.Excel;

import com.example.model.BaseTableModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.lang.reflect.*;
import java.util.Properties;

public class ExcelProcessor {

    //fetch file path from properties file
    private static String excelFilePath;
    static {
        try (InputStream input = ExcelProcessor.class.getClassLoader().getResourceAsStream("endpoint.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                excelFilePath = prop.getProperty("excel.path");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //content of an Excel file returned as a list of model instances
    public static <T extends BaseTableModel> List<T> readExcelTableToModel(String fileName, int startRowIndex, String tableType) {

        Class<T> modelClass = (Class<T>) ModelFactory.getModelClass(tableType);

        if (modelClass == null) {
            throw new IllegalArgumentException("Invalid table type: " + tableType);
        }

        List<T> dataList = new ArrayList<>();

        try (Workbook workbook = getWorkbook(fileName)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Get column mappings for the given table type
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

                    //reading the value of a single cell
                    try {
                        setFieldValue(modelInstance, fieldName, cell);
                    } catch (Exception e) {
                        System.err.println("Error :" + e.getMessage());
                        System.err.println("Error reading value for field: " + fieldName + " at row: " + (rowIndex + 1) + ", column: " + (columnIndex + 1));
                    }
                }

                dataList.add(modelInstance);

            }

            //Printing
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

        } catch (IOException | ReflectiveOperationException e) {
            e.printStackTrace();
        }

        return dataList;
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
    
//workbook opening
    private static Workbook getWorkbook(String fileName) {

        try {
            System.out.println("URL: " + excelFilePath);
            File file = new File(excelFilePath+fileName+".xlsx");
            if (!file.exists()) {
                throw new IOException("File not found: " + fileName);
            }
            try (FileInputStream inputStream = new FileInputStream(file)) {
                return new XSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}