package View;

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

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicArrowButton;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import Classes.BasicCard;
import Classes.MainBusiness;
import Model.Control;
import ViewHelper.CustomArrowButton;
import ViewHelper.CustomZoomSlider;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;

public class BusinessFiles extends JPanel {

	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
    private JComboBox<BasicCard> businessCardComboBox;
    private MainBusiness currentBusiness = Admin.activeBusiness;
    private File currentFile; 
    private ArrayList<File> currentBusinessFiles;
    private JInternalFrame displayJframe;
    private JLabel labelFileDisplay;
    private BufferedImage originalImage;
    private JLabel fileNameLabel;
    private ScrollPane scrollPane;
    private float zoomFactor = 1.0f; // Initial zoom factor
    private JSlider zoomSlider;
	private JList<File> list;
    private DefaultListModel<File> listModelCardFiles;













	/**
	 * Create the panel.
	 */
	public BusinessFiles(JFrame frame) {
		
		this.parentFrame = frame;
		
		setLayout(null);
		setBackground(SystemColor.activeCaption);
		
        businessCardComboBox = new JComboBox<>();
        businessCardComboBox.setBounds(83, 24, 151, 22);
        
		if(currentBusiness!=null) {
			for(BasicCard card : currentBusiness.getBusinessCards().values()) {
				businessCardComboBox.addItem(card);
			}
			if(businessCardComboBox.getItemCount()>0)
				currentBusinessFiles = ((BasicCard)businessCardComboBox.getSelectedItem()).getBusinessFiles().getReady();
			else
				currentBusinessFiles = new ArrayList<>();
		}
        add(businessCardComboBox);   
        
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
        zoomSlider.setBounds(10, 271, 224, 38);
        add(zoomSlider); 
        
		listModelCardFiles  = new DefaultListModel<>();

        
		list = new JList<>();
		list.setBounds(10, 77, 224, 161);
		list.setModel(listModelCardFiles);;
		list.addListSelectionListener(e -> displayFile());
		add(list);
        
		initiate();
	}
	
	
	private void initiate() {
		// update business variables according to selected business		
			listModelCardFiles.clear();
			
			if(businessCardComboBox.getItemCount()>0)
				currentBusinessFiles = ((BasicCard)businessCardComboBox.getSelectedItem()).getBusinessFiles().getReady();
			else
				currentBusinessFiles = new ArrayList<>();
			
			if(!currentBusinessFiles.isEmpty()) {
		        for(File f : currentBusinessFiles) {
		        	if(f!=null)
		        		listModelCardFiles.addElement(f);
		        	}
		        list.setSelectedIndex(0);
			}else {
				labelFileDisplay.setIcon(null);
				displayJframe.setTitle("");
			}

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
    		labelFileDisplay.setIcon(scaledIcon);
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
    	if(listModelCardFiles.getSize() > 0) {
    		currentFile = list.getSelectedValue();
    		if(currentFile!=null) {
    			displayJframe.setTitle(currentFile.getName());
    			loadAndRenderFile(currentFile);
    		}
    	}
	}
    
}
