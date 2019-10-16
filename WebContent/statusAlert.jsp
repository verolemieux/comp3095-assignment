<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="css/statusAlert.css">
</head>
<body>
<% String message = (String)request.getAttribute("statusMessage");
	if(message != null){%>
		<div class="alert" style="background-color: <%=request.getAttribute("color")%>; width: 30%">
			<span class="closebtn" style="cursor:pointer;float:right;" onclick="this.parentElement.style.display='none';">&times;</span>
			<span><h5>${statusMessage}</h5></span>
		</div>
	<%}%>
</body>
</html>