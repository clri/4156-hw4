<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Search Job</title>
    <link href="../../../css/genericstyle.css" rel="stylesheet" type="text/css">
	</head>
    <body>
    <header>
	
        
            <h1 style="display: inline-block;text-align:left; font-size:30px;font-family:arial;">QuickBucks</h1>
            <p href="" style = "float:right;display: inline-block;text-align:right; font-size:15px;font-family:arial;">Welcome, <a href="/myprofile">user!</a></p> 
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
		 <li><a href="../../../mypage.html" style="font-size: 15px; font-family: arial">My Page</a></li>
         <li><a href="../../../notifications" style="font-size: 15px; font-family: arial">Inbox</a></li>
    </ul>
    </nav>
    <section id="body" >
    <h2 style="text-align:center;">Search Results:</h2>

  
         <table id="searchTable">
		 <tr id="searchtr">
		   <th id="searchth">Job ID</th>
		   <th id="searchth">Job Title</th>
		   <th id="searchth">Description</th>
		   <th id="searchth">Category</th>
		 </tr>
	
        <c:forEach items="${results}" var="job">
		<%request.setAttribute("jobLink", "<a href='finalPage?id=");
		  request.setAttribute("jobLinkMid", "<a href='finalPage?id=");
		  request.setAttribute("jobLinkEndd", "<a href='finalPage?id=");%>
		
            <tr id="searchtr">
                <td id="searchtd"><c:out value="${job.id}" /></td>
                <td id="searchtd"><a href = "../finalPage?id=${job.id}"><c:out value="${job.jobtitle}" /></a></td>
                <td id="searchtd"><c:out value="${job.jobdesc}" /></td>
                <td id="searchtd"><c:out value="${job.category}" /></td>
            </tr>
        </c:forEach>

    </table>
	
   
    </section>
	
	    <div class="pagination">
		  <a href="../demo/search?keywords=${key}&category=${cat}&pageNum=1">&laquo;</a>
		  <a href="../demo/search?keywords=${key}&category=${cat}&pageNum=${prev}">&lt;</a>
		  <a class = "curr" href="#">${currPage}</a>
		  <a href="../demo/search?keywords=${key}&category=${cat}&pageNum=${next}">&gt;</a>
		  <a href="../demo/search?keywords=${key}&category=${cat}&pageNum=${max}">&raquo;</a>
	</div>
      
        <center>
        <a href="../../../searchJobs.html">Back</a> 
        </center>
	
	<div class="footer">
  <p href="" style = "text-align:center; font-size:15px;font-family:arial;">2017 Quick Bucks, ASE Inc</p>
</div>
    </body>
</html>
