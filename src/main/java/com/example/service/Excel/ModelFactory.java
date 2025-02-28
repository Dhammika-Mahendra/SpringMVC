package com.example.service.Excel;

import com.example.model.BaseTableModel;
import com.example.model.Payment;
import com.example.model.Sample;

import java.util.HashMap;
import java.util.Map;

public class ModelFactory {
    private static final Map<String, Class<? extends BaseTableModel>> tableTypeMap = new HashMap<>();

    // Register table types with corresponding model classes
    static {
        tableTypeMap.put("Payment", Payment.class);
        tableTypeMap.put("Sample", Sample.class);
    }

    // Method to return the corresponding model class for a given table type
    public static Class<? extends BaseTableModel> getModelClass(String tableType) {
        return tableTypeMap.get(tableType);
    }
}
