<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<link href="<c:url value="css/app.css" />" rel="stylesheet"
	type="text/css">
<title>Quickbucks: Login</title>
</head>


<body >

<header>
        
            <h1 style="display: inline-block;text-align:left; font-size:30px;font-family:arial;">QuickBucks</h1>
            <a style = "float:right;display: inline-block;margin: 0 0 15px 15px;float: right;text-align:right; font-size:20px;font-family:arial;" href="/homePageLoggedIn.html">Sign In</a>
            <a href="/registration.html" style = "float:right;display: inline-block;text-align:right; font-size:20px;font-family:arial;">Register</a> 
            <br>
			
			<link href="css/genericstyle.css" rel="stylesheet" type="text/css">
    </header>
	<nav style = "margin: 15px 15px 0 0;display:inline;text-align:right; font-size:16px;font-family:arial;"> 
        <ul>
		 <li><a href="/homePage.html" >Home</a></li>
         
         <li><a href="" style="font-size: 15px; font-family: arial">Legal</a></li>
         <li><a href=""style="font-size: 15px; font-family: arial" >FAQ</a></li>
      
		</ul>
        <br>
    </nav>
	
	<section id = "body">
	<div class="details">
		<a href="forgot.html"
			class="button red">Forgot Password?</a>
	</div>

	<form action="/login" method="post">

		<div class="lc-block2">
			<div>
				<input type="text" class="style-4" name="username"
					placeholder="Email Address" />
			</div>
			<div>
				<input type="password" class="style-4" name="password"
					placeholder="Password" />
			</div>
			<div>
				<input type="submit" value="Sign In" class="button red" />
			</div>
			<c:if test="${param.error ne null}">
				<div class="alert-danger">Invalid username and password.</div>
			</c:if>
			<c:if test="${param.logout ne null}">
				<div class="alert-normal">You have been logged out.</div>
			</c:if>
		</div>
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
	</form>
	</section>
<div class="footer">
  <p href="" style = "text-align:center; font-size:15px;font-family:arial;">2017 Quick Bucks, ASE Inc</p>
</div>
</body>
</html>
