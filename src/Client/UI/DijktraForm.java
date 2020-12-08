package Client.UI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Scrollbar;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import Client.Controler.Client;
import Client.Controler.InitData;
import Client.DAO.Edge;
import Client.DAO.Vertex;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class DijktraForm extends JInternalFrame  {

	// component
	private JPanel contentPane;
	private JPanel panel_1;
	private JComboBox cbVerSource = new JComboBox();
	private JComboBox cbVerDestination = new JComboBox();
	private JButton btnFindPath = new JButton("Tìm đường");
	private JButton btnImportData = new JButton("Import data");
	private JTable tableData = new JTable();
	private JRadioButton rbtnDdirectional = new JRadioButton("Đồ thị có hướng");
	private JRadioButton rbtnScalar = new JRadioButton("Đồ thị vô hướng");
	private JTextArea txtaResult = new JTextArea();
	private JTextField txtCP  = new JTextField();
	private JFileChooser chooser;
	ButtonGroup group = new ButtonGroup();
	DefaultComboBoxModel<Vertex> verSource = new DefaultComboBoxModel<Vertex>();
	DefaultComboBoxModel<Vertex> verDestinaion = new DefaultComboBoxModel<Vertex>();

	// attribute
	private boolean flag =true;//data hợp lệ
	// data của vertexs và edges
	private InitData data = new InitData();
	//vẽ 
	private Panneau paneGraph;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client.connect();
					DijktraForm frame = new DijktraForm();
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
	String[] columns = new String[] { "Đỉnh đầu", "Đỉnh cuối", "Trọng số" };
	DefaultTableModel model = new DefaultTableModel(null, columns);
	

	//dữ liệu của file text 
	//so dinh
	//[s] [d] [w]: edge 
	private void createTableData() {
		if(flag==false)
		{
			return;
		}
		model.setRowCount(0);
		for (Edge e : data.getEdges()) {
			String id1 = e.getSource().getId();
			String id2 = e.getDestination().getId();
			String w = e.getWeight() + "";
			Object[] items = new Object[] { id1, id2, w };
			model.addRow(items);
		}
		tableData.setModel(model);
	}

	private void loadComboBoxVertex() {
		verSource.removeAllElements();
		verDestinaion.removeAllElements();
		for (Vertex v : data.getVertexs()) {
			verSource.addElement(v);
			verDestinaion.addElement(v);
		}
	}

	public void newUI() {

		loadComboBoxVertex();
		cbVerSource.setModel(verSource);
		cbVerDestination.setModel(verDestinaion);

		group.add(rbtnDdirectional);
		group.add(rbtnScalar);
		
		paneGraph.setBorder(BorderFactory.createLineBorder(Color.black));
		rbtnDdirectional.setEnabled(true);
		rbtnScalar.setEnabled(true);
		btnFindPath.setEnabled(true);
		btnImportData.setEnabled(true);
	}

	public void init(File f) {
		paneGraph = new Panneau();
		if(data.readFile(f)==true) {
			data.readFile(f);
			paneGraph.initData(data.getVertexs(), data.getEdges());
		}
		else {
			flag=false;
		}
		
		
		if (Client.isConnected()) {
			//Client.init(data, true);
			if(flag==true)//du liệu file đúng
			{
				try {
					Client.resetData(data, true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Không khởi tạo dữ liệu được");
				}
				newUI();
			}
			else {
				rbtnDdirectional.setEnabled(false);
				rbtnScalar.setEnabled(false);
				btnFindPath.setEnabled(false);
				btnImportData.setEnabled(false);
			}
			
		} else {
			rbtnDdirectional.setEnabled(false);
			rbtnScalar.setEnabled(false);
			btnFindPath.setEnabled(false);
			btnImportData.setEnabled(false);
			JOptionPane.showMessageDialog(null, "Không kết nối được với server");
		}
		
	}

	public DijktraForm() {
		File f = new File("src//Client//Controler//data.txt");
		init(f);
		setBounds(100, 100, 1433, 744);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.getContentPane().setSize(1000, 500);

		panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		if (flag==true)
			panel_1=paneGraph;
		panel_1.setBounds(202, 70, 798, 531);
		
		contentPane.add(panel_1);
		
		

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_2.setBounds(1010, 70, 309, 531);
		contentPane.add(panel_2);
		panel_2.setLayout(null);

		JLabel lblNewLabel = new JLabel("Điểm đầu");
		lblNewLabel.setBounds(26, 26, 76, 14);
		panel_2.add(lblNewLabel);

		JLabel lblimCui = new JLabel("Điểm cuối");
		lblimCui.setBounds(26, 67, 76, 14);
		panel_2.add(lblimCui);

		cbVerSource.setBounds(112, 22, 71, 22);
		panel_2.add(cbVerSource);

		cbVerDestination.setBounds(112, 63, 71, 22);
		panel_2.add(cbVerDestination);
		Object[] columns = { "1", "2", "3" };
		DefaultTableModel modeltable = new DefaultTableModel();
		modeltable.setColumnIdentifiers(columns);

		
		btnFindPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findpath();
			}
		});
		btnFindPath.setBounds(26, 105, 132, 23);
		panel_2.add(btnFindPath);
		txtaResult.setFont(new Font("Monospaced", Font.PLAIN, 12));

		txtaResult.setEditable(false);
		txtaResult.setBounds(26, 209, 259, 187);
		Border border = BorderFactory.createLineBorder(Color.black);
		txtaResult.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		panel_2.add(txtaResult);

		JLabel lblNewLabel_2 = new JLabel("Kết quả đường đi");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_2.setBounds(26, 168, 197, 30);
		panel_2.add(lblNewLabel_2);

		JButton btnExportImage = new JButton("Export Image");
		btnExportImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveImage();
			}
		});
		btnExportImage.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExportImage.setBounds(26, 490, 142, 30);
		panel_2.add(btnExportImage);
		
		JLabel lbChiPhi = new JLabel("Chi phí");
		lbChiPhi.setFont(new Font("Tahoma", Font.BOLD, 15));
		lbChiPhi.setBounds(26, 422, 76, 30);
		panel_2.add(lbChiPhi);
		
		
		txtCP.setEditable(false);
		txtCP.setBounds(97, 429, 86, 20);
		panel_2.add(txtCP);
		txtCP.setColumns(10);

		tableData = new JTable();
		contentPane.add(tableData, BorderLayout.CENTER);
		
		tableData.setBackground(Color.white);
		tableData.setForeground(Color.black);
		tableData.setSelectionForeground(Color.white);
		tableData.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tableData.setRowHeight(30);
		tableData.setAutoCreateRowSorter(true);
		tableData.setDefaultEditor(Object.class, null);
		JScrollPane pane = new JScrollPane(tableData);
		pane.setForeground(Color.red);
		pane.setBackground(Color.white);
		pane.setForeground(Color.red);
		pane.setBounds(10, 156, 182, 356);
		contentPane.add(pane);

		JLabel lblNewLabel_1 = new JLabel("Dữ liệu đầu vào");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1.setBounds(10, 60, 170, 26);
		contentPane.add(lblNewLabel_1);
		rbtnDdirectional.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (paneGraph.isDirectional == false) {// nếu đồ thị hiện tại là vô hướng cần đổi thành có hướng
					paneGraph.isDirectional = true;
					paneGraph.isPath.clear();
					paneGraph.setPath(null);
					try {
						Client.resetData(data, paneGraph.isDirectional);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					paneGraph.repaint();
				}
			}
		});

		rbtnDdirectional.setSelected(true);
		rbtnDdirectional.setBounds(6, 520, 159, 23);
		contentPane.add(rbtnDdirectional);
		rbtnScalar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (paneGraph.isDirectional == true) {// nếu đây là đồ thị có hướng cần đổi thành vô hướng
					paneGraph.isDirectional = false;
					paneGraph.isPath.clear();
					paneGraph.setPath(null);
					try {
						Client.resetData(data, paneGraph.isDirectional);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					paneGraph.repaint();
				}
			}
		});

		rbtnScalar.setBounds(6, 564, 159, 23);
		contentPane.add(rbtnScalar);

		
		btnImportData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					openfile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnImportData.setBounds(10, 111, 139, 23);
		contentPane.add(btnImportData);
		
