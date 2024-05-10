<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registration Form</title>
    <style>
     body {
            background-image: url('obg.jpg'); /* Replace 'bgimg.jpg' with the path to your image */
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            margin: 0; /* Reset default body margin */
            padding: 0; /* Reset default body padding */
            min-height: 100vh;
    
    }
    .reg{
    border:2px solid black;
    height:600px;
    width:400px;
    margin-top:90px;
    margin-top-left-radius:5px;
        margin-top-right-radius:5px;
        margin-bottom-left-radius:5px;
        margin-bottom-right-radius:5px;
    
    }
    
    </style>
</head>
<body>
<center>
<div class="reg">
    <h2 >Registration Form</h2>
    <form action="RegisterServlet" method="post">
        <label for="name">Name:</label><br>
        <input type="text" id="name" name="name" required><br><br>
        
        <label for="mobile">Mobile:</label><br>
        <input type="text" id="mobile" name="mobile" required><br><br>
        
        <label for="location">Location:</label><br>
        <input type="text" id="location" name="location" required><br><br>
        
        <label for="address">Address:</label><br>
        <textarea id="address" name="address" rows="4" required></textarea><br><br>
        
        <label for="username">Username:</label><br>
        <input type="text" id="username" name="username" required><br><br>
        
        <label for="password">Password:</label><br>
        <input type="password" id="password" name="password" required><br><br>
        
        <label for="confirm_password">Confirm Password:</label><br>
        <input type="password" id="confirm_password" name="confirm_password" required><br><br>
        
        <input type="submit" value="Submit">
    </form>
    </div>
    </center>
</body>
</html>
