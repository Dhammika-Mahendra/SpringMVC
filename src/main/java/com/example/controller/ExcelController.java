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
}