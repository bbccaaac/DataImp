package bing.payne.fu;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import oracle.jdbc.OracleTypes;

public class YbshAutoScheduler {
	private Connection conn = null;
	private CallableStatement callStmt = null;
	public YbshAutoScheduler(Database db)
	{
		try {
			this.conn = db.getConn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void callProcedure(String procedure, int paraCount, String ...paraTypesAndValues)
	{
		System.out.println("-------  start 测试调用存储过程：无返回值");  
	    try {
	    	String lsSql = "{call " + procedure + "(";
	    	for(int i = 0; i < paraCount; i++)
	    	{
	    		lsSql += "?,";
	    	}
	    	String sql = lsSql.substring(0, lsSql.length() - 1);
	    	sql += ")}";
	    	callStmt = conn.prepareCall(sql);
	    	int j = 1;
	    	for(int i = 0; i < paraTypesAndValues.length; i += 2)
	    	{
	    		if(paraTypesAndValues[i].equals("string"))
	    		{
	    			callStmt.setString(j, paraTypesAndValues[i + 1]);
	    		}
	    		else if(paraTypesAndValues[i].equals("int"))
	    		{
	    			callStmt.setInt(j, Integer.parseInt(paraTypesAndValues[i + 1]));
	    		}
	    		else if(paraTypesAndValues[i].equals("float"))
	    		{
	    			callStmt.setFloat(j, Float.parseFloat(paraTypesAndValues[i + 1]));
	    		}
	    		else if(paraTypesAndValues[i].equals("double"))
	    		{
	    			callStmt.setDouble(j, Double.parseDouble(paraTypesAndValues[i + 1]));
	    		}
	    		else if(paraTypesAndValues[i].equals("double"))
	    		{
	    			SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	    			
	    			try {
						callStmt.setDate(j, new java.sql.Date(sdf.parse(paraTypesAndValues[i + 1]).getTime()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
	    		}
	    		
	    		j++;
	    	}
			callStmt.execute();
			System.out.println("调用成功了");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
	    
	}
	
	public String callProcedureHasOut(String procedure, int paraCount, String ...paraTypesAndValues)
	{
		System.out.println("-------  start 测试调用存储过程：有返回值"); 
		String result = "";
		Map<Integer, String> outIndex = new HashMap<>();
	    try {
	    	String lsSql = "{call " + procedure + "(";
	    	for(int i = 0; i < paraCount; i++)
	    	{
	    		lsSql += "?,";
	    	}
	    	String sql = lsSql.substring(0, lsSql.length() - 1);
	    	sql += ")}";
	    	callStmt = conn.prepareCall(sql);
	    	int j = 1;
	    	for(int i = 0; i < paraTypesAndValues.length; i += 2)
	    	{
	    		if(paraTypesAndValues[i].equals("string"))
	    		{
	    			callStmt.setString(j, paraTypesAndValues[i + 1]);
	    		}
	    		else if(paraTypesAndValues[i].equals("int"))
	    		{
	    			callStmt.setInt(j, Integer.parseInt(paraTypesAndValues[i + 1]));
	    		}
	    		else if(paraTypesAndValues[i].equals("float"))
	    		{
	    			callStmt.setFloat(j, Float.parseFloat(paraTypesAndValues[i + 1]));
	    		}
	    		else if(paraTypesAndValues[i].equals("double"))
	    		{
	    			callStmt.setDouble(j, Double.parseDouble(paraTypesAndValues[i + 1]));
	    		}
	    		else if(paraTypesAndValues[i].equals("date"))
	    		{
	    			SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	    			
	    			try {
						callStmt.setDate(j, new java.sql.Date(sdf.parse(paraTypesAndValues[i + 1]).getTime()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
	    		}
	    		//处理输出参数
	    		else if(paraTypesAndValues[i].equals("out"))
	    		{
	    			if(paraTypesAndValues[i + 1].equals("string"))
		    		{
	    				outIndex.put(j, paraTypesAndValues[i + 1]);
		    			callStmt.registerOutParameter(j, Types.VARCHAR);
		    		}
		    		else if(paraTypesAndValues[i + 1].equals("int"))
		    		{
		    			outIndex.put(j, paraTypesAndValues[i + 1]);
		    			callStmt.registerOutParameter(j, Types.INTEGER);
		    		}
		    		else if(paraTypesAndValues[i + 1].equals("float"))
		    		{
		    			outIndex.put(j, paraTypesAndValues[i + 1]);
		    			callStmt.registerOutParameter(j, Types.FLOAT);
		    		}
		    		else if(paraTypesAndValues[i + 1].equals("double"))
		    		{
		    			outIndex.put(j, paraTypesAndValues[i + 1]);
		    			callStmt.registerOutParameter(j, Types.DOUBLE);
		    		}
		    		else if(paraTypesAndValues[i + 1].equals("date"))
		    		{
		    			outIndex.put(j, paraTypesAndValues[i + 1]);
		    			callStmt.registerOutParameter(j, Types.DATE);		    			
		    		}
	    			
	    		}
	    		
	    		j++;
	    	}
			callStmt.execute();
		
			for(Integer key : outIndex.keySet())
			{
				String type;
				type = outIndex.get(key);
				if(type.equals("string"))
	    		{
					result += callStmt.getString(key);
	    		}
	    		else if(type.equals("int"))
	    		{
	    			result += callStmt.getInt(key);
	    		}
	    		else if(type.equals("float"))
	    		{
	    			result += callStmt.getFloat(key);
	    		}
	    		else if(type.equals("double"))
	    		{
	    			result += callStmt.getDouble(key);
	    		}
	    		else if(type.equals("date"))
	    		{
	    			result += callStmt.getDate(key);	    			
	    		}
				result += " ";
			}
			System.out.println("调用成功了");
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	    
	    
	}

}
