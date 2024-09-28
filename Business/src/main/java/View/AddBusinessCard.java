package View;

import java.awt.SystemColor;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import Classes.BasicCard;
import Enums.CardTypes;
import Model.Control;
import ViewHelper.CustomButton;
import ViewHelper.NineDigitsFilter;
import View.Admin;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class AddBusinessCard extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField numberTextField;
	private JTextField nameTextField;
	private JTextField idTextField;
	private JButton doneButton;
	private JComboBox<CardTypes> typeComboBox;
	private static int cardNumber=1;
	private JFrame parentFrame;


	/**
	 * Create the panel.
	 */
	public AddBusinessCard(JFrame frame) {
		
		this.parentFrame = frame;

		if(Admin.activeBusiness==null) {
			Admin.showMsg(new Admin(),"Please select a business", "Error",JOptionPane.ERROR_MESSAGE);
			return;
		}

		setLayout(null);
		setBackground(SystemColor.activeCaption);
		
		JLabel lblNewLabel = new JLabel("Number");
		lblNewLabel.setBounds(10, 41, 64, 14);
		add(lblNewLabel);
		
		numberTextField = new JTextField();
		numberTextField.setBounds(141, 41, 96, 20);
		PlainDocument doc = (PlainDocument) numberTextField.getDocument();
		doc.setDocumentFilter(new NineDigitsFilter());
		add(numberTextField);
		numberTextField.setColumns(10);
		while(numberTextField.getText().isBlank()) {
			if(Control.getInstance().getAllBusinesses().containsKey(cardNumber)) {
				cardNumber++;
			} else {
				numberTextField.setText(String.valueOf(cardNumber));
			}
		}
		
		JLabel lblNewLabel_1 = new JLabel("Name");
		lblNewLabel_1.setBounds(10, 66, 49, 14);
		add(lblNewLabel_1);
		
		nameTextField = new JTextField();
		nameTextField.setColumns(10);
		nameTextField.setBounds(141, 66, 96, 20);
		add(nameTextField);
		
		JLabel lblNewLabel_1_1 = new JLabel("Identification Number");
		lblNewLabel_1_1.setBounds(10, 91, 153, 14);
		add(lblNewLabel_1_1);
		
		idTextField = new JTextField();
		idTextField.setColumns(10);
		idTextField.setBounds(141, 91, 96, 20);
		PlainDocument doc_2 = (PlainDocument) idTextField.getDocument();
		doc_2.setDocumentFilter(new NineDigitsFilter());
		add(idTextField);
		
		doneButton = new CustomButton("Done",150,50,40);
		doneButton.setToolTipText("Press to add the new business");
		doneButton.setBounds(351, 11, 89, 23);
		doneButton.addActionListener(e -> addBusiness());
		add(doneButton);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Type");
		lblNewLabel_1_1_1.setBounds(10, 116, 121, 14);
		add(lblNewLabel_1_1_1);
		
		typeComboBox = new JComboBox<>();
		typeComboBox.setBounds(141, 116, 96, 22);
		for(CardTypes type : CardTypes.values()) {
			if(type!=null) {
				typeComboBox.addItem(type);
			}
		}		add(typeComboBox);
		
		JLabel lblFillTheDetails = new JLabel("Fill the details about the new Card :");
		lblFillTheDetails.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblFillTheDetails.setBounds(10, 17, 264, 14);
		add(lblFillTheDetails);
	}
	
	private void addBusiness() {

		int number = Integer.parseInt(numberTextField.getText());
		String name = nameTextField.getText();
		int id = Integer.parseInt(idTextField.getText());
		CardTypes type = (CardTypes)typeComboBox.getSelectedItem();


		if(Admin.activeBusiness.addBusinessCard(new BasicCard(number, name, id, type))) {
			Admin.showMsg(doneButton,"Card added Sucessfully", "Success",JOptionPane.INFORMATION_MESSAGE);
			resetFields();
		}
		else {
			Admin.showMsg(doneButton,"Failed to add Card", "Error",JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private void resetFields() {
		numberTextField.setText("-1");
		while(numberTextField.getText().equals("-1")) {
			if(Control.getInstance().getAllBusinesses().containsKey(cardNumber)) {
				cardNumber++;
			} else {
				numberTextField.setText(String.valueOf(cardNumber));
			}
		}
		nameTextField.setText("");
		idTextField.setText("");
	}
}
