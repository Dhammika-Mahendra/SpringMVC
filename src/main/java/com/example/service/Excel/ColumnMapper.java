package com.example.service.Excel;

import java.util.HashMap;
import java.util.Map;

public class ColumnMapper {
    private static final Map<String, Map<Integer, String>> columnMappings = new HashMap<>();


    // Define column mappings for each table type (column index | property name of model)
    static {
        columnMappings.put("Payment", Map.of(
                1, "amount",
                2, "paymentDate",
                3, "paymentMethod",
                4, "status"
        ));

        columnMappings.put("Sample", Map.of(
                0, "name",
                1, "age",
                2, "place",
                3, "nic",
                4, "gender"
        ));

    }

    //return the column mappings for the given table type
    public static Map<Integer, String> getColumnMappings(String tableType) {
        return columnMappings.getOrDefault(tableType, new HashMap<>());
    }
}
