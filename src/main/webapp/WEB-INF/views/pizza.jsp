<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/3 0003
  Time: 20:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Pizza JSP View</title>
</head>
<body>
<table border="1">
    <tr>
        <td>
            NAME
        </td>
        <td>
            Flavor
        </td>
        <td>
            Toppings
        </td>
    </tr>
    <tr>
        <td>${pizza.name}</td>
        <td>${pizza.flavor}</td>
        <td>
            <c:forEach var="item" items="${pizza.toppings}">
                <c:out value="${item}"/>&nbsp;

            </c:forEach>
        </td>
    </tr>
</table>
</body>
</html>
