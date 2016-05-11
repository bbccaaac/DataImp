package bing.payne.fu;

import java.sql.*;
import java.util.*;
import java.io.*;

public class Database {
   private String UserName;
   private String PassWord;
   private String Url;
   private String DriverInfo;
   
   public Database(String PropertiesFileName) throws IOException
   {
	   Properties pro = new Properties();
	   //获取当前目录路径
	   //System.out.println(System.getProperty("user.dir"));
	   FileInputStream in = new FileInputStream(PropertiesFileName);
	   pro.load(in);	   
	   UserName = pro.getProperty("user");
	   PassWord = pro.getProperty("password");
	   Url = pro.getProperty("url");
	   DriverInfo = pro.getProperty("driver");
	   in.close();
   }
   
   public Database(String username, String password, String url, String driver)
   {
	   this.UserName = username;
	   this.PassWord = password;
	   this.Url = url;
	   this.DriverInfo = driver;
   }
   
   public Connection getConn() throws Exception
   {
	   Class.forName(this.DriverInfo);
	   return DriverManager.getConnection(this.Url, this.UserName, this.PassWord);	  
   }
}
