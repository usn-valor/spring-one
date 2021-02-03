<%@ page import="ru.geekbrains.ru.geekbrains.persist.User" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: macbook
  Date: 01.02.2021
  Time: 19:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Users</title>
</head>
<body>

<table>
    <tr>
        <th>Id</th>
        <th>Username</th>
    </tr>
    <% for (User user : (List<User>) request.getAttribute("users")) { %>
    <tr>
        <td><%= user.getId() %></td>
        <td>
            <a href="<%= application.getContextPath() + "/user/" + user.getId() %>"><%= user.getUsername() %></a>
        </td>
    </tr>
    <% } %>
</table>

</body>
</html>
