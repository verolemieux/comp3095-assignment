<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<link href="css/material-design-iconic-font.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/dashboard.css">
<meta charset="ISO-8859-1">
<title>Error</title>
</head>
<body>
<div class="page-wrapper bg-gra-01 p-t-180 p-b-100 font-poppins" style="min-width:165px;">
	<div class="wrapper wrapper--w780">
	<div class="flex-horizontal">
	<div class="box">
		<h1 style="text-align:center;"><span>&#9888;</span> Error <span>&#9888;</span></h1>
		Sorry, the page you requested cannot be found. Please click below to be redirected.
		
		<form action="dashboard.jsp" method="post">
			<div class="p-t-10 buttons-container">
            	<button class="btn btn--pill btn--blue" name="button" type="submit">Redirect</button>
            </div>
		</form>
	</div>
	</div>
</div>
</body>
</html>