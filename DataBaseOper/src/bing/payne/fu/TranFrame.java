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

public class TranFrame extends JFrame {

	private JPanel contentPane;
    private JPanel panelDst;
    private JComboBox ShowDstTable;
    private JPanel panelSrc;
    private JComboBox showSrcTable;
    private JPanel panelExcel;
    private JComboBox showExcelSheetName;
    
    private JScrollPane scrollPane;
    private JTextArea Log;
    private String mLog = "";
    
    Map<String, Integer> sheetData = null;
	
    private ReadExcel excel;	
	private Connection SrcConn = null;
	private Connection DstConn = null;
	
	
	private JFrame mainFrame;
	   private JLabel headerLabel;
	   private JLabel statusLabel;
	   private JPanel controlPanel;
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
		
		if(SrcConn == null)
		{
			showSrcExcelUI();
		}
		else
		{
			showSrcTableUI();
		}
		
		showDstTableUI();
		
		JButton imp = new JButton("��ʼ��������");
		imp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int output = JOptionPane.showConfirmDialog(mainFrame
			               , "ȷ�����룿"
			               ,"��������"
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
		imp.setBounds(379, 48, 110, 23);
		contentPane.add(imp);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 290, 905, 326);
		contentPane.add(scrollPane);
		
		Log = new JTextArea();
		scrollPane.setViewportView(Log);
				
	}
	
	private void showDstTableUI()
	{
		panelDst = new JPanel();
		panelDst.setBounds(514, 43, 327, 45);
		contentPane.add(panelDst);
		panelDst.setLayout(null);
		
		JLabel label = new JLabel("��ѡ����Ҫ����ı�");
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
		
		JLabel label_1 = new JLabel("��ѡ����Ҫ�����ı�");
		label_1.setBounds(10, 10, 128, 15);
		panelSrc.add(label_1);
		
		showSrcTable = new JComboBox();
		showSrcTable.setBounds(128, 7, 177, 21);
		
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
		showSrcTable.setModel(new DefaultComboBoxModel(tableName));
		
		panelSrc.add(showSrcTable);
	}

	private void showSrcExcelUI()
	{
		panelExcel = new JPanel();
		panelExcel.setBounds(10, 41, 315, 45);
		contentPane.add(panelExcel);
		panelExcel.setLayout(null);
		
		JLabel lblsheet = new JLabel("��ѡ�����sheet��");
		lblsheet.setBounds(0, 10, 138, 15);
		panelExcel.add(lblsheet);
		
		sheetData = new HashMap<>();
		
		showExcelSheetName = new JComboBox();
		showExcelSheetName.setBounds(137, 7, 168, 21);
		Workbook hssfWorkbook = excel.hssfWorkbook;
		int numSheets = hssfWorkbook.getNumberOfSheets();
		String[] sheetName = new String[numSheets+1];
		sheetName[0] = "ȫ������";
		for(int i = 0; i < numSheets; i++)
		{
			sheetName[i+1] = hssfWorkbook.getSheetName(i);
			sheetData.put(sheetName[i+1], i);
		}
		showExcelSheetName.setModel(new DefaultComboBoxModel(sheetName));
		panelExcel.add(showExcelSheetName);
	}

	//���ó����С����λ��������Ļ����
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
				mLog += "��ʼ����" + sheetName + "������" + DstTableName + "����\r\n";
				long time1=System.currentTimeMillis();
				if(sheetName.equals("ȫ������"))
				{
					load.ExcelDataTransferLoadUI();
				}
				else
				{
					int numSheet = sheetData.get(sheetName);
					load.ExcelDataTransferLoadUI(numSheet);
				}																
				long time2=System.currentTimeMillis();		        
				mLog += "�������, ��ʱ��" + (time2 - time1) +"ms\r\n";
				
			}
			else
			{
				String SrcTableName = showSrcTable.getSelectedItem().toString();
				String sql = "select * from " + SrcTableName;
				DataExtract src = new DataExtract(SrcConn, sql);
				DataTransLoad load = new DataTransLoad(DstConn, InsertSql, src);
				mLog += "��ʼ����" + SrcTableName + "��������" + DstTableName + "����\r\n";
				long time1=System.currentTimeMillis();
				load.DataTransferLoad();
				
				long time2=System.currentTimeMillis();		        
				mLog += "�������, ��ʱ��" + (time2 - time1) +"ms\r\n";
			}
			Log.setText(mLog);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
