package bing.payne.fu.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class LoadConfUtil {
	
	public static Properties Load(String FileName) throws Exception
	{
		Properties pro = new Properties();
		   //��ȡ��ǰĿ¼·��
		   //System.out.println(System.getProperty("user.dir"));
	    FileInputStream in = new FileInputStream(FileName);
	    pro.load(in);
	    in.close();
	    return pro;		   
	}

}
