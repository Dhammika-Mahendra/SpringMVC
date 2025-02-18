package com.example.controller;

import com.example.util.DBUtil;
import com.example.util.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;
import java.util.Map;

@Controller
public class EmployeeController {

    @Autowired
    private DBUtil DBUtil;

    @Autowired
    private UserDao userDao;

    @GetMapping("/dbcheck")
    public String checkDatabaseConnection() {
        userDao.testConnection();
        return "hello";
    }

    @GetMapping("/employees")
    public String getEmployeeData(Model model) {
        // Call the utility class to get employee data from DB
        Set<Map<String, Object>> employeeData = DBUtil.getEmployeeData();
        model.addAttribute("employeeData", employeeData);
        model.addAttribute("message", "Employee data fetched successfully!");
        return "employee"; // Return to a view (you can display any message or data in the view)
    }

    @GetMapping("/empList")
    public String getEmployees(Model model) {
        Set<Map<String, Object>> employeeData = DBUtil.getEmployeeData();
        model.addAttribute("employees", employeeData);
        return "empLoop/empContainer";
    }
}