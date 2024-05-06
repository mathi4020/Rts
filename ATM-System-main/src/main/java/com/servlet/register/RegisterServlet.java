package com.servlet.register;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	
	//create the query
	private static final String INSERT_QUERY="INSERT INTO USER_DETAILS(NAME,USERID,PIN,CONTACT,BALANCE) VALUES(?,?,?,?,0)";
	
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		//get PrintWriter
		
		PrintWriter pw=resp.getWriter();
		//set Content type to understand the document type
		resp.setContentType("text/html");
		//To read the form values we use request.getParameter function
		
		String name=req.getParameter("name");
		String userid=req.getParameter("userid");
		String pin=req.getParameter("pin");
		String contact=req.getParameter("contact");

//		System.out.println("Name of the user: "+name);
//		System.out.println("Contact of the user: "+contact);
//		System.out.println("User ID of the user: "+userid);
//		System.out.println("PIN of the user: "+pin);
		
		//Load the JDBC Driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Create the connection
		try(Connection con=DriverManager.getConnection("jdbc:mysql:///firsttable","root","");
				
				PreparedStatement ps=con.prepareStatement(INSERT_QUERY);){
				//set the values
				ps.setString(1,name);
				ps.setString(2,userid);
				ps.setString(3,pin);
				ps.setString(4,contact);
				
				//execute the query
				
				int count=ps.executeUpdate();
				if(count==0)
				{
					pw.println("Record Not Stored into Database");
				}
				else
				{
					pw.println("Record Stored into Database");
				}
		}
		catch(SQLException se) {
			pw.println(se.getMessage());
			se.printStackTrace();
		}
		catch(Exception e)
		{
			pw.println(e.getMessage());
			e.printStackTrace();
		}
		
		//close the stream
		pw.close();
	}
	
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		doGet(req,resp);
	}
}
