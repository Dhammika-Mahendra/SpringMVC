package com.example.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

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
                    System.out.print(getCellValueAsString(cell) + "\t");
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
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                double value = cell.getNumericCellValue();
                return (value == Math.floor(value)) ? String.valueOf((int) value) : String.valueOf(value);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e1) {
                    try {
                        return String.valueOf(cell.getNumericCellValue());
                    } catch (Exception e2) {
                        return "#FORMULA_ERROR#";
                    }
                }
            default:
                return "";
        }
    }
}