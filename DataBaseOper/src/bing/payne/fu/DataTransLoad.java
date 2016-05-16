package bing.payne.fu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;

import java.sql.Types;
import java.text.SimpleDateFormat;

import bing.payne.fu.utils.LoadConfUtil;

public class DataTransLoad {
	public Connection conn = null;
	public PreparedStatement pstTar = null;
	public DataExtract data = null;
	public ReadExcel excel = null;
	public Workbook hssfWorkbook = null;
	public String TargetDBFile = null;
	public String TargetSqlFile = null;
	public String sql = null;
	public ReadTxt txt = null;
	
	public DataTransLoad(String TargetDBFile, DataExtract srcData)
	{
		try
		{
			data = srcData;
			System.out.println("初始化目标库...");
			Database DB = new Database(TargetDBFile);
		    conn = DB.getConn();
		    conn.setAutoCommit(false);
			pstTar = conn.prepareStatement(data.tarSql);
			System.out.println("目标库初始化完成");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public DataTransLoad(String TargetDBFile, String TargetSqlFile, ReadExcel excel)
	{
		this.excel = excel;
		this.TargetDBFile = TargetDBFile;
		this.TargetSqlFile = TargetSqlFile;		    							
	}
	
	public DataTransLoad(Connection TarConn, String sql, ReadExcel excel)
	{
		this.conn = TarConn;
		this.excel = excel;
		this.sql = sql;
	}
	
	public DataTransLoad(Connection TarConn, String sql, ReadTxt txt)
	{
		this.conn = TarConn;
		this.txt = txt;
		this.sql = sql;
		try {
			conn.setAutoCommit(false);
			pstTar = conn.prepareStatement(sql);
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
	}
	
	public DataTransLoad(Connection TarConn, String sql, DataExtract de)
	{
		this.conn = TarConn;
		this.sql = sql;
		this.data = de;
		try {
			conn.setAutoCommit(false);
			this.pstTar = conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void DataTransferLoad()
	{
		try
		{
			System.out.println("开始导入数据...");
		    ResultSet rs = data.getSelectRes();
		    int srcDataColoumn = data.numberOfColumns;
		    while(rs.next())
		    {
		    	for(int i = 1; i <= srcDataColoumn; i++)
				{
					int ColType = data.rsmd.getColumnType(i);
					switch(ColType)
					{
					case Types.INTEGER:
						pstTar.setInt(i, rs.getInt(i));
						break;
					case 12:					
					    pstTar.setString(i, rs.getString(i));												
						break;
					case Types.DATE:
						pstTar.setDate(i, rs.getDate(i));
						break;
					case Types.FLOAT:
						pstTar.setFloat(i, rs.getFloat(i));
						break;
					case Types.DOUBLE:
						pstTar.setDouble(i, rs.getDouble(i));
						break;
					case Types.NUMERIC:
						pstTar.setBigDecimal(i, rs.getBigDecimal(i));
						break;
					case Types.REAL:
						pstTar.setFloat(i, rs.getFloat(i));
						break;
					case Types.DECIMAL:
						pstTar.setBigDecimal(i, rs.getBigDecimal(i));
						break;
					case Types.TIME:
						pstTar.setTime(i, rs.getTime(i));
						break;
					case Types.TIMESTAMP:
						pstTar.setTimestamp(i, rs.getTimestamp(i));
						break;
					case Types.CLOB:
						pstTar.setClob(i, rs.getClob(i));
						break;
					case Types.BLOB:
						pstTar.setBlob(i, rs.getBlob(i));
						break;						
					}				
				}
		    	pstTar.addBatch();
		    	
		    	//pstTar.execute();
		    }
		    pstTar.executeBatch();
		    conn.commit();
		    //conn.close();
			System.out.println("数据导入完成");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void TxtDataTransferLoad(String FieldSplit)
	{
		System.out.println("开始导入文本数据...");
		String[] strs = null;
		try {
			while((strs = txt.getData(FieldSplit)) != null)
			{
				int i = 1;
				
				for(String str : strs)
				{
					
						pstTar.setString(i, str);
						i++;						
				}
				pstTar.addBatch();
			
			}
			pstTar.executeBatch();
		    conn.commit();
		    conn.close();
			System.out.println("数据导入完成");
		} catch (SQLException e) {					
			e.printStackTrace();
		}			
	}
	
	public void ExcelDataTransferLoad() throws NumberFormatException, SQLException
	{
		hssfWorkbook = excel.hssfWorkbook;
		int numSheets = hssfWorkbook.getNumberOfSheets();
		for(int i = 0; i < numSheets; i++)
		{
			new Thread(new handleExcelThread(TargetDBFile, TargetSqlFile, excel, i)).start();
		}							
	}

	public void ExcelDataTransferLoadUI() throws NumberFormatException, SQLException
	{
		hssfWorkbook = excel.hssfWorkbook;
		int numSheets = hssfWorkbook.getNumberOfSheets();
		for(int i = 0; i < numSheets; i++)
		{
			new Thread(new handleExcelThread(conn, sql, excel, i)).start();
		}							
	}
	
	public void ExcelDataTransferLoadUI(int numSheet) throws NumberFormatException, SQLException
	{
		hssfWorkbook = excel.hssfWorkbook;
		new Thread(new handleExcelThread(conn, sql, excel, numSheet)).start();
									
	}
}
