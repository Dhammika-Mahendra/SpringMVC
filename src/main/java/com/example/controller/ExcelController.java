package com.example.controller;

import com.example.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @GetMapping("/ex")
    @ResponseBody
    public String readExcel() {
        excelService.readExcelFile();
        return "hello";
    }

    @GetMapping("/exind")
    @ResponseBody
    public String readSpecificCell(
            @RequestParam(name = "row", defaultValue = "0") int row,
            @RequestParam(name = "col", defaultValue = "0") int col) {

        String cellValue = excelService.readCellByAddress(row, col);
        System.out.println("Cell value: " + cellValue);
        return "hello";
    }

    // Generic version that can be used for any column lookup
    @GetMapping("/exin")
    @ResponseBody
    public String lookupValue(
            @RequestParam (name="inputColumn") String inputColumn,
            @RequestParam (name="inputValue") String inputValue,
            @RequestParam (name="outputColumn") String outputColumn,
            @RequestParam (name="headInd") int headInd) {
        System.out.println(inputColumn+ " " + inputValue + " " + outputColumn + " " + headInd);
        String result = excelService.findValueByColumnMatch(inputColumn, headInd, inputValue, outputColumn);
        if (result != null) {
            System.out.println("Found " + outputColumn + ": " + result + " for " + inputColumn + ": " + inputValue);
            return "hello";
        } else {
            System.out.println("Could not find " + outputColumn + " for " + inputColumn + ": " + inputValue);
            return "hello";
        }
    }
}