<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Notifications</title>
    </head>
    <body>
    <header>

            <h1 style="display: inline-block;text-align:left; font-size:30px;font-family:arial;">QuickBucks</h1>
            <p href="" style = "float:right;display: inline-block;text-align:right; font-size:15px;font-family:arial;">Welcome, user!</p>
            <br>
    </header>

    <section id="SearchResults" >
    <h2 style="text-align:center;">Notification:</h2>


         <table>

        <c:forEach items="${notifs}" var="req">

            <tr>
                <td><c:out value="${req.id}" /></td>
                <td>
                        <c:if test="${req.employee == userr}">
                                <c:out value="${req.employer}" />
                        </c:if>
                        <c:if test="${req.employee != userr}">
                                <c:out value="${req.employee}" />
                        </c:if>
                </td>
                <td><c:choose>
                        <c:when test="${req.employee == userr}">
                                Has decided
                        </c:when>
                        <c:otherwise>
                                Has requested
                        </c:otherwise>
                </c:choose></td>
                <td>
                        <c:out value="${req.job}" />
                </td>
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
                        <c:when test="${req.employee == userr}">
                                <p style="display:none;">x</p>
                        </c:when>
                        <c:otherwise>
                                <a href = "decide?id=${req.id}&decision=1">
                                        Accept
                                </a>
                        </c:otherwise>
                </c:choose> </td>
                <td> <c:choose>
                        <c:when test="${req.employee == userr}">
                                <p style="display:none;">x</p>
                        </c:when>
                        <c:otherwise>
                                <a href = "decide?id=${req.id}&decision=2">
                                        Reject
                                </a>
                        </c:otherwise>
                </c:choose></td>

            </tr>
        </c:forEach>

    </table>


        <center>
        <a href="homePageLoggedIn.html">Home</a>
        </center>

    </section>
    </body>
</html>
