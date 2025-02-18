package com.example.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void testConnection() {
        String sql = "SELECT 1 FROM DUAL";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        if (result != null && result == 1) {
            System.out.println("Connection is established.");
        } else {
            System.out.println("Failed to establish connection.");
        }
    }
}