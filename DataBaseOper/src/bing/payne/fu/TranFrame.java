package bing.payne.fu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class TranFrame extends JFrame {

	private JPanel contentPane;
    private JPanel panelDst;
    private JComboBox ShowDstTable;
    private JPanel panelSrc;
    private JComboBox showSrcTable;
    private JPanel panelExcel;
    private JComboBox showExcelSheetName;
    
    JPanel TxtPanel;
    
    
    private JScrollPane scrollPane;
    private JTextArea Log;
    private String mLog = "";
    
    Map<String, Integer> sheetData = null;
	
    private ReadExcel excel;	
	private Connection SrcConn = null;
	private Connection DstConn = null;
	ReadTxt txt;
	
	private JFrame mainFrame;
	   private JLabel headerLabel;
	   private JLabel statusLabel;
	   private JPanel controlPanel;
	   private JTextField textField;
	   private JLabel label_2;
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TranFrame frame = new TranFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public TranFrame()
	{
		showUI();
	}
	
	public TranFrame(ReadExcel excel, Connection conn) {
		this.excel = excel;
		this.DstConn = conn;
		showUI();
	}
	
	public TranFrame(ReadTxt txt, Connection conn) {
		this.txt = txt;
		this.DstConn = conn;
		showUI();
	}
	
	public TranFrame(Connection sconn, Connection dconn) {
		this.SrcConn = sconn;
		this.DstConn = dconn;
		showUI();
	}
	
	private void showUI()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 941, 686);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		if(excel != null)
		{
			showSrcExcelUI();
		}
		else if(txt != null)
		{
			showSrcTxtUI();
		}
		else
		{
			showSrcTableUI();
		}
		
		showDstTableUI();
		
		JButton imp = new JButton("开始导入数据");
		imp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int output = JOptionPane.showConfirmDialog(mainFrame
			               , "确定导入？"
			               ,"导入数据"
			               ,JOptionPane.YES_NO_OPTION,
			               JOptionPane.INFORMATION_MESSAGE);

			            if(output == JOptionPane.YES_OPTION){
			            	impData();
			            }else if(output == JOptionPane.NO_OPTION){
			               return;
			            }
				//impData();
			}
		});
		imp.setBounds(400, 53, 122, 23);
		contentPane.add(imp);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 290, 905, 326);
		contentPane.add(scrollPane);
		
		Log = new JTextArea();
		scrollPane.setViewportView(Log);
		
		
				
	}
	
	private void showSrcTxtUI()
	{
		TxtPanel = new JPanel();
		TxtPanel.setBounds(10, 41, 368, 47);
		contentPane.add(TxtPanel);
		TxtPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("请输入文件中字段分隔符");
		lblNewLabel.setBounds(10, 10, 150, 15);
		TxtPanel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(170, 7, 66, 21);
		TxtPanel.add(textField);
		textField.setColumns(10);
		
		label_2 = new JLabel("\u6CE8\u610F\u5143\u5B57\u7B26'\\'\u8F6C\u4E49");
		label_2.setBounds(246, 10, 112, 15);
		TxtPanel.add(label_2);
	}
	
	private void showDstTableUI()
	{
		panelDst = new JPanel();
		panelDst.setBounds(546, 43, 327, 45);
		contentPane.add(panelDst);
		panelDst.setLayout(null);
		
		JLabel label = new JLabel("请选择需要导入的表");
		label.setBounds(10, 10, 139, 15);
		panelDst.add(label);
		
		ShowDstTable = new JComboBox();
		ShowDstTable.setBounds(135, 7, 182, 21);
		
		
		List<String> list = new ArrayList<String>();
		DatabaseMetaData metaData;
		try {
			metaData = DstConn.getMetaData();
			ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"});
			while(rs.next()) {
			   list.add(rs.getString("TABLE_NAME"));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] tableName = new String[list.size()];
		list.toArray(tableName);
		ShowDstTable.setModel(new DefaultComboBoxModel(tableName));
		panelDst.add(ShowDstTable);
		
		
		
		
	}
	
	private void showSrcTableUI()
	{
		panelSrc = new JPanel();
		panelSrc.setBounds(20, 41, 315, 45);
		contentPane.add(panelSrc);
		panelSrc.setLayout(null);
		
		JLabel label_1 = new JLabel("请选择需要导出的表");
		label_1.setBounds(10, 10, 128, 15);
		panelSrc.add(label_1);
		
		showSrcTable = new JComboBox();
		showSrcTable.setBounds(128, 7, 177, 21);
		
		List<String> list = new ArrayList<String>();
		DatabaseMetaData metaData;
		try {
			metaData = SrcConn.getMetaData();
			ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"});
			while(rs.next()) {
			   list.add(rs.getString("TABLE_NAME"));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] tableName = new String[list.size()];
		list.toArray(tableName);
		showSrcTable.setModel(new DefaultComboBoxModel(tableName));
		
		panelSrc.add(showSrcTable);
	}

	private void showSrcExcelUI()
	{
		panelExcel = new JPanel();
		panelExcel.setBounds(10, 41, 315, 45);
		contentPane.add(panelExcel);
		panelExcel.setLayout(null);
		
		JLabel lblsheet = new JLabel("请选择导入的sheet名");
		lblsheet.setBounds(0, 10, 138, 15);
		panelExcel.add(lblsheet);
		
		sheetData = new HashMap<>();
		
		showExcelSheetName = new JComboBox();
		showExcelSheetName.setBounds(137, 7, 168, 21);
		Workbook hssfWorkbook = excel.hssfWorkbook;
		int numSheets = hssfWorkbook.getNumberOfSheets();
		String[] sheetName = new String[numSheets+1];
		sheetName[0] = "全部导入";
		for(int i = 0; i < numSheets; i++)
		{
			sheetName[i+1] = hssfWorkbook.getSheetName(i);
			sheetData.put(sheetName[i+1], i);
		}
		showExcelSheetName.setModel(new DefaultComboBoxModel(sheetName));
		panelExcel.add(showExcelSheetName);
	}

	//设置程序大小并定位程序在屏幕正中
    protected void setSizeAndCentralizeMe(int width, int height) {
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      this.setSize(width, height);
      this.setLocation(screenSize.width / 2 - width / 2, screenSize.height
          / 2 - height / 2);
    }
    
	private void impData()
	{
		String DstTableName = ShowDstTable.getSelectedItem().toString();
		String SelectSql = "select * from " + DstTableName;
		String InsertSql = null;
		String str = "insert into " + DstTableName + " values(";
		try {
			PreparedStatement pst = DstConn.prepareStatement(SelectSql);
			ResultSet rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCnt = rsmd.getColumnCount();
			
			for(int i = 0; i < colCnt; i++)
			{
				str += "?,";
			}
			InsertSql = str.substring(0,str.length()-1) + ")";
			if(excel != null)
			{
				DataTransLoad load = new DataTransLoad(DstConn, InsertSql, excel);
				String sheetName = showExcelSheetName.getSelectedItem().toString();
				mLog += "开始导入" + sheetName + "数据至" + DstTableName + "表中\r\n";
				long time1=System.currentTimeMillis();
				if(sheetName.equals("全部导入"))
				{
					load.ExcelDataTransferLoadUI();
				}
				else
				{
					int numSheet = sheetData.get(sheetName);
					load.ExcelDataTransferLoadUI(numSheet);
				}																
				long time2=System.currentTimeMillis();		        
				mLog += "导入完成, 用时：" + (time2 - time1) +"ms\r\n";
				
			}
			else if(txt != null)
			{
				DataTransLoad load = new DataTransLoad(DstConn, InsertSql, txt);
				mLog += "开始导入文本文件数据至" + DstTableName + "表中\r\n";
				long time1=System.currentTimeMillis();
				String FieldSplit = textField.getText();
				load.TxtDataTransferLoad(FieldSplit);
				long time2=System.currentTimeMillis();		        
				mLog += "导入完成, 用时：" + (time2 - time1) +"ms\r\n";
			}
			else
			{
				String SrcTableName = showSrcTable.getSelectedItem().toString();
				String sql = "select * from " + SrcTableName;
				DataExtract src = new DataExtract(SrcConn, sql);
				DataTransLoad load = new DataTransLoad(DstConn, InsertSql, src);
				mLog += "开始导入" + SrcTableName + "表数据至" + DstTableName + "表中\r\n";
				long time1=System.currentTimeMillis();
				load.DataTransferLoad();
				
				long time2=System.currentTimeMillis();		        
				mLog += "导入完成, 用时：" + (time2 - time1) +"ms\r\n";
			}
			Log.setText(mLog);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
