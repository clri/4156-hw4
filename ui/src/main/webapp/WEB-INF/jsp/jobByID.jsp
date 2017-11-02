<%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JobByID</title>
    </head>
    <body>
    <header>
        
            <h1 style="display: inline-block;text-align:left; font-size:30px;font-family:arial;">QuickBucks</h1>
            <p href="" style = "float:right;display: inline-block;text-align:right; font-size:15px;font-family:arial;">Welcome, user!</p> 
            <br>
    </header>

    <section>
    <h2 style="text-align:center;">Enter an ID:</h2>
  
        <form:form method = "GET" action = "/finalPage">
         <table>
            <tr>
               <td>
				  <input type = "text" name = "id"/>
                  <input type = "submit" value = "Lookup"/>
               </td>
            </tr>
         </table>  
      </form:form>
        
      
        <center>
        <a href="../../../searchJobs.html">Back</a> 
        </center>
   
    </section>
    </body>
</html>



<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
 

