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
}