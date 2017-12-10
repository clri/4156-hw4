<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Quickbucks: Reset Password</title>
	<link href="../../../css/genericstyle.css" rel="stylesheet" type="text/css">
    </head>
    <body>
     <header>

             <h1 style="display: inline-block;text-align:left; font-size:30px;font-family:arial;">QuickBucks</h1>
            <a style = "float:right;display: inline-block;margin: 0 0 15px 15px;float: right;text-align:right; font-size:20px;font-family:arial;" href="/homePageLoggedIn.html">Sign In</a>
            <a href="/registration.html" style = "float:right;display: inline-block;text-align:right; font-size:20px;font-family:arial;">Register</a> 
            <br>
			
	
    </header>
	
	<nav style = "margin: 15px 15px 0 0;display:inline;text-align:right; font-size:16px;font-family:arial;">
		<ul>
		<li><a href="../../../homePage.html" >Home</a></li>
         <li><a href="" style="font-size: 15px; font-family: arial">Legal</a></li>
         <li><a href=""style="font-size: 15px; font-family: arial" >FAQ</a></li>
       
    </ul>
    </nav>

    <section id="body" >
    <h2 style="text-align:center;">Email: ${email}</h2>

        <center>
        <form id="reset"   action="/demo/reset" style = "margin: 20px 15px 0 0;float:none;">
        <input type="text" name="email" value=${email} readonly /><br />
        <input type="text" name="token" value=${token} readonly /><br />
        <input type="text" name="password" value="" /><br />
		<input type="submit" value="request">
        </form>
        </center>

    </section>
	
	<div class="footer">
  <p href="" style = "text-align:center; font-size:15px;font-family:arial;">2017 Quick Bucks, ASE Inc</p>
</div>
    </body>
</html>
