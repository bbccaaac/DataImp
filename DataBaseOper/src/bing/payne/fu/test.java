package bing.payne.fu;

/****************************
 * 作者：payne fu
 * 修改时间：2016年5月3日
 * 功能说明：支持Excel导入数据至oracle数据库中；支持从mysql数据库导数据至oracle中，如果修改配置文件，也支持oracle导数据至mysql中，Excel导数据至mysql
 * 修改说明：增加从一个excel文件不同sheet并行导数据至oracle或mysql中
 ***************************/

import java.sql.*;
import java.text.DecimalFormat;
import java.util.Properties;

import bing.payne.fu.utils.LoadConfUtil;
public class test {

	public static void main(String[] args) throws Exception{
		/*Database TargetDB = new Database("conf/targetdb.properties");	
		Connection connTar = TargetDB.getConn();
		System.out.println("连接Oracle数据库成功");
		Database SourceDB = new Database("conf/sourcedb.properties");
		Connection connSrc = SourceDB.getConn();
		System.out.println("连接Mysql数据库成功");
		Properties prop = LoadConfUtil.Load("conf/sql.properties");
		String srcSql = prop.getProperty("srcSql");
		String tarSql = prop.getProperty("tarSql");
		PreparedStatement pstSrc = connSrc.prepareStatement(srcSql);
		PreparedStatement pstTar = connTar.prepareStatement(tarSql);
		//pst.setInt(1, 1);
		ResultSet rs = pstSrc.executeQuery();
		System.out.println("开始导入数据......");
		while (rs.next())
		{
			pstTar.setInt(1, rs.getInt(1));
			pstTar.setString(2, rs.getString(2));
			pstTar.execute();
			System.out.println(rs.getInt(1) + " " + rs.getString(2));
		}
		System.out.println("导入数据完成");*/
		
		//DataExtract srcData = new DataExtract("conf/sourcedb.properties", "conf/sql.properties");
		//DataTransLoad dtl = new DataTransLoad("conf/targetdb.properties", srcData);
		//dtl.DataTransferLoad();
		
		
		//ReadExcel excel = new ReadExcel("conf/test.xls");
		/*ReadExcel excel = new ReadExcel("E:\\工作\\各地区\\咸阳\\门诊结算单.xls");
		DataTransLoad dtl = new DataTransLoad("conf/targetdb.properties", "conf/sql.properties", excel);
        dtl.ExcelDataTransferLoad();*/
		
		ReadTxt rt = new ReadTxt("E:\\工作\\各地区\\南充\\ck15_nhsdml.unl");
		rt.openFile();
		Database TargetDB = new Database("conf/targetdb.properties");
		String sql = "insert into ck15_nhsdml values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		DataTransLoad dtl = new DataTransLoad(TargetDB.getConn(), sql, rt);
		dtl.TxtDataTransferLoad();
		
		
		//保留两位小数测试
		/*double d = 1.0553;
		DecimalFormat    df   = new DecimalFormat("######0.00");
		System.out.println(df.format(d));
		System.out.println(String.format("%.2f", d));*/
	}

}
