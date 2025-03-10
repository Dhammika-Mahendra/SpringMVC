package com.example.controller;

import com.example.model.BaseTableModel;
import com.example.repository.ExcelRepository;
import com.example.service.Excel.ExcelProcessor;
import com.example.service.Excel.ExcelProcessorDir;
import com.example.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @Autowired
    private ExcelRepository excelRepository;


    @GetMapping("/ex")
    @ResponseBody
    public String readExcel(@RequestParam (name="fileName") String fileName) {
        excelService.readExcelFile(fileName);
        return "hello";
    }

    @GetMapping("/extb")
    @ResponseBody
    public String readExcelTable(@RequestParam (name="fileName") String fileName, @RequestParam (name="headInd") int headInd) {
        excelService.readExcelTableToModel(fileName,headInd);
        return "hello";
    }

    @GetMapping("/exind")
    @ResponseBody
    public String readSpecificCell(
            @RequestParam(name = "row", defaultValue = "0") int row,
            @RequestParam(name = "col", defaultValue = "0") int col,
            @RequestParam (name="fileName") String fileName) {

        String cellValue = excelService.readCellByAddress(fileName, row, col);
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
            @RequestParam (name="headInd") int headInd,
            @RequestParam (name="fileName") String fileName) {
        System.out.println(inputColumn+ " | " + inputValue + " | " + outputColumn + " | " + headInd);
        String result = excelService.findValueByColumnMatch(fileName, inputColumn, headInd, inputValue, outputColumn);
        if (result != null) {
            System.out.println("Found " + outputColumn + ": " + result + " for " + inputColumn + ": " + inputValue);
            return "hello";
        } else {
            System.out.println("Could not find " + outputColumn + " for " + inputColumn + ": " + inputValue);
            return "hello";
        }
    }

    //------------------------------------------------------------------------------------------
    //                  Excel to DB
    //-----------------------------------------------------------------------------------------

    //Read single Excel file and fetch to database
    @GetMapping("/exdb")
    @ResponseBody
    public String readExcelToDB(
            @RequestParam (name="fileName") String fileName,
            @RequestParam (name="startRowIndex") int startRowIndex,
            @RequestParam (name="tableType") String tableType
    ) {
        List<BaseTableModel> records = ExcelProcessor.readExcelTableToModel(fileName, startRowIndex, tableType);
        return "hello";
    }

    //Read multiple Excel file and fetch to database
    @GetMapping("/exdbdir")
    @ResponseBody
    public String readExcelToDBDir(
            @RequestParam (name="dirName") String dirName,
            @RequestParam (name="startRowIndex") int startRowIndex,
            @RequestParam (name="tableType") String tableType
    ) {
        List<BaseTableModel> records = ExcelProcessorDir.readExcelTableToModel(dirName, startRowIndex, tableType);
        excelRepository.fetchData(records);
        return "hello";
    }
}