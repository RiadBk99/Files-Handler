package View;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.ScrollPane;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import Classes.BasicCard;
import Classes.CustomFile;
import Classes.MainBusiness;
import Enums.CardTypes;
import Model.Control;
import ViewHelper.CustomArrowButton;
import ViewHelper.CustomButton;
import ViewHelper.CustomZoomSlider;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;

public class BusinessFiles extends JPanel {

	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
    private JComboBox<BasicCard> businessCardComboBox;
    private MainBusiness currentBusiness = Admin.activeBusiness;
    private BasicCard currentCard;
    private File currentFile; 
    private ArrayList<File> currentCardfiles;
    private JInternalFrame displayJframe;
    private JLabel labelFileDisplay;
    private BufferedImage originalImage;
    private JLabel fileNameLabel;
    private ScrollPane scrollPane;
    private ScrollPane treeScrollPane;
	private double rot = 0.0;
    private float zoomFactor = 1.0f; // Initial zoom factor
    private JSlider zoomSlider;
	private JTree tree;
	private DefaultMutableTreeNode root;
	private JButton btnNewButton;
	private BasicCard allCards;



	/**
	 * Create the panel.
	 */
	public BusinessFiles(JFrame frame) {
		
		this.parentFrame = frame;
		
		setLayout(null);
		setBackground(SystemColor.activeCaption);
		
		// Create combobox with active business's cards
        businessCardComboBox = new JComboBox<>();
        businessCardComboBox.setBounds(83, 24, 151, 22);
		allCards = new BasicCard(0,"All Cards",0,CardTypes.Client);

		if(currentBusiness!=null) {
			businessCardComboBox.addItem(allCards);

			for(BasicCard card : currentBusiness.getBusinessCards().values()) {
				businessCardComboBox.addItem(card);
			}
			if(businessCardComboBox.getItemCount()>0)
				currentCardfiles = ((BasicCard)businessCardComboBox.getSelectedItem()).getBusinessFiles().getReady();
			else
				currentCardfiles = new ArrayList<>();
		}
        add(businessCardComboBox);   
        
        // Update tree files according to chosen businessCard
		businessCardComboBox.addActionListener(e -> initiate());

		
		JLabel lblNewLabel = new JLabel("Select Card :");
		lblNewLabel.setBounds(10, 28, 112, 14);
		add(lblNewLabel);
		
		
		fileNameLabel = new JLabel();
		
        // label to display the file
        labelFileDisplay = new JLabel("");
        labelFileDisplay.setHorizontalAlignment(JLabel.CENTER);
        labelFileDisplay.setBounds(335, 10, 566, 534);
        
		// scrollpane to view file properly
		scrollPane = new ScrollPane();
		scrollPane.setBounds(335, 10, 566, 534);
		scrollPane.add(labelFileDisplay);
		
		
        displayJframe = new JInternalFrame();
        displayJframe.setResizable(true);
        displayJframe.setFrameIcon(null);
        displayJframe.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        displayJframe.setBounds(319,10,408,384);
        displayJframe.getContentPane().add(scrollPane);
        displayJframe.setVisible(true);  // Make sure the internal frame is visible
        add(displayJframe);
        
        // adjust display size
        zoomSlider = new JSlider(0, 200, 100); // Zoom range from 10% to 200%
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
        zoomSlider.setBounds(10, 66, 181, 38);
        add(zoomSlider); 
        

        // JTree holding current businessCard files
		root = new DefaultMutableTreeNode("Root"); // main root
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
        
        tree = new JTree(treeModel);     
        tree.setRootVisible(false);
   		tree.setBounds(10, 77, 224, 161);
		tree.addTreeSelectionListener(e -> displayFile());
		treeScrollPane = new ScrollPane();
		treeScrollPane.setBounds(10, 134, 224, 288);
		treeScrollPane.add(tree);
		add(treeScrollPane);

		btnNewButton = new CustomButton("Rotate",150,50,40);
		btnNewButton.setBounds(201, 66, 65, 38);
		add(btnNewButton);
		btnNewButton.addActionListener(e -> {
            rot += Math.PI / 4;
            labelFileDisplay.repaint();
            scrollPane.revalidate();
            scrollPane.repaint();
        });
        
		tree.addTreeSelectionListener(e -> displayFile());
		
		
				
		initiate();
	}
	
	
	private void initiate() {
		
		currentCard = (BasicCard) businessCardComboBox.getSelectedItem();
		// Start view with all available files by catagories in the business
		if(currentCard.getNumber()==0) {
			root.removeAllChildren();
		// Go through all available catagories
			for(String catagory : currentBusiness.getBusinessReadyFilesByCatagory().keySet()) {
				if(catagory!=null) {
					DefaultMutableTreeNode catagoryRoot = new DefaultMutableTreeNode(catagory);
					root.add(catagoryRoot);
		// Add each catagory files
					for(File f : currentBusiness.getBusinessReadyFilesByCatagory().get(catagory))
						if(f!=null) {
							CustomFile customF = new CustomFile(f);
							catagoryRoot.add(new DefaultMutableTreeNode(customF));
						}
							
				}
			}
			
		    DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
		    treeModel.reload(root);
			return;
		}
		if(currentCard.getNumber()!=0) {
		// If the user selected a specific card, update files and assign them by catagories
			root.removeAllChildren();
			
		// Add all available catagories
			for(String catagory : currentCard.getBusinessFiles().getCardReadyFilesByCatagory().keySet()) {
				if(catagory!=null) {
					DefaultMutableTreeNode catagoryRoot = new DefaultMutableTreeNode(catagory);
					root.add(catagoryRoot); 
		// Add all available files related to the catagory
					for(File f : currentCard.getBusinessFiles().getCardReadyFilesByCatagory().get(catagory))		        	
						if(f!=null) {
							CustomFile customF = new CustomFile(f);
							catagoryRoot.add(new DefaultMutableTreeNode(customF));
						}
				}
		    DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
		    treeModel.reload(root);
		    return;
			}
		}
		//	Last possible scenario
		
		labelFileDisplay.setIcon(null);
		displayJframe.setTitle("");
	}
	
    private void loadAndRenderFile(File file) {
    	
    	
    	 
        try {
            // Load the PDF document using File
        	
        	if(file.getName().endsWith(".pdf")) {
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
    
    private void displayFile() {
    	if(!tree.isSelectionEmpty()) {
    		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            Object userObject = currentNode.getUserObject();
            if (userObject instanceof File) 
            	currentFile = (File)userObject;
    		if(currentFile!=null) {
    			displayJframe.setTitle(currentFile.getName());
    			loadAndRenderFile(currentFile);
    		}
    	}
	}
}
