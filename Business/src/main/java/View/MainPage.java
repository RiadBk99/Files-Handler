package View;

import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import Classes.MainBusiness;
import Enums.CardTypes;
import Model.Control;

public class MainPage extends JPanel {

	private static final long serialVersionUID = 1L;
    private JLabel lblNewLabel;
    private JButton newBusinessButton;
    private JButton enterButton;
	private JList<MainBusiness> list;
    private DefaultListModel<MainBusiness> listModelMainBusinesses;
	private JButton btnConfirm;
	private JButton btnNewButton;
	private JComboBox<MainBusiness> comboBox;
	private JComboBox<CardTypes> comboBox_1;
	private JFrame parentFrame;

	/**
	 * Create the panel.
	 */
	public MainPage(JFrame frame) {
		
		this.parentFrame = frame;
		
		setLayout(null);
		setBackground(SystemColor.activeCaption);
		
		
		listModelMainBusinesses = new DefaultListModel<>();
        for(MainBusiness m : Control.getInstance().getAllBusinesses().values()) {
        	if(m!=null)
        		listModelMainBusinesses.addElement(m);
        	}
        
		list = new JList<>();
		list.setBounds(125, 76, 317, 165);
		list.setModel(listModelMainBusinesses);
		list.addListSelectionListener(e -> {
			Admin.activeBusiness = list.getSelectedValue();
		});
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if(me.getClickCount()==2) {
					JPanel p = new BusinessFiles(parentFrame);
					parentFrame.setContentPane(p);
					parentFrame.getContentPane().setVisible(true);
					parentFrame.repaint();
					parentFrame.revalidate();
				}
			}
		});
		add(list);
		
		lblNewLabel = new JLabel("Select Business to proceed :");
		lblNewLabel.setBounds(125, 51, 208, 14);
		add(lblNewLabel);
		
		newBusinessButton = new JButton("New Business");
		newBusinessButton.addActionListener(e -> {		
			JPanel p = new AddBusiness(parentFrame);
			parentFrame.setContentPane(p);
			parentFrame.getContentPane().setVisible(true);
			parentFrame.repaint();
			parentFrame.revalidate();
			
			});
		newBusinessButton.setBounds(452, 104, 118, 23);
		add(newBusinessButton);
		
		enterButton = new JButton("Enter");
		enterButton.addActionListener(e -> {
			if(list.getSelectedValue()!=null) {
				JPanel p = new BusinessFiles(parentFrame);
				parentFrame.setContentPane(p);
				parentFrame.getContentPane().setVisible(true);
				parentFrame.repaint();
				parentFrame.revalidate();
			}
		});
		enterButton.setBounds(452, 73, 118, 23);
		add(enterButton);
	}
}
