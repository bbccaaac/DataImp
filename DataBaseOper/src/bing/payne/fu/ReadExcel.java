package bing.payne.fu;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ReadExcel {
	public Workbook hssfWorkbook;

	public ReadExcel(String ExcelName)
	{
		try
		{
			InputStream is = new FileInputStream(ExcelName);
			if(ExcelName.matches("^.+\\.(?i)(xls)$"))
			{
				hssfWorkbook = new HSSFWorkbook(is);
			}
			else if(ExcelName.matches("^.+\\.(?i)(xlsx)$"))
			{
				hssfWorkbook = new XSSFWorkbook(ExcelName);
			}
	        
		}
		catch(Exception e)
		{												
			e.printStackTrace();			
		}
	}
	   

	public String getValue(Cell cell) 
	{
		String cellValue = "";      
	    switch (cell.getCellType()) {      
	    case Cell.CELL_TYPE_STRING://字符串类型  
	        cellValue = cell.getStringCellValue();
	        if(cellValue.trim().equals("")||cellValue.trim().length()<=0)      
	            cellValue=" ";      
	        break;      
	    case Cell.CELL_TYPE_NUMERIC: //数值类型 
	    	double doubleVal = cell.getNumericCellValue();
	    	long longVal = Math.round(cell.getNumericCellValue());
	    	Object inputValue;
	        if(Double.parseDouble(longVal + ".0") == doubleVal)  
	            inputValue = longVal;  
	        else  
	            inputValue = doubleVal;
	    	cellValue = String.valueOf(inputValue);      
	        break;      
	    case Cell.CELL_TYPE_FORMULA: //公式  
	        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);      
	        cellValue = String.valueOf(cell.getNumericCellValue());      
	        break;      
	    case Cell.CELL_TYPE_BLANK:      
	    	
	        cellValue=null;      
	        break;      
	    case Cell.CELL_TYPE_BOOLEAN:      
	        break;      
	    case Cell.CELL_TYPE_ERROR:   
	    	
	        break;      
	    default:   
	    	
	        break;      
	    }      
	    return cellValue;   
	}

}
