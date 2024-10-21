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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
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
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

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
    private JLabel fileNameLabel;
    private JLabel fileNameLabel_2;
    private JInternalFrame testFrame;
    private JComboBox<BasicCard> businessCardsComboBox;
	private JFrame parentFrame;
	private BasicCard currentBusinessCard;
	private double rot = 0.0;

    
    
	/**
	 * Create the panel.
	 */
	public Factory(JFrame frame) {
		setAlignmentY(Component.TOP_ALIGNMENT);

		this.parentFrame = frame;

		setBackground(SystemColor.activeCaption);
		setLayout(null);
		previousFiles = new Stack<>();

		
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
        testFrame.setBackground(SystemColor.activeCaption);
        testFrame.setResizable(true);
        testFrame.setFrameIcon(null);
        testFrame.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        testFrame.setBounds(319,10,566,401);
        testFrame.getContentPane().setLayout(new BorderLayout());
        testFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        testFrame.setVisible(true);  // Make sure the internal frame is visible
        add(testFrame);
        
        JPanel p = new JPanel();
        p.setBackground(SystemColor.activeCaption);
        testFrame.getContentPane().add(p, BorderLayout.SOUTH);
        
		// buttons to go through all the business files
		
		leftArrowButton_1 = new CustomArrowButton(0,150,50,40);
		leftArrowButton_1.setDirection(7);
		leftArrowButton_1.addActionListener(e -> showPreviousFile());
		leftArrowButton_1.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Previous File");
		leftArrowButton_1.getActionMap().put("Previous File", new AbstractAction() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    public void actionPerformed(ActionEvent e) {
		        showPreviousFile();
		    }
		});
		p.setLayout(new GridLayout());
		p.add(leftArrowButton_1);
		
		rightArrowButton = new CustomArrowButton(0,150,50,40);
		rightArrowButton.setDirection(3);
		rightArrowButton.addActionListener(e -> showNextFile());
		rightArrowButton.addKeyListener(new KeyAdapter() {
		    public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_RIGHT) showNextFile();
		    }
		});	
		
		rightArrowButton.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Next File");
		rightArrowButton.getActionMap().put("Next File", new AbstractAction() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    public void actionPerformed(ActionEvent e) {
		        showNextFile();
		    }
		});
		
		p.add(rightArrowButton);
		
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
        });
        
        // show current zoom value
        zoomSlider.addChangeListener(e ->{
            zoomSlider.setToolTipText(String.valueOf(zoomSlider.getValue()));
        });
        
        rotateButton = new CustomButton("Rotate",150,50,40);
        rotateButton.addActionListener(e -> {
                    rot += Math.PI / 4;
                    labelFileDisplay.repaint();
                });
                
        p.add(rotateButton);
        p.add(zoomSlider); 
        
        
        // button to move file from not ready to ready storage
        doneButton = new CustomButton("Done",150,50,40);
        doneButton.addActionListener(e -> addFileToCard());
        p.add(doneButton);
        
        undoButton = new CustomButton("Undo",150,50,40);
        undoButton.addActionListener(e -> {
            // Check if there are any files to undo
            if (!previousFiles.isEmpty()) {
                // Pop the last removed file from the stack
                File lastRemovedFile = previousFiles.pop();

                // Add the file back to the current business files list
                currentBusinessFiles.add(currentIndex, lastRemovedFile);
                
                // Remove the file from the card
                if(currentBusinessCard!=null)
                	currentBusinessCard.getBusinessFiles().getReady().remove(lastRemovedFile);

                // Update the display
                currentFile = lastRemovedFile;
                initiate();
            }
        });
        p.add(undoButton);           
		        

        
        businessCardsComboBox = new JComboBox<>();
        businessCardsComboBox.setBounds(20, 104, 245, 22);
        add(businessCardsComboBox);
        
        JLabel lblChooseCard = new JLabel("Choose Card :");
        lblChooseCard.setBounds(20, 83, 115, 14);
        add(lblChooseCard);
        
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
		}else labelFileDisplay.setText("NO FILES AVIALABLE");
	}
	
	
	private void addFileToCard() {
		
	    // Check if any business card is selected in the combo box
		if(businessCardsComboBox.getItemCount()>0)
			currentBusinessCard = (BasicCard)businessCardsComboBox.getSelectedItem();
		else
			currentBusinessCard = null;

	    // Proceed only if both the current file and business card are valid and there are new files left
        if(currentFile!=null && currentBusinessCard!=null && currentBusinessFiles.isEmpty()!= true) {
        	
            // Add the current file to the 'ready' files list of the business card
        	currentBusinessCard.getBusinessFiles().getReady().add(currentFile);
        	
            // Remove the file from the current business file list if index is valid
	       	if(currentBusinessFiles.size()-1>=currentIndex)
	       		currentBusinessFiles.remove(currentIndex);
	       	
	        // Push the current file to the previousFiles stack
	       	previousFiles.push(currentFile);
	       	
	        // If the current business files are empty, clear the file display icon
	       	if(currentBusinessFiles.isEmpty())
	       		labelFileDisplay.setIcon(null);
	       	
            // Reinitialize the display if there are still files left
	       	else 
	       		initiate();
       	}
	}
	
	
	private void showNextFile() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();

		if(currentBusinessFiles!=null)
			if(currentBusinessFiles.size()-1 > currentIndex) {
				currentIndex++;
				loadAndRenderFile(currentIndex);
			}
		
	}
		
		
	private void showPreviousFile() {
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();

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
        	                Graphics2D g2 = (Graphics2D) g;
        	                g2.rotate(rot, labelFileDisplay.getIcon().getIconWidth() / 2, labelFileDisplay.getIcon().getIconWidth() / 2);
       		                g2.drawImage(((ImageIcon) labelFileDisplay.getIcon()).getImage(), 0, 0, null);
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

    
    private void resizeImage() {
        // Calculate new dimensions based on the zoom factor
    	if(originalImage!=null) {
	        int newWidth = (int) (originalImage.getWidth() * zoomFactor);
	        int newHeight = (int) (originalImage.getHeight() * zoomFactor);
	
	        // Create a new scaled image
	        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	        labelFileDisplay.setIcon(new ImageIcon(scaledImage)); // Update JLabel with new image
    	}
    }
}
