<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>ViewJob</title>
    </head>
    <body>
    <header>
        
            <h1 style="display: inline-block;text-align:left; font-size:30px;font-family:arial;">QuickBucks</h1>
            <p href="" style = "float:right;display: inline-block;text-align:right; font-size:15px;font-family:arial;">Welcome, user!</p> 
            <br>
    </header>

    <section id="JobPosting" >
    <h2 style="text-align:center;">Job ID: ###</h2>
  
        <p>Job Title:</p> ${title}
        <p>Employer:</p> ${emp}
        <p>Tags:</p> ${tags}
        <p>Description:</p> ${desc}
        
      
        <center>
        <form id="request"  onSubmit="pressSubmit()" style = "margin: 20px 15px 0 0;float:none;">
        <input type="submit" value="request">
        </form>
        <a href="">Back</a> 
        </center>
   
    </section>
    </body>
</html>

<%@include file="/WEB-INF/views/includes/footer.jsp"%>