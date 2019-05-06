package test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet{

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	              throws ServletException, IOException {
         response.setContentType("text/html;charset=utf-8");
         request.setCharacterEncoding("utf-8");
         response.setCharacterEncoding("utf-8");
         PrintWriter out = response.getWriter();
	          
		 String username = request.getParameter("username");
		 String password = request.getParameter("password");
	                 
         //判断用户名密码是否正确
         if(username.equals("admin") && password.equals("123456")) {
             out.print("Login succeeded!");
         }else {
             out.print("Login failed!");
         } 
	     out.flush();
	     out.close();
	 }
}
