<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Employee List</title>
</head>
<body>
<ul>
    <c:forEach var="employee" items="${employees}">
        <jsp:include page="empItem.jsp">
            <jsp:param name="EMPID" value="${employee.EMPID}" />
            <jsp:param name="EMPNAME" value="${employee.EMPNAME}" />
            <jsp:param name="EMPDOB" value="${employee.EMPDOB}" />
            <jsp:param name="EMPSALARY" value="${employee.EMPSALARY}" />
            <jsp:param name="EMPPLACE" value="${employee.EMPPLACE}" />
        </jsp:include>
    </c:forEach>
</ul>
</body>
</html>