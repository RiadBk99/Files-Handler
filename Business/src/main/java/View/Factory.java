package View;

import javax.swing.JPanel;
import Classes.BasicBusiness;
import Classes.BasicCard;
import Classes.BusinessStorage;
import Classes.MainBusiness;
import Model.Control;
import ViewHelper.CustomArrowButton;
import ViewHelper.CustomButton;
import ViewHelper.CustomZoomSlider;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import java.awt.ScrollPane;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

public class Factory extends JPanel {

	private static final long serialVersionUID = 1L;
	private JComboBox<MainBusiness> businessComboBox;
    private JLabel labelFileDisplay;
    private ScrollPane scrollPane;
    private float zoomFactor = 1.0f; // Initial zoom factor
    private BufferedImage originalImage;
    private JSlider zoomSlider;
    private JButton doneButton;
    private File currentFile; 
    private ArrayList<File> currentBusinessFiles;
    private MainBusiness currentBusiness;
    private int currentIndex;
    private JButton undoButton;
    private JButton rotateButton;
    private BasicArrowButton rightArrowButton;
    private BasicArrowButton leftArrowButton_1;
    private Stack<File>previousFiles;
    private Stack<BasicCard>previousFilesCards;
    private Stack<String>previousFileCatagory;
    private JLabel fileNameLabel;
    private JLabel fileNameLabel_2;
    private JInternalFrame testFrame;
	private JFrame parentFrame;
	private BasicCard currentBusinessCard;
	private double rot = 0.0;
	private JComboBox<String> cardCatagoriesComboBox;
	private JTextField catagoryTextField;
	private JCheckBox chckbxNewCheckBox;
	private JButton btnNewButton;
	private JFrame newCardJFrame;
	private JComboBox<BasicCard> businessCardsComboBox;

    
    
