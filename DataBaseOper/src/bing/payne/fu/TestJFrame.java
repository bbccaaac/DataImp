package bing.payne.fu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class TestJFrame extends JFrame {

	private JPanel contentPane;
	private JTextField SrcFile;
	private JButton SrcScanFile;
	private JButton SrcOpenFile;
	
	private JTextField SrcUser;
	private JTextField SrcPwd;
	private JTextField SrcPort;
	private JTextField SrcHostAddr;
	private JLabel LabSrcDB;
	private JTextField SrcDB;
	private JButton ConnSrcDB;
	private JTextField DstHostAddr;
	private JTextField DstPort;
	private JTextField DstUser;
	private JTextField DstPwd;
	private JTextField DstDB;
	private JComboBox SrcComboBox;
	private JComboBox DstComboBox;
	private JPanel SrcPanel;
	private JPanel DstPanel;
	private JTextArea Log;
	private ReadExcel excel;
	private ReadTxt txt;
	
	private String mLog = "";
	private String filePath = "E:\\";

	private Connection SrcConn = null;
	private Connection DstConn = null;
	private JButton CloseSrcConn;
	private JButton CloseDstConn;
	private JButton button;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					TestJFrame frame = new TestJFrame();
					Toolkit kit=frame.getToolkit();
					Image image=kit.getImage("resource/biekex.png"); 
					frame.setIconImage(image);
					frame.setTitle("数据导入工具");
					
					
					//设置背景图片
					JLabel jlpic = new JLabel();
					ImageIcon icon = new ImageIcon("resource/bg1.jpg");  
			        icon.setImage(icon.getImage().getScaledInstance(icon.getIconWidth(),  
			                icon.getIconHeight(), Image.SCALE_DEFAULT));  
			        jlpic.setBounds(0, 0, 945, 668);  
			        jlpic.setHorizontalAlignment(0);  
			        jlpic.setIcon(icon);
			        //设置窗口大小
			        //frame.setSize(945, 668);  
			        frame.add(jlpic);  
					
					
			        frame.setResizable(false);
			        frame.setSizeAndCentralizeMe(945, 668);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestJFrame() {
		
		
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 945, 658);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		SrcComboBox = new JComboBox();
		SrcComboBox.setModel(new DefaultComboBoxModel(new String[] {"请选择源数据库", "mysql", "oracle", "excel", "文本文件"}));
		SrcComboBox.setToolTipText("");
		SrcComboBox.setBounds(39, 32, 120, 21);
		contentPane.add(SrcComboBox);
		
		SrcComboBox.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				comboBoxItemStateChanged(evt);
				}
			});
		
		SrcPanel = new JPanel();
		SrcPanel.setBounds(37, 63, 287, 229);
		contentPane.add(SrcPanel);
		SrcPanel.setLayout(null);
		showSrcDBUI();
		
		
		
		
		DstComboBox = new JComboBox();
		DstComboBox.setModel(new DefaultComboBoxModel(new String[] {"请选择目标数据库", "oracle", "mysql"}));
		DstComboBox.setToolTipText("");
		DstComboBox.setBounds(500, 32, 120, 21);
		contentPane.add(DstComboBox);
		
		DstComboBox.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				DstPanel.removeAll();
				showDstDBUI();
				DstPanel.repaint();
				}
			});
							
		DstPanel = new JPanel();
		DstPanel.setLayout(null);
		DstPanel.setBounds(498, 63, 312, 229);
		contentPane.add(DstPanel);		
		
		showDstDBUI();
		
		JScrollPane scrollPaneLog = new JScrollPane();
		scrollPaneLog.setBounds(28, 369, 805, 241);
		contentPane.add(scrollPaneLog);
		
		
		Log = new JTextArea();
		scrollPaneLog.setViewportView(Log);
		Log.setEditable(false);
		
		button = new JButton("开始导入数据");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(excel == null && SrcConn == null && txt == null)
				{
					mLog += "未发现源数据... \r\n";
					Log.setText(mLog);
					return;
				}
				if(DstConn == null)
				{
					mLog += "未连接目标库... \r\n";
					Log.setText(mLog);
					return;
				}
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						
						try {
							TranFrame frame = null;
							if(excel != null && SrcConn != null && txt != null)
							{
								mLog += "请确保只有一个数据源... \r\n";
								Log.setText(mLog);
								return;
							}
							else if(excel != null)
							{
								frame = new TranFrame(excel, DstConn);
							}
							else if(txt != null)
							{
								frame = new TranFrame(txt, DstConn);
							}
							else if(SrcConn != null)
							{
								frame = new TranFrame(SrcConn, DstConn);
							}
							Toolkit kit=frame.getToolkit();
							Image image=kit.getImage("resource/biekex.png"); 
							frame.setIconImage(image);
							//设置背景图片
							JLabel jlpic = new JLabel();
							ImageIcon icon = new ImageIcon("resource/bg1.jpg");  
					        icon.setImage(icon.getImage().getScaledInstance(icon.getIconWidth(),  
					                icon.getIconHeight(), Image.SCALE_DEFAULT));  
					        jlpic.setBounds(0, 0, 945, 668);  
					        jlpic.setHorizontalAlignment(0);  
					        jlpic.setIcon(icon);
					        //设置窗口大小
					        //frame.setSize(945, 668);  
					        frame.add(jlpic);  
							
							
					        frame.setResizable(false);
							frame.setTitle("数据导入工具");
							frame.setSizeAndCentralizeMe(945, 668);
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		button.setBounds(334, 140, 118, 23);
		contentPane.add(button);
	}

    private void showSrcDBUI()
    {
    	ConnSrcDB = new JButton("连接数据库");
    	ConnSrcDB.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			ConnSrcDataBase();
    		}
    	});
		ConnSrcDB.setBounds(10, 173, 112, 23);
		SrcPanel.add(ConnSrcDB);

		SrcPanel.setBackground(Color.white);
		
		SrcDB = new JTextField();
		SrcDB.setBounds(64, 132, 172, 21);
		SrcPanel.add(SrcDB);
		SrcDB.setColumns(10);
		
		LabSrcDB = new JLabel("数据库");
		LabSrcDB.setBounds(10, 135, 54, 15);
		SrcPanel.add(LabSrcDB);
		
		SrcPwd = new JTextField();
		SrcPwd.setBounds(64, 101, 172, 21);
		SrcPanel.add(SrcPwd);
		SrcPwd.setColumns(10);
		
		JLabel LabSrcPwd = new JLabel("密码");
		LabSrcPwd.setBounds(10, 104, 54, 21);
		SrcPanel.add(LabSrcPwd);
		
		SrcUser = new JTextField();
		SrcUser.setBounds(64, 69, 172, 21);
		SrcPanel.add(SrcUser);
		SrcUser.setColumns(10);
		
			
			
		JLabel LabSrcUser = new JLabel("用户名");
		LabSrcUser.setBounds(10, 72, 54, 21);
		SrcPanel.add(LabSrcUser);
		
		SrcPort = new JTextField();
		SrcPort.setBounds(64, 38, 172, 21);
		SrcPanel.add(SrcPort);
		SrcPort.setColumns(10);
		
		JLabel LabSrcPort = new JLabel("端口");
		LabSrcPort.setBounds(10, 38, 54, 21);
		SrcPanel.add(LabSrcPort);
		
		SrcHostAddr = new JTextField();
		SrcHostAddr.setBounds(64, 10, 172, 21);
		SrcPanel.add(SrcHostAddr);
		SrcHostAddr.setColumns(10);
		
		
		
		JLabel LabSrcHost = new JLabel("主机地址");
		LabSrcHost.setBounds(10, 13, 54, 15);
		SrcPanel.add(LabSrcHost);
		
		CloseSrcConn = new JButton("断开连接");
		CloseSrcConn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SrcConn.close();
					if(SrcConn.isClosed())
					{
						SrcConn = null;
						mLog += "断开源数据库连接成功...\r\n";
						Log.setText(mLog);
					}
				} catch (SQLException e1) {					
					mLog += "断开源数据库连接失败...\r\n";
		            StringWriter sw = new StringWriter(); 
		            e1.printStackTrace(new PrintWriter(sw, true)); 
		            String str = sw.toString();
		            mLog += str;
		            Log.setText(mLog);
				}
				
			}
		});
		CloseSrcConn.setBounds(143, 173, 93, 23);
		SrcPanel.add(CloseSrcConn);
    }
	
    private void showSrcFileUI(String fileType)
    {
    	SrcFile = new JTextField();
    	SrcFile.setBounds(10, 70, 192, 21);
		SrcPanel.add(SrcFile);
		SrcFile.setColumns(10);
		
		SrcScanFile = new JButton("浏览");
		SrcScanFile.setBounds(202, 70, 80, 23);
		SrcPanel.add(SrcScanFile);
		
		SrcScanFile.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			if(excel != null)
    			{
    				try {
						excel.hssfWorkbook.close();
						excel.hssfWorkbook = null;
						excel = null;
					} catch (IOException e) {
						
						e.printStackTrace();
					}
    				
    			}
    			else if(txt != null)
    			{
    				txt.close();
    			}
    			JFileChooser jfc=new JFileChooser(filePath);  
    	        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);  
    	        int retVal = jfc.showDialog(new JLabel(), "选择");
    	        if(JFileChooser.CANCEL_OPTION == retVal)
    	        {
    	        	return;
    	        }
    	        File file=jfc.getSelectedFile();
    	        filePath = file.getParent();
    	        /*if(file.isDirectory()){  
    	            System.out.println("文件夹:"+file.getAbsolutePath());  
    	        }else if(file.isFile()){  
    	            System.out.println("文件:"+file.getAbsolutePath());  
    	        }  */
    	        SrcFile.setText(file.getAbsolutePath());
    		}
    	});
		
		
		SrcOpenFile = new JButton("打开文件");
		SrcOpenFile.setBounds(172, 100, 135, 23);
		SrcPanel.add(SrcOpenFile);
		SrcOpenFile.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			if(fileType.equals("excel"))
    				openExcel();
    			else if(fileType.equals("文本文件"))
    				openTxt();
    			else
    			{   				
    				return;
    			}
    			
    		}
    	});
		
    }
    
    private void showDstDBUI()
    {
    	JButton ConnDstDB = new JButton("连接数据库");
    	ConnDstDB.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent arg0) {
    			ConnDstDataBase();
    		}
    	});
		ConnDstDB.setBounds(10, 175, 111, 23);
		DstPanel.add(ConnDstDB);
		
		DstPanel.setBackground(Color.white);
		
		DstDB = new JTextField();
		DstDB.setBounds(64, 132, 172, 21);
		DstPanel.add(DstDB);
		DstDB.setColumns(10);
		
		JLabel LabDstDB = new JLabel("数据库");
		LabDstDB.setBounds(10, 135, 54, 15);
		DstPanel.add(LabDstDB);
		
		DstPwd = new JTextField();
		DstPwd.setBounds(64, 101, 172, 21);
		DstPanel.add(DstPwd);
		DstPwd.setColumns(10);
		
		JLabel LabDstPwd = new JLabel("密码");
		LabDstPwd.setBounds(10, 104, 54, 21);
		DstPanel.add(LabDstPwd);
		
		DstUser = new JTextField();
		DstUser.setBounds(64, 69, 172, 21);
		DstPanel.add(DstUser);
		DstUser.setColumns(10);
		
		JLabel LabDstUser = new JLabel("用户名");
		LabDstUser.setBounds(10, 72, 54, 21);
		DstPanel.add(LabDstUser);
		
		DstPort = new JTextField();
		DstPort.setBounds(64, 38, 172, 21);
		DstPanel.add(DstPort);
		DstPort.setColumns(10);
		
		JLabel LabDstPort = new JLabel("端口");
		LabDstPort.setBounds(10, 38, 54, 21);
		DstPanel.add(LabDstPort);
		
		DstHostAddr = new JTextField();
		DstHostAddr.setBounds(64, 10, 172, 21);
		DstPanel.add(DstHostAddr);
		DstHostAddr.setColumns(10);
		
		JLabel LabDstHost = new JLabel("主机地址");
		LabDstHost.setBounds(10, 13, 54, 15);
		DstPanel.add(LabDstHost);
		
		CloseDstConn = new JButton("断开连接");
		CloseDstConn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					DstConn.close();
					if(DstConn.isClosed())
					{
						DstConn = null;
						mLog += "断开目标数据库连接成功...\r\n";
						Log.setText(mLog);
					}
				} catch (SQLException e1) {					
					mLog += "断开目标数据库连接失败...\r\n";
		            StringWriter sw = new StringWriter(); 
		            e1.printStackTrace(new PrintWriter(sw, true)); 
		            String str = sw.toString();
		            mLog += str;
		            Log.setText(mLog);
				}				
			}
		});
		CloseDstConn.setBounds(143, 175, 93, 23);
		DstPanel.add(CloseDstConn);
    }
    
    private void comboBoxItemStateChanged(java.awt.event.ItemEvent evt)
	{
		/*if (ItemEvent.SELECTED == evt.getStateChange()) 
		{ 
			//这个判断是选择只会得到一个结果，如果没有判断，会得到两个相同的值，从而获取不到所要选中的值。。
		}*/
		if(evt.getItem().equals("excel") || evt.getItem().equals("文本文件"))
		{
			SrcPanel.removeAll();
			showSrcFileUI((String)evt.getItem());
			SrcPanel.repaint();
		}
		else
		{
			SrcPanel.removeAll();
			showSrcDBUI();
			SrcPanel.repaint();
		}
		/*String Str_A = SrcComboBox.getSelectedItem().toString();*/
    }

    private void ConnSrcDataBase()
    {
    	if(SrcConn != null)
    	{
    		mLog += "源数据库已连接成功，不需要再次连接...\r\n";
    		Log.setText(mLog);
    		return ;
    	}
    	String host = SrcHostAddr.getText();
    	String port = SrcPort.getText();
    	String username = SrcUser.getText();
    	String password = SrcPwd.getText();
    	String dbname = SrcDB.getText();
    	String url = null;
    	String driver = null;
    	if(SrcComboBox.getSelectedItem().toString().equals("mysql"))
    	{
    		url = "jdbc:mysql://" + host + ":" + port + "/" + dbname + "?&useSSL=true&useUnicode=true&characterEncoding=gbk";
        	driver = "com.mysql.jdbc.Driver";
    	}else if(SrcComboBox.getSelectedItem().toString().equals("oracle"))
    	{
    		url = "jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS = (PROTOCOL = TCP)(HOST = " + host + ")(PORT = " + port + ")))(CONNECT_DATA =(SERVICE_NAME = " + dbname + ")))";
    		driver = "oracle.jdbc.driver.OracleDriver";
    	}else
    	{
    		mLog += "未选中数据库，连接源数据库失败...\r\n";
    		Log.setText(mLog);
    		return ;
    	}
    	Database db = new Database(username, password, url, driver);
		try {
			SrcConn = db.getConn();
		} catch (Exception e) {
			mLog += "获取源数据库连接失败...\r\n";
            StringWriter sw = new StringWriter(); 
            e.printStackTrace(new PrintWriter(sw, true)); 
            String str = sw.toString();
            mLog += str;
            Log.setText(mLog);
		}
    	if(SrcConn != null)
    	{
    		mLog += "连接源数据库成功...\r\n";
    		Log.setText(mLog);
    	}
    }

    
    private void ConnDstDataBase()
    {
    	if(DstConn != null)
    	{
    		mLog += "目标数据库已连接成功，不需要再次连接...\r\n";
    		Log.setText(mLog);
    		return ;
    	}
    	String host = DstHostAddr.getText();
    	String port = DstPort.getText();
    	String username = DstUser.getText();
    	String password = DstPwd.getText();
    	String dbname = DstDB.getText();
    	String url = null;
    	String driver = null;
    	if(DstComboBox.getSelectedItem().toString().equals("mysql"))
    	{
    		url = "jdbc:mysql://" + host + ":" + port + "/" + dbname + "?&useSSL=true";
        	driver = "com.mysql.jdbc.Driver";
    	}else if(DstComboBox.getSelectedItem().toString().equals("oracle"))
    	{
    		url = "jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS = (PROTOCOL = TCP)(HOST = " + host + ")(PORT = " + port + ")))(CONNECT_DATA =(SERVICE_NAME = " + dbname + ")))";
    		driver = "oracle.jdbc.driver.OracleDriver";
    	}else
    	{
    		mLog += "未选中数据库，连接目标数据库失败...\r\n";
    		Log.setText(mLog);
    		return ;
    	}
    	Database db = new Database(username, password, url, driver);
		try {
			DstConn = db.getConn();
		} catch (Exception e) {
			mLog += "获取目标数据库连接失败...\r\n";
            StringWriter sw = new StringWriter(); 
            e.printStackTrace(new PrintWriter(sw, true)); 
            String str = sw.toString();
            mLog += str;
            Log.setText(mLog);
		}
    	if(DstConn != null)
    	{
    		
    		mLog += "连接目标数据库成功...\r\n";
    		Log.setText(mLog);
    	}
    }

    private void openExcel()
    {
    	String path = SrcFile.getText();
    	excel = new ReadExcel(path);
    	if(excel != null)
    	{
    		mLog += "打开" + path + "文件成功... \r\n";
    		Log.setText(mLog);
    	}
    }
  
    private void openTxt()
    {
    	String path = SrcFile.getText();
    	txt = new ReadTxt(path);
    	txt.openFile();
    	if(txt != null)
    	{
    		mLog += "打开" + path + "文件成功... \r\n";
    		Log.setText(mLog);
    	}
    }
    
    
    //设置程序大小并定位程序在屏幕正中
    private void setSizeAndCentralizeMe(int width, int height) {
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      this.setSize(width, height);
      this.setLocation(screenSize.width / 2 - width / 2, screenSize.height
          / 2 - height / 2);
    }

}