//		JScrollPane scrollPane = new JScrollPane();
//		scrollPane.setBounds(243, 70, 740, 530);
//		scrollPane.add(paneGraph);
//		contentPane.add(scrollPane);

		Icon icon = new ImageIcon("src//Image//close_window_48px.png");
		createTableData();
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//setUndecorated(true);
		
	}

	/// function
	private void openfile() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter files = new FileNameExtensionFilter("file", "txt");
		fileChooser.setFileFilter(files);
		fileChooser.setMultiSelectionEnabled(false);

		int x = fileChooser.showDialog((this), "Chon file");
		if (x == JFileChooser.APPROVE_OPTION) {
			File f = fileChooser.getSelectedFile();
			if (data.readFile(f) == true) {
				paneGraph.initData(data.getVertexs(), data.getEdges());
				paneGraph.isPath.clear();
				paneGraph.setPath(null);
				if(rbtnDdirectional.isSelected()) {
					paneGraph.isDirectional=true;
				}
				else {
					paneGraph.isDirectional=false;
				}
				Client.resetData(data, paneGraph.isDirectional);
				paneGraph.repaint();
				createTableData();
				newUI();
				fileChooser.getSelectedFile().getPath();
			}
			

		}
	}

	private boolean checkVertexInGraph(int s) {
		
		for(Edge e: data.getEdges()) {
			if(e.getSource().getId().equals(s+"")||e.getDestination().getId().equals(s+"")){
				return true;
			}
		}
		return false;
	}
	private void findpath() {
		// tìm đường đi tra ve biến temp
		LinkedList<String> temp = new LinkedList<String>();
		// lấy node source và destination
		int source = Integer.valueOf(((Vertex) cbVerSource.getSelectedItem()).getId());
		int destiantion = Integer.valueOf(((Vertex) cbVerDestination.getSelectedItem()).getId());
		if(checkVertexInGraph(source)==false)
		{
			JOptionPane.showMessageDialog(null, "Điểm nguồn không tồn tại trong đồ thị");
			return;
		}
		if(checkVertexInGraph(destiantion)==false)
		{
			JOptionPane.showMessageDialog(null, "Điểm cuối không tồn tại trong đồ thị");
			return;
		}
		
		// kiểm tra node
		if (source != destiantion) {
			try {
				temp = Client.findPath(source, destiantion);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "Nguồn và đích đến không được trùng nhau");
			return;
		}
		System.out.println("Temp: " + temp.size());
		// neu ket qua temp.size la 0 thì không tìm thấy đường đi
		if (temp.size() == 0) {
			JOptionPane.showMessageDialog(null, "Không tìm được đường đi");
		}

		// ghi ket qua vao textarea result
		String result = "";
		

		int d = 0;// đếm để xuống dòng cho kết quả
		for (String i : temp) {
			d++;
			result += "Node_" + i + " ";
			if (d == 3) {
				result += "\n";
				d = 0;
			}

		}
		result = result.trim();
		result = result.replace(" ", " => ");

		txtaResult.setText(result);
		txtCP.setText(Client.getChiphi()+"");

		paneGraph.setPath(temp);
		paneGraph.repaint();
		System.out.println("find path " + paneGraph.getPath());

	}

	public void saveImage() {
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Lưu vào");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
			System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
			Container container = paneGraph;
			BufferedImage im = new BufferedImage(container.getWidth(), container.getHeight(), BufferedImage.TYPE_INT_ARGB);
			String path = chooser.getSelectedFile() + "\\";
			container.paint(im.getGraphics());
			try {
				ImageIO.write(im, "PNG", new File(path + "" + "graph.png"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "Lưu thành công");
		} else {
			System.out.println("No Selection ");
		}
	}
}
