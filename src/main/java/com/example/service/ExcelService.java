package com.example.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ExcelService {

    public void readExcelFile() {
        try {
            // Load the file from resources directory
            ClassPathResource resource = new ClassPathResource("Sample.xlsx");
            InputStream inputStream = resource.getInputStream();

            // Create Workbook instance from input stream
            Workbook workbook = new XSSFWorkbook(inputStream);

            // Get first worksheet
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate through each row
            for (Row row : sheet) {
                // Iterate through each cell in row
                for (Cell cell : row) {
                    // Print cell value based on cell type
                    switch (cell.getCellType()) {
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t");
                            break;
                        case BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t");
                            break;
                        default:
                            System.out.print("" + "\t");
                    }
                }
                System.out.println(); // New line after each row
            }

            // Close workbook and stream
            workbook.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readCellByAddress(int rowIndex, int colIndex) {
        System.out.println("Reading cell at row " + rowIndex + ", column " + colIndex);
        try {
            // Load the file from resources directory
            ClassPathResource resource = new ClassPathResource("HTOU.xlsx");
            InputStream inputStream = resource.getInputStream();

            // Create Workbook instance from input stream
            Workbook workbook = new XSSFWorkbook(inputStream);

            // Get first worksheet
            Sheet sheet = workbook.getSheetAt(0);

            // Get the specific row (null check in case row doesn't exist)
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                workbook.close();
                inputStream.close();
                System.out.println("Row " + rowIndex + " does not exist");
                return "Row not found";
            }

            // Get the specific cell (null check in case cell doesn't exist)
            Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell == null) {
                workbook.close();
                inputStream.close();
                System.out.println("Cell at row " + rowIndex + ", column " + colIndex + " does not exist");
                return "Cell not found";
            }

            // Get cell value based on cell type
            String result;
            switch (cell.getCellType()) {
                case STRING:
                    result = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        result = cell.getDateCellValue().toString();
                    } else {
                        result = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    result = String.valueOf(cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    result = "Formula: " + cell.getCellFormula();
                    break;
                default:
                    result = "Empty or unsupported cell type";
            }

            // Print to console
            System.out.println("Value at row " + rowIndex + ", column " + colIndex + ": " + result);

            // Close resources
            workbook.close();
            inputStream.close();

            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

}