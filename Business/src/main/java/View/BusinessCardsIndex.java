package View;

import java.awt.SystemColor;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
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
import com.jgoodies.forms.factories.DefaultComponentFactory;

public class BusinessCardsIndex extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField numberTextField;
	private JTextField nameTextField;
	private JTextField idTextField;
	private JButton btnNewButton;
	private JButton confirmButton;
	private JButton editButton;
	private JComboBox<CardTypes> typeComboBox;
	private JFrame parentFrame;
	private JComboBox<BasicCard> comboBox;


	/**
	 * Create the panel.
	 */
	public BusinessCardsIndex(JFrame frame) {
		
		this.parentFrame = frame;


		setLayout(null);
		setBackground(SystemColor.activeCaption);
		
		JLabel lblNewLabel = new JLabel("Number");
		lblNewLabel.setBounds(10, 67, 64, 14);
		add(lblNewLabel);
		
		numberTextField = new JTextField();
		numberTextField.setBounds(141, 67, 96, 20);
		PlainDocument doc = (PlainDocument) numberTextField.getDocument();
		doc.setDocumentFilter(new NineDigitsFilter());
		numberTextField.setColumns(10);
		numberTextField.setEditable(false);
		add(numberTextField);
		
		JLabel lblNewLabel_1 = new JLabel("Name");
		lblNewLabel_1.setBounds(10, 92, 49, 14);
		add(lblNewLabel_1);
		
		nameTextField = new JTextField();
		nameTextField.setColumns(10);
		nameTextField.setBounds(141, 92, 96, 20);
		nameTextField.setEditable(false);
		add(nameTextField);
		
		JLabel lblNewLabel_1_1 = new JLabel("Identification Number");
		lblNewLabel_1_1.setBounds(10, 117, 153, 14);
		add(lblNewLabel_1_1);
		
		idTextField = new JTextField();
		idTextField.setColumns(10);
		idTextField.setBounds(141, 117, 96, 20);
		PlainDocument doc_2 = (PlainDocument) idTextField.getDocument();
		doc_2.setDocumentFilter(new NineDigitsFilter());
		idTextField.setEditable(false);
		add(idTextField);
		
		confirmButton = new CustomButton("Confirm",150,50,40);
		confirmButton.setToolTipText("Press to confirm card changes.");
		confirmButton.setBounds(268, 11, 89, 23);
		confirmButton.addActionListener(e -> updateCardDetails());
		confirmButton.setVisible(false);
		add(confirmButton);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Type");
		lblNewLabel_1_1_1.setBounds(10, 142, 121, 14);
		add(lblNewLabel_1_1_1);
		
		typeComboBox = new JComboBox<>();
		typeComboBox.setBounds(141, 142, 96, 22);
		for(CardTypes type : CardTypes.values()) {
			if(type!=null) {
				typeComboBox.addItem(type);
			}
		}
		typeComboBox.setEnabled(false);
		add(typeComboBox);
		
		JLabel cardLabel = new JLabel("Card");
		cardLabel.setBounds(10, 17, 64, 14);
		add(cardLabel);
		
		comboBox = new JComboBox<>();
		comboBox.setBounds(68, 13, 169, 22);
		for(BasicCard card : Admin.activeBusiness.getBusinessCards().values())
			comboBox.addItem(card);
		comboBox.addActionListener(e -> showCardDetails());
		add(comboBox);
		
		JLabel lblNewLabel_2 = new JLabel("______________________________________");
		lblNewLabel_2.setBounds(10, 42, 265, 14);
		add(lblNewLabel_2);
		
		editButton = new CustomButton("Done", 150, 50, 40);
		editButton.setText("Edit");
		editButton.setToolTipText("Press to add the new business");
		editButton.setBounds(268, 11, 89, 23);
		editButton.setVisible(true);
		editButton.addActionListener(e -> makeFieldsEditable());
		add(editButton);
		
		btnNewButton = new CustomButton("Remove",150,50,40);
		btnNewButton.setBounds(361, 13, 89, 23);
		btnNewButton.addActionListener(e -> {
			BasicCard currentCard = (BasicCard)comboBox.getSelectedItem();
			if(currentCard==null)
				return;
			comboBox.removeItem(currentCard);
			Admin.activeBusiness.removeBusinessCard(currentCard);

		});
		add(btnNewButton);
		
		showCardDetails();
	}
	
	private void showCardDetails() {
	
		BasicCard currentCard = (BasicCard)comboBox.getSelectedItem();
		if(currentCard==null)
			return;
		
		numberTextField.setText(String.valueOf(currentCard.getNumber()));
		nameTextField.setText(currentCard.getName());
		idTextField.setText(String.valueOf(currentCard.getId()));
		typeComboBox.setSelectedItem(currentCard.getCardType());
	}
	
	private void makeFieldsEditable() {
		
		BasicCard currentCard = (BasicCard)comboBox.getSelectedItem();
		if(currentCard==null)
			return;
		
		nameTextField.setEditable(true);
		idTextField.setEditable(true);
		typeComboBox.setEnabled(true);
		confirmButton.setVisible(true);
		editButton.setVisible(false);
	}
	
	private void updateCardDetails() {
		
		BasicCard currentCard = (BasicCard)comboBox.getSelectedItem();
		if(currentCard==null)
			return;
		
		String name = nameTextField.getText();
		int	id = Integer.parseInt(idTextField.getText());
		CardTypes type = (CardTypes)typeComboBox.getSelectedItem();
		
		currentCard.setName(name);
		currentCard.setId(id);
		currentCard.setCardType(type);
		
		nameTextField.setEditable(false);
		idTextField.setEditable(false);
		typeComboBox.setEnabled(false);
		
		confirmButton.setVisible(false);
		editButton.setVisible(true);
		
	}
}
