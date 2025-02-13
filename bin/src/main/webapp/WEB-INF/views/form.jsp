<!DOCTYPE html>
<html>
<head>
    <title>Simple Form</title>
</head>
<body>
<h1>Enter Details</h1>
<form action="${submitEndpoint}" method="POST">
    <h3>${message}</h3>
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" required><br><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required><br><br>

    <button type="submit">Submit</button>
</form>
</body>
</html>
