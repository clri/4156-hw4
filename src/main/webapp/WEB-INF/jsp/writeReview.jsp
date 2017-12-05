<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Write a Review</title>
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
    <h2 style="text-align:center;">Review for ${job}</h2>
	<p>Reviewing: ${user} </p>
	<p>Role: ${role} </p>
    <form id = "review" action="/demo/writeReview" method="get">
		<textarea name="reviewText" form="review"></textarea><br>
		<input type="radio" name="rating" value="1"> 1
		<input type="radio" name="rating" value="2"> 2
		<input type="radio" name="rating" value="3"> 3
		<input type="radio" name="rating" value="4"> 4
		<input type="radio" name="rating" value="5"> 5
		<div style="width: 200px;"><input type="submit" value="Submit Request" style="display: block;"></div>
	</form>
 

    </section>
	<div class="footer">
  <p href="" style = "text-align:center; font-size:15px;font-family:arial;">2017 Quick Bucks, ASE Inc</p>
</div>
    </body>
</html>
