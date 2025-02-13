<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Result Page</title>
</head>
<body>
<h1>Submitted Data</h1>
<p>Username: ${username}</p>
<p>Email: ${email}</p>
<a href="<c:url value='/' />">Go Back</a>
</body>
</html>
