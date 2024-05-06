package com.servlet.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String SELECT_QUERY = "SELECT * FROM USER_DETAILS WHERE USERID=? AND PIN=?";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();
        resp.setContentType("text/html");

        String userid = req.getParameter("userid");
        String pin = req.getParameter("pin");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql:///firsttable", "root", "");
             PreparedStatement ps = con.prepareStatement(SELECT_QUERY)) {

            ps.setString(1, userid);
            ps.setString(2, pin);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Valid credentials
                pw.println("Login Successful");
                
                String contextpath=req.getContextPath();
                req.getSession().setAttribute("userid", userid);
                
                resp.sendRedirect(contextpath+"/usertransaction.jsp");
//                String welcomemsg="<html><body><p>Welcome"+userid+"</p></body></html>";
//                pw.println(welcomemsg);
            } else {
                // Invalid credentials
                pw.println("Login Failed. Please check your username and password.");
            }

        } catch (SQLException se) {
            pw.println(se.getMessage());
            se.printStackTrace();
        } catch (Exception e) {
            pw.println(e.getMessage());
            e.printStackTrace();
        }

        pw.close();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		doPost(req,resp);
}
}
