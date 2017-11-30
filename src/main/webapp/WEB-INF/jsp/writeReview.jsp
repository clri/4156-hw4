<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Write a Review</title>
    </head>
    <body>
    <header>

            <h1 style="display: inline-block;text-align:left; font-size:30px;font-family:arial;">QuickBucks</h1>
            <p href="" style = "float:right;display: inline-block;text-align:right; font-size:15px;font-family:arial;">Welcome, user!</p>
            <br>
    </header>

    <section id="Review" >
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
    </body>
</html>
