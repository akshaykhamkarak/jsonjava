package com.mindtree.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	
	private static String URL="jdbc:mysql://localhost:3307/BookAuthorjson";
	private static String USERNAME="root";
	private static String PASSWORD="Trishali@1999";

	  public static Connection getConnection()throws SQLException{
		  Connection con=null;
		  try{
			  Class.forName("com.mysql.cj.jdbc.Driver");
			  con=DriverManager.getConnection(URL,USERNAME,PASSWORD);
		  }
		  catch (Exception e) {
		
		}
		return con;
	  }
}
