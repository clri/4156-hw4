<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Quickbucks: Compose Message</title>
    </head>
    <body>
    <header>

            <h1 style="display: inline-block;text-align:left; font-size:30px;font-family:arial;">QuickBucks</h1>
            <p href="" style = "float:right;display: inline-block;text-align:right; font-size:15px;font-family:arial;">Welcome, user!</p>
            <p>By submitting this form, you agree to your email address being sent to the job poster.</p>
            <br />
    </header>

    <section id="Reset" >
    <h2 style="text-align:center;">Job ID: ${id}</h2>

        <center>
        <form id="compose"   action="/demo/sendcontact" style = "margin: 20px 15px 0 0;float:none;">
        <input type="text" name="id" value=${id} readonly style="display:none;"/><br />
        <input type="textarea" name="msg" value="" /><br />
		<input type="submit" value="send">
        </form>
        </center>

    </section>
    </body>
</html>
