package com.example.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class DBUtil {

    @Value("${database.url}")
    private static String URL;

    @Value("${database.user}")
    private static String USERNAME;

    @Value("${database.password}")
    private static String PASSWORD;

    // Method to establish connection and query data
    public static Set<Map<String, Object>> getEmployeeData() {
        Set<Map<String, Object>> employeeData = new HashSet<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Register JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Connected to database driver...");
            // Open a connection
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to database...");
            // Execute a query
            stmt = conn.createStatement();
            String sql = "SELECT EMPID, EMPNAME, EMPDOB, EMPSALARY, EMPPLACE FROM DHAMMIKA.EMPLOYEES";
            rs = stmt.executeQuery(sql);

            // Extract data from result set
            while (rs.next()) {
                // Retrieve by column name and store in a map
                Map<String, Object> row = new HashMap<>();
                row.put("EMPID", rs.getInt("EMPID"));
                row.put("EMPNAME", rs.getString("EMPNAME"));
                row.put("EMPDOB", rs.getDate("EMPDOB"));
                row.put("EMPSALARY", rs.getInt("EMPSALARY"));
                row.put("EMPPLACE", rs.getString("EMPPLACE"));

                // Add the map to the set
                employeeData.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Clean up environment
            try { if (rs != null) rs.close(); } catch (Exception e) { System.out.println(e.getMessage()); }
            try { if (stmt != null) stmt.close(); } catch (Exception e) { System.out.println(e.getMessage()); }
            try { if (conn != null) conn.close(); } catch (Exception e) { System.out.println(e.getMessage()); }
        }

        return employeeData;
    }
}
