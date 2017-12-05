<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Jobs Awaiting Review</title>
	 <link href="../../../css/genericstyle.css" rel="stylesheet" type="text/css">
    </head>
    <body>
       <header>

            <h1 style="display: inline-block;text-align:left; font-size:30px;font-family:arial;">QuickBucks</h1>
            <p href="" style = "float:right;display: inline-block;text-align:right; font-size:15px;font-family:arial;">Welcome, user!</p>
            <form action="/logout" method="get">
                    <input type="submit" class="button red big" value="Sign Out" />
            </form>
            <br>
			
	
    </header>
    <nav style = "margin: 15px 15px 0 0;display:inline;text-align:right; font-size:16px;font-family:arial;">
		<ul>
		<li><a href="../../../homePageLoggedIn.html" >Home</a></li>
         <li><a href ="../../../searchJobs.html" style="font-size: 15px; font-family: arial">Search Jobs</a></li>
         <li><a href="../../../PostJob.html" style="font-size: 15px; font-family: arial">Post Job</a></li>
         <li><a href="" style="font-size: 15px; font-family: arial">Legal</a></li>
         <li><a href=""style="font-size: 15px; font-family: arial" >FAQ</a></li>
         <li><a href="../../../notifications" style="font-size: 15px; font-family: arial">Inbox</a></li>
    </ul>
    </nav>

    <section id="body" >
    <h2 style="text-align:center;">${type} to Review:</h2>

  
         <table id = "searchTable">
		  <tr id="searchtr">
		   <th id="searchth">Job</th>
		   <th id="searchth">Review?</th>
		 </tr>
	
        <c:forEach items="${results}" var="request">

		
            <tr id = "searchtr">
                <td id="searchtd"><a href = "../finalPage?id=${request.job}"><c:out value="${request.title}" /></a></td>
                <td id="searchtd"><a href = "../createAReview?id=${request.job}">Review</a></td>
            </tr>
        </c:forEach>

    </table>
        
      
        <center>
        <a href="../../../searchJobs.html">Back</a> 
        </center>
   
    </section>
	
	<div class="footer">
  <p href="" style = "text-align:center; font-size:15px;font-family:arial;">2017 Quick Bucks, ASE Inc</p>
</div>
    </body>
</html>
