<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Notifications</title>
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
    <h2 style="text-align:center;">Notification:</h2>


         <table>

        <c:forEach items="${notifs}" var="req">

            <tr>
                <td>
                        <c:out value="Job ${req.job}:" />
                </td>
                <td>
                        <c:if test="${req.employee == userr}">
                        <a href = "../profile?id=${req.employer}"><c:out value="User ${req.employer}" /></a>
                        </c:if>
                        <c:if test="${req.employee != userr}">
							<a href = "../profile?id=${req.employee}"><c:out value="User ${req.employee}" /></a>
                        </c:if>
                </td>
                <td><c:choose>
                        <c:when test="${req.employee == userr}">
                                has decided!
                        </c:when>
                        <c:otherwise>
                                has requested!
                        </c:otherwise>
                </c:choose></td>
                <td>
                        <c:choose>
                                <c:when test="${req.decision == 1}">
                                        Accepted
                                </c:when>
                                <c:when test="${req.decision == 2}">
                                        Rejected
                                </c:when>
                                <c:otherwise>
                                        Requested
                                </c:otherwise>
                        </c:choose>
                </td>
                <td> <c:choose>
                        <c:when test="${req.decision == 1}">
                                <a href = "createAReview?id=${req.job}">Review</a>
                                <a href = "read?id=${req.id}">Dismiss</a>
                        </c:when>
                        <c:when test="${req.employee == userr}">
                                <a href = "read?id=${req.id}">Dismiss</a>
                        </c:when>
                        <c:otherwise>
                                <a href = "decide?id=${req.id}&decision=1">
                                        Accept
                                </a>
                                <a href = "decide?id=${req.id}&decision=2">
                                        Reject
                                </a>
                        </c:otherwise>
                </c:choose> </td>

            </tr>
        </c:forEach>

    </table>


        <center>
        <a href="homePageLoggedIn.html">Home</a>
        </center>

    </section>
		<div class="footer">
  <p href="" style = "text-align:center; font-size:15px;font-family:arial;">2017 Quick Bucks, ASE Inc</p>
</div>
    </body>
</html>
