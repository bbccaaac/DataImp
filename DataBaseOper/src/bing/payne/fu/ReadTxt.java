package bing.payne.fu;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadTxt {
	private String fileName;
	InputStream in = null;
	InputStreamReader inr = null;
	BufferedReader br = null;
	public ReadTxt(String fileName)
	{
	   this.fileName = fileName;	
	}
	
	void openFile()
	{
		byte[] buffer = new byte[1024];
		File file = new File(fileName);
		try {
			in = new FileInputStream(file);
			inr=new InputStreamReader(in,"gbk");
			br = new BufferedReader(inr);						
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
	
	public String[] getData(String FiledSplit)
	{
		try {
			String chunk = br.readLine();
			//String[]  strs=chunk.split("\\^");
			String[]  strs=chunk.split(FiledSplit);
			/*BufferedInputStream bin = new BufferedInputStream(in);
			int len;
			len = bin.read(buffer);
			String chunk = new String(buffer, 0, len);*/
			System.out.println(chunk);
			//System.out.println(strs[0]);
			System.out.println("³¤¶ÈÎª" + strs.length);
			/*for(String str : strs)
			{
				System.out.println(str);
			}*/						
			return strs;
			
		} catch (IOException e) {			
			e.printStackTrace();			
			return null;
		}
		
	}
	
	public void close()
	{
		try {
			br.close();		
			inr.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
