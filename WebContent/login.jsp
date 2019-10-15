<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<script src="https://www.google.com/recaptcha/api.js"></script>
<link href="css/material-design-iconic-font.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<body>
<div class="page-wrapper bg-gra-01 p-t-180 p-b-100 font-poppins">
	<div class="wrapper wrapper--w780">
		<div class="card card-3">
			<div class="card-heading"></div>
                <div class="card-body">
                    <h2 class="title">LOGIN</h2>
                    <h4 class="error">${errorMessage}</h4>
                    <h5>${errorMessage2}</h5>
                    <form action="Login" method="post">
                        <div class="input-group">
                            <input class="input--style-3" type="text" placeholder="Username" name="username">
                        </div>
                        <div class="input-group">
                            <input class="input--style-3" type="password" placeholder="Password" name="password">
                        </div>
						<div class="g-recaptcha" name="recaptcha"
   							data-sitekey="6LcQa7sUAAAAAMTVVLjhfdBIxpAmlDhZ-BuTvL-_">
   						</div>
                        <div class="p-t-10 buttons-container">
                            <button class="btn btn--pill btn--blue" name="button" value="login" type="submit">Login</button>
                            <button class="btn btn--pill btn--blue" name="button" value="register" type="submit">Register</button>
                        </div>
                        <div class="forgot-password">
							<p><a class="p-white" href="forgotpassword.jsp">Forgot your password?</a></p>
  						</div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>