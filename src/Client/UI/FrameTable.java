package Client.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

public class FrameTable extends JFrame {

	private JPanel contentPane;
	private JTable tableData;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameTable frame = new FrameTable();
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
	public FrameTable() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		tableData = new JTable();
		contentPane.add(tableData, BorderLayout.CENTER);
		Object []columns = {"1","2","3"};
		DefaultTableModel modeltable = new DefaultTableModel();
		
		modeltable.setColumnIdentifiers(columns);
		tableData.setModel(modeltable);
		tableData.setBackground(Color.white);
		tableData.setForeground(Color.black);
		tableData.setSelectionBackground(Color.red);
		tableData.setGridColor(Color.red);
		tableData.setSelectionForeground(Color.white);
		tableData.setFont(new Font("Tahoma",Font.PLAIN,17));
		tableData.setRowHeight(30);
		tableData.setAutoCreateRowSorter(true);
		
		JScrollPane pane = new JScrollPane(tableData);
		pane.setForeground(Color.red);
		pane.setBackground(Color.white);
		pane.setForeground(Color.red);
		pane.setBounds(10,10,10,10);
		contentPane.add(pane);
		
	}

}
