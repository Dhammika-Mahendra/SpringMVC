<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Employee Data</title>
</head>
<body>
<h1>${message}</h1>
<table>
    <thead>
    <tr>
        <th>Employee ID</th>
        <th>Name</th>
        <th>Date of Birth</th>
        <th>Salary</th>
        <th>Place</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="employee" items="${employeeData}">
        <tr>
            <td>${employee.EMPID}</td>
            <td>${employee.EMPNAME}</td>
            <td>${employee.EMPDOB}</td>
            <td>${employee.EMPSALARY}</td>
            <td>${employee.EMPPLACE}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>