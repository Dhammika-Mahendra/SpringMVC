<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Simple Form</title>
    <link href="<c:url value="/resources/css/bootstrap/bootstrap.min.css" />" rel="stylesheet">
</head>
<body>
<h1>Enter Details</h1>
<form action="${submitEndpoint}" method="POST">
    <h3>${message}</h3>
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" required class="form-control"><br><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required class="form-control"><br><br>

    <button type="submit">Submit</button>
</form>
</body>
</html>
