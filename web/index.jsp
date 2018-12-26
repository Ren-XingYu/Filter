<%--
  Created by IntelliJ IDEA.
  User: RenXingYu
  Date: 2018/12/26
  Time: 13:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
    <form action="LoginServlet" method="post">
      <table>
        <tr>
          <td>username：</td>
          <td><input type="text" name="username"></td>
        </tr>
        <tr>
          <td><input type="submit" value="login"></td>
        </tr>
      </table>
    </form>
    <hr>
    <form action="RegisterServlet" method="post">
      <table>
        <tr>
          <td>username：</td>
          <td><input type="text" name="username"></td>
        </tr>
        <tr>
          <td><input type="submit" value="login"></td>
        </tr>
      </table>
    </form>
  </body>
</html>