	/**
	 * Create the panel.
	 */
	public Factory(JFrame frame) {
		setAlignmentY(Component.TOP_ALIGNMENT);

		this.parentFrame = frame;

		setBackground(SystemColor.activeCaption);
		setLayout(null);
		previousFiles = new Stack<>();
		previousFilesCards = new Stack<>();
		previousFileCatagory = new Stack<>();

		JLabel businessLabel = new JLabel("Choose Business :");
		businessLabel.setBounds(20, 10, 115, 14);
		add(businessLabel);
		
		// combobox to hold all businesses with not ready files
		businessComboBox = new JComboBox<>();
		businessComboBox.setBounds(20, 35, 245, 22);
		for(MainBusiness business : Control.getInstance().getAllBusinesses().values())
			if(business!=null)
				if(business.getBusinessFiles().getNotReady().isEmpty()==false)				
					businessComboBox.addItem(business);		
		add(businessComboBox);
		
		fileNameLabel = new JLabel();

		// change business according to selection on Combobox and update current business files variables
		businessComboBox.addActionListener(e -> initiate());
				
        // label to display the file
        labelFileDisplay = new JLabel("");
        labelFileDisplay.setHorizontalAlignment(JLabel.CENTER);
        labelFileDisplay.setBounds(335, 10, 566, 534);

		// scrollpane to view file properly
		scrollPane = new ScrollPane();
		scrollPane.setBounds(335, 10, 566, 534);
		scrollPane.add(labelFileDisplay);
		
		// jframe to hold the label, more flexibilty for view and resizing
        testFrame = new JInternalFrame();
        testFrame.setResizable(true);
        testFrame.setFrameIcon(null);
        testFrame.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        testFrame.setBounds(319,10,566,401);
        testFrame.getContentPane().setLayout(new BorderLayout());
        testFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        testFrame.setVisible(true);  // Make sure the internal frame is visible
        add(testFrame);
        
        JPanel p = new JPanel();
		p.setLayout(new GridLayout());
        p.setBackground(SystemColor.activeCaption);
        testFrame.getContentPane().add(p, BorderLayout.SOUTH);
        
		// buttons to go through all the business files
		
		leftArrowButton_1 = new CustomArrowButton(0,150,50,40);
		leftArrowButton_1.setDirection(7);
		leftArrowButton_1.addActionListener(e -> showPreviousFile());
		p.add(leftArrowButton_1);
		
		rightArrowButton = new CustomArrowButton(0,150,50,40);
		rightArrowButton.setDirection(3);
		rightArrowButton.addActionListener(e -> showNextFile());
		p.add(rightArrowButton);
		
		
		Action leftAction = new LeftAction();
		Action rightAction = new RightAction();
		
		testFrame.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0), "leftAction");
		testFrame.getActionMap().put("leftAction", leftAction);
		
		testFrame.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0), "rightAction");
		testFrame.getActionMap().put("rightAction", rightAction);
		
        
        // adjust display size
        zoomSlider = new JSlider(0, 200, 100); // Zoom range from 10% to 200%
        zoomSlider.setBackground(SystemColor.activeCaption);
        zoomSlider.setMajorTickSpacing(100);
        zoomSlider.setMinorTickSpacing(5);
        zoomSlider.setUI(new CustomZoomSlider(zoomSlider));

        zoomSlider.addChangeListener(e -> {
            zoomFactor = zoomSlider.getValue() / 100.0f; // Update zoom factor
            resizeImage(); // Resize the image based on the new zoom factor
            repaint(); // Repaint the panel
            testFrame.requestFocus();
        });
        
        // show current zoom value
        zoomSlider.addChangeListener(e ->{
            zoomSlider.setToolTipText(String.valueOf(zoomSlider.getValue()));
        });
        
        rotateButton = new CustomButton("Rotate",150,50,40);
        rotateButton.addActionListener(e -> {
                    rot += Math.PI / 4;
                    labelFileDisplay.repaint();
                    scrollPane.revalidate();
                    scrollPane.repaint();
                });
                
        p.add(rotateButton);
        p.add(zoomSlider); 
        
        
        // button to move file from not ready to ready storage
        doneButton = new CustomButton("Done",150,50,40);
        doneButton.addActionListener(e -> addFileToCard());
        p.add(doneButton);
        
		Action enterAction = new EnterAction();
		testFrame.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ENTER"), "enterAction");
		testFrame.getActionMap().put("enterAction", enterAction);
		
        undoButton = new CustomButton("Undo",150,50,40);
        undoButton.addActionListener(e -> removeFileFromCard());
        p.add(undoButton);
        
        businessCardsComboBox = new JComboBox<>();
        businessCardsComboBox.setBounds(20, 93, 245, 22);
        add(businessCardsComboBox);
        
        JLabel lblChooseCard = new JLabel("Choose Card :");
        lblChooseCard.setBounds(20, 68, 115, 14);
        add(lblChooseCard);
        
        JLabel lblChooseCardCatagory_1 = new JLabel("Choose Card Catagory :");
        lblChooseCardCatagory_1.setBounds(20, 174, 175, 14);
        add(lblChooseCardCatagory_1);
        
        cardCatagoriesComboBox = new JComboBox<>();
        cardCatagoriesComboBox.setBounds(20, 199, 245, 22);
        add(cardCatagoriesComboBox);
        
        chckbxNewCheckBox = new JCheckBox("New");
        chckbxNewCheckBox.setOpaque(false);
        chckbxNewCheckBox.setBounds(218, 170, 64, 23);
        chckbxNewCheckBox.addChangeListener(e -> updateView());
        add(chckbxNewCheckBox);
        
        catagoryTextField = new JTextField();
        catagoryTextField.setBounds(20, 199, 245, 20);
        catagoryTextField.setColumns(10);
        add(catagoryTextField);
        
        btnNewButton = new JButton("New Card");
        btnNewButton.setBounds(20, 125, 100, 23);
        btnNewButton.addActionListener(e -> addCardInternalFrame());
        add(btnNewButton);
        catagoryTextField.setVisible(false);

        initiate();

    }

	private void initiate() {
		// update business variables according to selected business
		currentBusiness = (MainBusiness)businessComboBox.getSelectedItem();
		
		if(currentBusiness!=null) {
			businessCardsComboBox.removeAllItems();
			for(BasicCard card : currentBusiness.getBusinessCards().values()) {
				businessCardsComboBox.addItem(card);
			}
			currentBusinessFiles = currentBusiness.getBusinessFiles().getNotReady();
			currentIndex = 0;
			if(currentBusinessFiles.isEmpty()==false) {
				currentFile = currentBusinessFiles.get(currentIndex);
				testFrame.setTitle(currentFile.getName());
				loadAndRenderFile(currentIndex);
			}
			else labelFileDisplay.setText("NO FILES AVIALABLE");
		}
		updateCatagories();

	}
	
	private void updateCatagories() {
		cardCatagoriesComboBox.removeAllItems();
		
		// Add available catagories
		if(currentBusiness!=null)
			for(String catagory : currentBusiness.getCatagories())
				cardCatagoriesComboBox.addItem(catagory);
	}
	
	private void updateView() {
		if(chckbxNewCheckBox.isSelected()) {
			catagoryTextField.setVisible(true);
			cardCatagoriesComboBox.setVisible(false);
		}else {
			catagoryTextField.setVisible(false);
			cardCatagoriesComboBox.setVisible(true);
		}
	
	}
	
	
	private void addFileToCard() {
		
	    // Check if any business card is selected in the combo box
		if(businessCardsComboBox.getItemCount()>0)
			currentBusinessCard = (BasicCard)businessCardsComboBox.getSelectedItem();
		else
			currentBusinessCard = null;
		
    	// Get selected catagory
    	String catagory = null;
    	if(chckbxNewCheckBox.isSelected()) { 
    		catagory = catagoryTextField.getText();
    		if(!currentBusiness.getCatagories().contains(catagory))
    			currentBusiness.getCatagories().add(catagory);
    		updateCatagories();

    	}
    	else
    		if(cardCatagoriesComboBox.getItemCount()>0)
    			catagory = (String)cardCatagoriesComboBox.getSelectedItem();
    	

	    // Proceed only if both the current file and business card are valid and there are new files left
        if(currentFile!=null && currentBusinessCard!=null && !currentBusinessFiles.isEmpty() && catagory != null) {
        	currentBusiness.addCardFile(catagory, currentFile);
        	currentBusinessCard.getBusinessFiles().addCardFile(catagory, currentFile);
        	
        // Remove the file from the current business file list if index is valid
	    if(currentBusinessFiles.size()>=currentIndex) {
	      	currentBusinessFiles.remove(currentIndex);
	       	
	    // Push the current file to the previousFiles stack
	    previousFiles.push(currentFile);
	    previousFilesCards.push(currentBusinessCard);
	    previousFileCatagory.push(catagory);

	       	
        if (currentIndex >= currentBusinessFiles.size()) {
            currentIndex = 0; // Reset index to the start if end is reached
            }
	       	
	    // If the current business files are empty, clear the file display icon
	       	if(currentBusinessFiles.isEmpty()) {
	       		labelFileDisplay.setIcon(null);
	       		labelFileDisplay.setText("NO FILES AVAILABLE");
	       		}else
	       			loadAndRenderFile(currentIndex);
	       	}
        }
	}
	
	private void removeFileFromCard() {
         // Check if there are any files to undo
         if (!previousFiles.isEmpty()) {
         // Pop the last removed file from the stack
         File lastRemovedFile = previousFiles.pop();
         BasicCard lastCard = previousFilesCards.pop();
         String catagory = previousFileCatagory.pop();
         
         currentBusiness.removeCardFile(catagory, lastRemovedFile, lastCard.getNumber());
         // Update the display
         currentFile = lastRemovedFile;
         initiate();
         }
	}
	
	private void showNextFile() {
		if(currentBusinessFiles!=null)
			if(currentBusinessFiles.size()-1 > currentIndex) {
				currentIndex++;
				loadAndRenderFile(currentIndex);
			}
				
	}
		
		
	private void showPreviousFile() {
		
		if(currentIndex > 0) {
			currentIndex--;
			loadAndRenderFile(currentIndex);
		}

	}
	
	
	
    private void loadAndRenderFile(int index) {
    	
    	
 
            try {
                // Load the PDF document using File
            	currentFile = currentBusinessFiles.get(index);
            	
            	if(currentFile.getName().endsWith(".pdf")) {
            		PDDocument document = Loader.loadPDF(currentFile);
            		// Create a PDFRenderer to render the PDF document
            		PDFRenderer pdfRenderer = new PDFRenderer(document); 
            		// Render the first page to a BufferedImage at 300 DPI
            		originalImage = pdfRenderer.renderImageWithDPI(0, 300); // Higher DPI for better quality
                    document.close();

            	}else {
            		originalImage = ImageIO.read(currentFile);
            	}
            	
				fileNameLabel.setText(currentFile.getName());
				
                ImageIcon scaledIcon = new ImageIcon(originalImage);

        		int width = originalImage.getWidth();
        		int height = originalImage.getHeight();
        		labelFileDisplay.setBounds(197, 35, width, height);
        		labelFileDisplay = new JLabel(null, scaledIcon, JLabel.CENTER) {
        			private static final long serialVersionUID = 1L;
        				@Override
        	            protected void paintComponent(Graphics g) {
        					Graphics2D g2 = (Graphics2D) g.create();
        			        if (getIcon() != null) {
        			            int iconWidth = getIcon().getIconWidth();
        			            int iconHeight = getIcon().getIconHeight();

        			            // Calculate the bounds for the rotated image
        			            double cos = Math.abs(Math.cos(rot));
        			            double sin = Math.abs(Math.sin(rot));
        			            int newWidth = (int) Math.floor(iconWidth * cos + iconHeight * sin);
        			            int newHeight = (int) Math.floor(iconHeight * cos + iconWidth * sin);
        			            
        			            // Center the rotation
        			            g2.translate((getWidth() - newWidth) / 2, (getHeight() - newHeight) / 2);
        			            g2.rotate(rot, newWidth / 2.0, newHeight / 2.0);
        			            g2.drawImage(((ImageIcon) getIcon()).getImage(), 0, 0, null);
        			        }
        			        g2.dispose();
       		            }
       		        };
        		scrollPane.add(labelFileDisplay);
                resizeImage();
                scrollPane.repaint();
                scrollPane.revalidate();
                
                // Close the document to free resources
            } catch (IOException e) {
                e.printStackTrace();
            }
                		
        }
    
    private void addCardInternalFrame() {
    		Admin.activeBusiness = currentBusiness;
	    	newCardJFrame = new JFrame();
	    	newCardJFrame.getContentPane().add(new AddBusinessCard(parentFrame));
	    	newCardJFrame.setVisible(true);
	    	newCardJFrame.setResizable(true);
	    	newCardJFrame.setBounds(20,133,400,200);

    }

    
    private void resizeImage() {
        // Calculate new dimensions based on the zoom factor
    	if(originalImage!=null) {
	        int newWidth = (int) (originalImage.getWidth() * zoomFactor);
	        int newHeight = (int) (originalImage.getHeight() * zoomFactor);
	
	        // Create a new scaled image
	        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	        labelFileDisplay.setIcon(new ImageIcon(scaledImage)); // Update JLabel with new image
	        scrollPane.revalidate();
	        scrollPane.repaint();
    	}
    }

    
    public class LeftAction extends AbstractAction{

		@Override
		public void actionPerformed(ActionEvent e) {
			showPreviousFile();		
		}
    	
    }
    public class RightAction extends AbstractAction{

		@Override
		public void actionPerformed(ActionEvent e) {
			showNextFile();

		}
    	
    }
    
    public class EnterAction extends AbstractAction{

		@Override
		public void actionPerformed(ActionEvent e) {
			addFileToCard();
		}
    	
    }
}
