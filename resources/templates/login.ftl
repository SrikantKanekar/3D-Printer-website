<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login</title>
</head>
<body style="text-align: center; font-family: sans-serif">
<img src="/static/ktor.png">
<h1>Login</h1>
<p><i>enter name and password</i></p>
<hr>
<div>
    <form action="/login" method="post">
        <input type="text" name="Username">
        <br>
        <input type="text" name="Password">
        <br>
        <input type="submit">
    </form>
</div>
</body>
</html>