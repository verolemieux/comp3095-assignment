<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<link href="css/material-design-iconic-font.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/dashboard.css">
<meta charset="ISO-8859-1">
<title>Navigation</title>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	<div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
      <li class="nav-item">
        <a class="nav-link" href="tab1.jsp">Tab1</span></a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="tab2.jsp">Tab2</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="tab3.jsp">Tab3</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="tab4.jsp">Tab4</a>
      </li>
      </ul>

<%  if(session.getAttribute("authUser") == null)
	{	
		request.setAttribute("GuestUser", "true");
		request.getRequestDispatcher("/Login").forward(request, response);
	}%>
	  <div class="name">
	  	Welcome, ${authUser.getFirstname()}!
	  </div>
	  &nbsp;&nbsp;
      <form action="Login" method="post" class="form-inline my-2 my-lg-0">
      <input type="submit" name="Logout" value="Logout" class="btn btn--pill btn--blue">
      </form>
      </div>
</nav>
</body>
</html>