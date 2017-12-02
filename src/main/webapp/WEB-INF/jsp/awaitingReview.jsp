<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Jobs Awaiting Review</title>
    </head>
    <body>
    <header>
        
            <h1 style="display: inline-block;text-align:left; font-size:30px;font-family:arial;">QuickBucks</h1>
            <p href="" style = "float:right;display: inline-block;text-align:right; font-size:15px;font-family:arial;">Welcome, user!</p> 
            <br>
    </header>

    <section id="SearchResults" >
    <h2 style="text-align:center;">${type} to Review:</h2>

  
         <table>
	
        <c:forEach items="${results}" var="request">

		
            <tr>
                <td><a href = "../finalPage?id=${request.job}"><c:out value="${request.title}" /></a></td>
                <td><a href = "../createAReview?id=${request.job}">Review</a></td>
            </tr>
        </c:forEach>

    </table>
        
      
        <center>
        <a href="../../../searchJobs.html">Back</a> 
        </center>
   
    </section>
    </body>
</html>
