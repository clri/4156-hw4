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
			
			<link href="../../../css/genericstyle.css" rel="stylesheet" type="text/css">
    </header>

    <section id="JobPosting" >
    <h2 style="text-align:center;">Job ID: ${jobID}</h2>

        <p>Job Title: ${title} </p>
        <p>Tags: ${tags} </p>
        <p>Description: ${desc} </p>


        <center>
        <form id="request"   action="/demo/request" style = "margin: 20px 15px 0 0;float:none;">
        <input type="text" name="id" value=${jobID} readonly /><br />
		<input type="submit" value="request">
        </form>
        <form id="contact"   action="/demo/contact" style = "margin: 20px 15px 0 0;float:none;">
        <input type="text" name="id" value=${jobID} readonly style="display:none;" /><br />
		<input type="submit" value="contact" />
        </form>
        <a href="searchJobs.html">Back</a>
        </center>

    </section>
    </body>
</html>
