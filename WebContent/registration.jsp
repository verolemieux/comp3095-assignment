<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Registration</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<link rel="stylesheet" href="css/registration.css">
<link href="css/material-design-iconic-font.min.css" rel="stylesheet">
</head>
<body>
<div class="page-wrapper bg-gra-01 p-t-180 p-b-100 font-poppins">
<div class="alert">
	<%@ include file="statusAlert.jsp" %>
</div>
        <div class="wrapper wrapper--w780">
            <div class="card card-3">
                <div class="card-heading"></div>
                <div class="card-body">
                    <h2 class="title">REGISTRATION</h2>
                    <form action="Register" method="POST">
                        <div class="input-group">
                            <input class="input--style-3" type="text" placeholder="First Name" name="firstname" value="${firstname}">
                        </div>
                        <div class="input-group">
                            <input class="input--style-3" type="text" placeholder="Last Name" name="lastname" value="${lastname}">
                        </div>
                        <div class="input-group">
                            <input class="input--style-3" type="text" placeholder="Address" name="address" value="${address}">
                        </div>
                        
                        <div class="input-group">
                            <input class="input--style-3" type="email" placeholder="Email" name="email" value="${email}">
                        </div>
                        <div class="input-group">
                            <input class="input--style-3" type="password" placeholder="Password" name="password">
                        </div>
                        <div class="input-group">
                            <input class="input--style-3" type="password" placeholder="Confirm Password" name="confirm_password">
                        </div>
         				<div class="checkbox-div">
                        	<input type="checkbox" name="agree_check" class="checkbox">
						    <label class="checkbox_label p-white" for="exampleCheck1">I agree to the terms of service</label>
						 </div>
                        <div class="p-t-10 buttons-container">
                            <button class="btn btn--pill btn--blue" type="submit">Register</button>
                       		<a href='/comp3095Assignment/' class="btn btn--pill btn--blue go-back-btn">Back</a>
           			   </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
 

</body>
</html>