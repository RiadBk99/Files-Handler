package View;

import java.awt.SystemColor;

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
    private JButton btnNewButton_1;
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
		add(list);
		
		lblNewLabel = new JLabel("Select Business to proceed :");
		lblNewLabel.setBounds(125, 51, 208, 14);
		add(lblNewLabel);
		
		btnNewButton_1 = new JButton("New Business");
		btnNewButton_1.addActionListener(e -> {		
			JPanel p = new AddBusiness(frame);
		    frame.setContentPane(p);
		    frame.getContentPane().setVisible(true);
		    frame.repaint();
		    frame.revalidate();
			
			});
		btnNewButton_1.setBounds(324, 47, 118, 23);
		add(btnNewButton_1);
	}
	


}
