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

    public String findValueByColumnMatch(String inputColumnName,int HeadInd, String inputValue, String outputColumnName) {
        try {
            // Load the file from resources directory
            ClassPathResource resource = new ClassPathResource("BH.xlsx");
            InputStream inputStream = resource.getInputStream();

            // Create Workbook instance from input stream
            Workbook workbook = new XSSFWorkbook(inputStream);

            // Get first worksheet
            Sheet sheet = workbook.getSheetAt(0);

            // Get header row (assuming it's the first row - index 0)
            Row headerRow = sheet.getRow(HeadInd);
            if (headerRow == null) {
                System.out.println("Header row not found");
                workbook.close();
                inputStream.close();
                return null;
            }

            // Find column indices for inputColumnName and outputColumnName
            int inputColumnIndex = -1;
            int outputColumnIndex = -1;

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    String headerValue = cell.getStringCellValue();
                    if (headerValue.equalsIgnoreCase(inputColumnName)) {
                        inputColumnIndex = i;
                    }
                    if (headerValue.equalsIgnoreCase(outputColumnName)) {
                        outputColumnIndex = i;
                    }
                }
            }

            // Check if both columns were found
            if (inputColumnIndex == -1) {
                System.out.println("Input column '" + inputColumnName + "' not found");
                workbook.close();
                inputStream.close();
                return null;
            }

            if (outputColumnIndex == -1) {
                System.out.println("Output column '" + outputColumnName + "' not found");
                workbook.close();
                inputStream.close();
                return null;
            }

            // Search through data rows (starting from row 1, after header)
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row dataRow = sheet.getRow(rowIndex);
                if (dataRow != null) {
                    Cell inputCell = dataRow.getCell(inputColumnIndex);
                    if (inputCell != null) {
                        // Convert cell value to string for comparison
                        String cellValue = getCellValueAsString(inputCell);

                        // Check if this is the row we're looking for
                        if (cellValue.equals(inputValue)) {
                            // Found matching row, get output value
                            Cell outputCell = dataRow.getCell(outputColumnIndex);
                            if (outputCell != null) {
                                String result = getCellValueAsString(outputCell);
                                System.out.println("Found " + outputColumnName + ": " + result +
                                        " for " + inputColumnName + ": " + inputValue);
                                workbook.close();
                                inputStream.close();
                                return result;
                            } else {
                                System.out.println("Output cell is empty for " + inputColumnName +
                                        ": " + inputValue);
                                workbook.close();
                                inputStream.close();
                                return null;
                            }
                        }
                    }
                }
            }

            // If we get here, no matching row was found
            System.out.println("No match found for " + inputColumnName + ": " + inputValue);
            workbook.close();
            inputStream.close();
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper method to convert any cell type to String
     */
    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Convert to string but remove .0 for whole numbers
                    double value = cell.getNumericCellValue();
                    if (value == Math.floor(value)) {
                        return String.valueOf((int)value);
                    }
                    return String.valueOf(value);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getStringCellValue());
                } catch (Exception e) {
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