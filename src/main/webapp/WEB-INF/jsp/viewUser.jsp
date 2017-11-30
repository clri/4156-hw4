<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>View User</title>
    </head>
    <body>
    <header>

            <h1 style="display: inline-block;text-align:left; font-size:30px;font-family:arial;">QuickBucks</h1>
            <p href="" style = "float:right;display: inline-block;text-align:right; font-size:15px;font-family:arial;">Welcome, user!</p>
            <br>
    </header>

    <section id="UserProfile" >
    <h2 style="text-align:center;">User ID: ${userID}</h2>
        <p>Name: ${name}</p>
        <p>School: ${school}</p>
        <p>Degree: ${degree}</p>
		<p>Location: ${location}</p>
		
    <h3 style="text-align:center;">Reviews:</h2>

  
     <!--<table>
	
        <c:forEach items="${reviews}" var="rev">
            <tr>
                <td><c:out value="${rev.user}" /></td>
                <td><c:out value="${rev.jobdesc}" /></td>
                <td><c:out value="${job.category}" /></td>
            </tr>
        </c:forEach>

    </table>-->



    </section>
    </body>
</html>
