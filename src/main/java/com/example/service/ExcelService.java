package com.example.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

@Service
public class ExcelService {

    /**
     * Reads the entire content of an Excel file and prints it to the console.
     */
    public void readExcelFile(String fileName) {
        try (Workbook workbook = getWorkbook(fileName)) {
            if (workbook == null) return;

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                for (Cell cell : row) {
                    System.out.print(getCellValueAsString(cell) + "| \t");
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a specific cell from an Excel file.
     */
    public String readCellByAddress(String fileName, int rowIndex, int colIndex) {
        System.out.println("Reading cell at row " + rowIndex + ", column " + colIndex);
        try (Workbook workbook = getWorkbook(fileName)) {
            if (workbook == null) return "Error: Unable to open workbook";

            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(rowIndex);
            if (row == null) return "Row not found";

            Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell == null) return "Cell not found";

            String result = getCellValueAsString(cell);
            System.out.println("Value at row " + rowIndex + ", column " + colIndex + ": " + result);
            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Finds a value by matching a column and retrieving another column's value.
     */
    public String findValueByColumnMatch(String fileName, String inputColumnName, int headerRowIndex,
                                         String inputValue, String outputColumnName) {
        try (Workbook workbook = getWorkbook(fileName)) {
            if (workbook == null) return null;

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(headerRowIndex);
            if (headerRow == null) return null;

            int inputColumnIndex = -1;
            int outputColumnIndex = -1;

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    String headerValue = cell.getStringCellValue();
                    if (headerValue.equalsIgnoreCase(inputColumnName)) inputColumnIndex = i;
                    if (headerValue.equalsIgnoreCase(outputColumnName)) outputColumnIndex = i;
                }
            }

            if (inputColumnIndex == -1 || outputColumnIndex == -1) return null;

            for (int rowIndex = headerRowIndex + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row dataRow = sheet.getRow(rowIndex);
                if (dataRow == null) continue;

                Cell inputCell = dataRow.getCell(inputColumnIndex);
                System.out.println(getCellValueAsString(inputCell)+"--->"+inputValue);
                if (inputCell != null && getCellValueAsString(inputCell).equals(inputValue)) {
                    Cell outputCell = dataRow.getCell(outputColumnIndex);
                    return outputCell != null ? getCellValueAsString(outputCell) : null;
                }
            }

            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper method to load a workbook from a file.
     */
    private Workbook getWorkbook(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource(fileName+".xlsx");
            InputStream inputStream = resource.getInputStream();
            return new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts any cell type to a string representation.
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        // Use DataFormatter for consistent String representation
        DataFormatter formatter = new DataFormatter(true);

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // For dates, use SimpleDateFormat to ensure consistent output
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    return sdf.format(cell.getDateCellValue());
                } else {
                    // For numeric values, preserve exact representation using BigDecimal
                    // This prevents scientific notation and maintains precision
                    BigDecimal bd = BigDecimal.valueOf(cell.getNumericCellValue());
                    return bd.toPlainString();
                }

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                // First try to evaluate the formula
                try {
                    FormulaEvaluator evaluator = cell.getSheet().getWorkbook()
                            .getCreationHelper().createFormulaEvaluator();
                    CellValue cellValue = evaluator.evaluate(cell);

                    switch (cellValue.getCellType()) {
                        case STRING:
                            return cellValue.getStringValue();
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                return sdf.format(DateUtil.getJavaDate(cellValue.getNumberValue()));
                            } else {
                                BigDecimal bd = BigDecimal.valueOf(cellValue.getNumberValue());
                                return bd.toPlainString();
                            }
                        case BOOLEAN:
                            return String.valueOf(cellValue.getBooleanValue());
                        default:
                            return formatter.formatCellValue(cell, evaluator);
                    }
                } catch (Exception e) {
                    // Fall back to getting the formula string
                    return cell.getCellFormula();
                }

            case BLANK:
                return "";

            default:
                // For any other types, use the DataFormatter as fallback
                return formatter.formatCellValue(cell);
        }
    }
}