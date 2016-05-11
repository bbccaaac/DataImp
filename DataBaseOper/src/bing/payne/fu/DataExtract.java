package bing.payne.fu;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;



import bing.payne.fu.utils.LoadConfUtil;

public class DataExtract {
    private Connection conn = null;
    private String sql;
    private PreparedStatement pst = null;
    public int numberOfColumns = 0;
    public ResultSetMetaData rsmd = null;
    public String tarSql;
    
    public DataExtract(String DBFileName, String SqlFileName) throws Exception
    {
    	System.out.println("初始化源库...");
    	Database DB = new Database(DBFileName);
	    conn = DB.getConn();
	    initSql(SqlFileName);
	    initStatement();
	    System.out.println("源库初始化完成");
    }
    
    public DataExtract(Connection SrcConn, String sql)
    {
    	this.conn = SrcConn;
    	this.sql = sql;
    	try {
			this.initStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
	private void initSql(String SqlFileName)
	{
		try
		{
			Properties prop = LoadConfUtil.Load(SqlFileName);
			sql = prop.getProperty("srcSql");
			tarSql = prop.getProperty("tarSql");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void initStatement() throws SQLException
	{
	    pst = conn.prepareStatement(sql);
	}
	
	public ResultSet getSelectRes() throws SQLException
	{		
		ResultSet rs = pst.executeQuery();
		rsmd = (ResultSetMetaData) rs.getMetaData();
		numberOfColumns = rsmd.getColumnCount();
		for(int i = 1; i <= numberOfColumns; i++)
		{
		   System.out.println("第" + i + "列是" + rsmd.getColumnTypeName(i));
		   System.out.println("第" + i + "列是" + rsmd.getColumnType(i));
		}
		//conn.close();
		return rs;
	}
	
}
