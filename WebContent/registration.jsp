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
<!-- Make if statement here  -->
	<% String message = (String)request.getAttribute("statusMessage");
	   String color = (String)request.getAttribute("color"); 	
		if(message != null){
			if(color == "green"){%>
				<div class="alert" style="background-color:green;width:30%">
					<span class="closebtn" style="cursor:pointer;float:right;" onclick="this.parentElement.style.display='none';">&times;</span>
					<%
						message = (String)request.getAttribute("statusMessage");
			  			out.println("<span>"+message+"</span>");
					%>
				</div>
			<%				
			}else{%>
				<div class="alert" style="background-color:red;width:30%">
					<span class="closebtn" style="cursor:pointer;float:right;" onclick="this.parentElement.style.display='none';">&times;</span>
					<%
						message = (String)request.getAttribute("statusMessage");
			  			out.println("<span>"+message+"</span>");
					%>
				</div>
			<%}
		}
	%>

        <div class="wrapper wrapper--w780">
            <div class="card card-3">
                <div class="card-heading">
                </div>
                <div class="card-body">
                    <h2 class="title">REGISTRATION</h2>
                    <form action="Register" method="POST">
                        <div class="input-group">
                            <input class="input--style-3" type="text" placeholder="First Name" name="firstname">
                        </div>
                        <div class="input-group">
                            <input class="input--style-3" type="text" placeholder="Last Name" name="lastname">
                        </div>
                        <div class="input-group">
                            <input class="input--style-3" type="text" placeholder="Address" name="address">
                        </div>
                        
                        <div class="input-group">
                            <input class="input--style-3" type="email" placeholder="Email" name="email">
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
                        <div>
                            <button class="btn btn--pill btn--blue" type="submit">Register</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
 

</body>
</html>