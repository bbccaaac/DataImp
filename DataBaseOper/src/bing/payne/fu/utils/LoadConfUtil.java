package bing.payne.fu.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class LoadConfUtil {
	
	public static Properties Load(String FileName) throws Exception
	{
		Properties pro = new Properties();
		   //获取当前目录路径
		   //System.out.println(System.getProperty("user.dir"));
	    FileInputStream in = new FileInputStream(FileName);
	    pro.load(in);
	    in.close();
	    return pro;		   
	}

}
