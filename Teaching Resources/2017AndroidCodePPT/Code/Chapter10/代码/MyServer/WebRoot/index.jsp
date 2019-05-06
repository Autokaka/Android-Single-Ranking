<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<html>
  <head>
    <title>My JSP 'index.jsp' starting page</title>
  </head>
  <body>
        	登录演示<br>
  
	  <form action="${pageContext.request.contextPath}/servlet/LoginServlet" method="post">
	      账号：<input type = "text" name = "username" ><br>
	     密码：<input type = "text" name = "password" ><br>
	  <input type = "submit" value = "提交">
	  </form>
  
  </body>
  
</html>
