package bing.payne.fu;

/****************************
 * ���ߣ�payne fu
 * �޸�ʱ�䣺2016��5��3��
 * ����˵����֧��Excel����������oracle���ݿ��У�֧�ִ�mysql���ݿ⵼������oracle�У�����޸������ļ���Ҳ֧��oracle��������mysql�У�Excel��������mysql
 * �޸�˵�������Ӵ�һ��excel�ļ���ͬsheet���е�������oracle��mysql��
 ***************************/

import java.sql.*;
import java.text.DecimalFormat;
import java.util.Properties;

import bing.payne.fu.utils.LoadConfUtil;
public class test {

	public static void tt(String[] args) throws Exception{
		/*Database TargetDB = new Database("conf/targetdb.properties");	
		Connection connTar = TargetDB.getConn();
		System.out.println("����Oracle���ݿ�ɹ�");
		Database SourceDB = new Database("conf/sourcedb.properties");
		Connection connSrc = SourceDB.getConn();
		System.out.println("����Mysql���ݿ�ɹ�");
		Properties prop = LoadConfUtil.Load("conf/sql.properties");
		String srcSql = prop.getProperty("srcSql");
		String tarSql = prop.getProperty("tarSql");
		PreparedStatement pstSrc = connSrc.prepareStatement(srcSql);
		PreparedStatement pstTar = connTar.prepareStatement(tarSql);
		//pst.setInt(1, 1);
		ResultSet rs = pstSrc.executeQuery();
		System.out.println("��ʼ��������......");
		while (rs.next())
		{
			pstTar.setInt(1, rs.getInt(1));
			pstTar.setString(2, rs.getString(2));
			pstTar.execute();
			System.out.println(rs.getInt(1) + " " + rs.getString(2));
		}
		System.out.println("�����������");*/
		
		//DataExtract srcData = new DataExtract("conf/sourcedb.properties", "conf/sql.properties");
		//DataTransLoad dtl = new DataTransLoad("conf/targetdb.properties", srcData);
		//dtl.DataTransferLoad();
		
		
		//ReadExcel excel = new ReadExcel("conf/test.xls");
		ReadExcel excel = new ReadExcel("E:\\����\\������\\����\\������㵥.xls");
		DataTransLoad dtl = new DataTransLoad("conf/targetdb.properties", "conf/sql.properties", excel);
        dtl.ExcelDataTransferLoad();
		
		
		//������λС������
		/*double d = 1.0553;
		DecimalFormat    df   = new DecimalFormat("######0.00");
		System.out.println(df.format(d));
		System.out.println(String.format("%.2f", d));*/
	}

}
