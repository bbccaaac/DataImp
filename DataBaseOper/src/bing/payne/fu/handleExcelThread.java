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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import bing.payne.fu.utils.LoadConfUtil;

public class handleExcelThread implements Runnable {
	public Workbook hssfWorkbook = null;
	private Connection conn;
	public PreparedStatement pstTar = null;
	public ReadExcel excel = null;
	public int numSheet;
	
	handleExcelThread(String TargetDBFile, String TargetSqlFile, ReadExcel excel, int numSheets)
	{
		this.excel = excel;
		this.numSheet = numSheets;
	    Properties prop;
		try {
			System.out.println("初始化目标库...");
			Database DB = new Database(TargetDBFile);
			conn = DB.getConn();
			conn.setAutoCommit(false);
			prop = LoadConfUtil.Load(TargetSqlFile);
			String tarSql = prop.getProperty("tarSql");
			pstTar = conn.prepareStatement(tarSql);
		    //pstTar = conn.prepareStatement(tarSql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			System.out.println("目标库初始化完成");
			
		} catch (Exception e) {
			e.printStackTrace();
		}		   			
	}	
	
	handleExcelThread(Connection TarConn, String sql, ReadExcel excel, int numSheets)
	{
		this.conn = TarConn;
		this.excel = excel;
		this.numSheet = numSheets;
		try {
			conn.setAutoCommit(false);
			pstTar = conn.prepareStatement(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void run()
	{
		//for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) 
        //{
		
		hssfWorkbook = excel.hssfWorkbook;
		//System.out.println("正在处理" + hssfWorkbook.getSheetName(numSheet) + "数据");
		String sheetName = hssfWorkbook.getSheetName(numSheet);
        Sheet hssfSheet =  hssfWorkbook.getSheetAt(numSheet);
        if (hssfSheet == null) 
        {
        	System.out.println("没找到" + sheetName);
        	return;
            //continue;
        }
        try
		{
	        // 循环行Row
	        for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) 
	        {
	        	
	            Row hssfRow = hssfSheet.getRow(rowNum);
	            System.out.println("正在导入"+ sheetName + "第" + rowNum + "行数据。。。");
	            if (hssfRow != null) {
	            	for(int i = 0; i < hssfSheet.getRow(0).getPhysicalNumberOfCells(); i++)
	            	{
	            		Cell cell = hssfRow.getCell(i);
	            		
	            		/*if(cell != null)
	            		{
	            			System.out.println(cell.getCellType());
		            		System.out.println(excel.getValue(cell));
	            		}*/
	            		
	            		if(cell == null)
	            		{
	            			//java.sql.Types
							pstTar.setNull(i+1, 0);
	
	            		}
	            		else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType())
	            		{
	            			if(DateUtil.isCellDateFormatted(cell))// 判断单元格是否属于日期格式 
	                    	{    		
	                    	    Date inputValue = cell.getDateCellValue();//java.util.Date类型 
	                    	    //String cellValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(inputValue);                   	    
								pstTar.setDate(i+1, new java.sql.Date(inputValue.getTime()));
	                    	}
	            			else
	            			{    
	            				//System.out.println(excel.getValue(cell));
								pstTar.setString(i+1, excel.getValue(cell));
	            			}
	            		}
	            		
	            		else
	            		{   
	            			//System.out.println(excel.getValue(cell));
							pstTar.setString(i+1, excel.getValue(cell));
	            		}            		                    
	            	}
	            }
	            //pstTar.execute();
				pstTar.addBatch(); 	                        
	         }
	         pstTar.executeBatch();
	                                    
	         conn.commit();     
	         //conn.close();
        }             
       catch(Exception e)
       {
    	e.printStackTrace();
       }
        
       
     //}   
	}

}
