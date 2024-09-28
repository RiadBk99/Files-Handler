package View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Classes.BasicCard;
import Classes.CardMovement;
import Classes.MainBusiness;
import Enums.CardTypes;
import Model.Control;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.SystemColor;

import javax.swing.JSplitPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JList;

public class Admin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox<MainBusiness> comboBox;
	private JComboBox<CardTypes> comboBox_1;
	private JButton btnConfirm;
	private JButton btnNewButton;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmNewMenuItem_1;
	private JMenu navigateMenu;
	private JMenuItem mntmNewMenuItem_2;
	public static MainBusiness activeBusiness;
	private JList<MainBusiness> list;
    private DefaultListModel<MainBusiness> listModelMainBusinesses;
    private JMenuItem mntmNewMenuItem_3;
    private JLabel lblNewLabel;
    private JButton btnNewButton_1;
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Admin frame = new Admin();
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
	public Admin() {
		
		getContentPane().setLayout(null);
		setBackground(SystemColor.activeCaption);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.activeCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		listModelMainBusinesses = new DefaultListModel<>();
        for(MainBusiness m : Control.getInstance().getAllBusinesses().values()) {
        	listModelMainBusinesses.addElement(m);
        			}
        
		list = new JList<>();
		list.setBounds(125, 76, 317, 165);
		list.setModel(listModelMainBusinesses);
		list.addListSelectionListener(e -> {
			activeBusiness = list.getSelectedValue();
		});
		contentPane.add(list);
		
		lblNewLabel = new JLabel("Select Business to proceed :");
		lblNewLabel.setBounds(125, 51, 208, 14);
		contentPane.add(lblNewLabel);
		
		btnNewButton_1 = new JButton("New Business");
		btnNewButton_1.addActionListener(e -> openPanel("Add Business"));
		btnNewButton_1.setBounds(324, 47, 118, 23);
		contentPane.add(btnNewButton_1);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		navigateMenu = new JMenu("Navigate");
		navigateMenu.setBounds(117, 392, 115, 26);
		navigateMenu.setOpaque(false);
		menuBar.add(navigateMenu);
		
		mntmNewMenuItem = new JMenuItem("Factory");
		mntmNewMenuItem.addActionListener(e -> openPanel(mntmNewMenuItem.getText()));
		navigateMenu.add(mntmNewMenuItem);

		mntmNewMenuItem_3 = new JMenuItem("Main menu");
		mntmNewMenuItem_3.addActionListener(e -> openPanel(mntmNewMenuItem_3.getText()));
		navigateMenu.add(mntmNewMenuItem_3);

		mntmNewMenuItem_1 = new JMenuItem("Add Files");
		mntmNewMenuItem_1.addActionListener(e -> openPanel(mntmNewMenuItem_1.getText()));
		navigateMenu.add(mntmNewMenuItem_1);
		
		mntmNewMenuItem_2 = new JMenuItem("Add Business Card");
		mntmNewMenuItem_2.addActionListener(e -> openPanel(mntmNewMenuItem_2.getText()));
		navigateMenu.add(mntmNewMenuItem_2);
		

	}

	
	private void openPanel(String panelName) {
		getContentPane().setVisible(false);
	    	    JPanel p = null;

	    switch(panelName) {
	        case "Factory":
	            p = new Factory();
	            break;
	        case "Add Files":
	            p = new AddFiles();
	            break;
	        case "Add Business Card":
	            p = new AddBusinessCard();
	            break;
	        case "Main menu":
	            p = contentPane;
	            break;
	        case "Add Business":
	            p = new AddBusiness();
	            break;
	    }

	    if (p==null) {
			return;
		}
	    setContentPane(p);
	    getContentPane().setVisible(true);
	    repaint();
	    revalidate();
	}

	protected static void showMsg(Component c,String text, String title,int msgType) {
		JOptionPane.showMessageDialog(
				c.getParent(),
				text,
				title,
				msgType);
	}
}
