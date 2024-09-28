package View;

import java.awt.Color;
import java.awt.Image;
import java.awt.SystemColor;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import Classes.BasicBusiness;
import Model.Control;
import javax.swing.JLabel;

public class AddFiles extends JPanel {
	
	private JButton singleFileUpload;
    private JButton wholeFolderUpload;
	private JComboBox<BasicBusiness> businessComboBox;
    private ArrayList<File> currentBusinessFiles;
    private BasicBusiness currentBusiness;
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public AddFiles() {
		setLayout(null);
		setBackground(SystemColor.activeCaption);
		
		JLabel chooseBusiness = new JLabel("Choose Business :");
		chooseBusiness.setBounds(10, 15, 116, 14);
		add(chooseBusiness);

		// combobox with all avaliable businesses
		businessComboBox = new JComboBox<>();
		businessComboBox.setBounds(128, 11, 137, 22);
		for(BasicBusiness business : Control.getInstance().getAllBusinesses().values())
			if(business!=null)
				businessComboBox.addItem(business);		
		add(businessComboBox);
		// make selected business change according to selection on Combobox and update current business files variables
		businessComboBox.addActionListener(e -> initiate());

		
		// upload single file
		singleFileUpload = new JButton("Choose File");
		singleFileUpload.setToolTipText("Add file to business Storage");
		singleFileUpload.setBackground(new Color(179, 229, 251));
		singleFileUpload.setHorizontalAlignment(SwingConstants.CENTER);
		singleFileUpload.setBounds(42, 80, 140, 94);
		singleFileUpload.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		singleFileUpload.addActionListener(e -> uploadFile());
		add(singleFileUpload);
		
				
		wholeFolderUpload = new JButton("Choose Folder");
		wholeFolderUpload.setToolTipText("Add multiple files to business Storage");
		wholeFolderUpload.setHorizontalAlignment(SwingConstants.CENTER);
		wholeFolderUpload.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		wholeFolderUpload.setBackground(new Color(179, 229, 251));
		wholeFolderUpload.setBounds(225, 80, 140, 94);
		wholeFolderUpload.addActionListener(e -> uploadWholeFolder());
		add(wholeFolderUpload);

		initiate();

		
	}
	
	// update business variables
	private void initiate() { 
		currentBusiness = (BasicBusiness)businessComboBox.getSelectedItem();
		if(currentBusiness!=null) 
			currentBusinessFiles = currentBusiness.getBusinessFiles().getNotReady();
		
	}

    private void uploadFile() {

        JFileChooser fileChooser = new JFileChooser(); // Create a file chooser dialog
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Only allow files, not directories
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
        		"Image Files (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif", "PDF");
        fileChooser.setFileFilter(imageFilter);

        // Show the file chooser and get the result
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile(); // Get the selected file
            String fileName = selectedFile.getName();
            if (fileName.endsWith(".pdf")) {
            	uploadPdfFile(selectedFile, fileName);
            }
            else
            	currentBusinessFiles.add(selectedFile);

        }

    }
    
    private void uploadWholeFolder() {
    	
        File selectedDirectory; // directory to be selected
        JFileChooser fileChooser = new JFileChooser(); // Create a file chooser dialog
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Only allow directories
        
        // Show the file chooser and get the result
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedDirectory = fileChooser.getSelectedFile(); // Get the selected file        
	        Path folderPath = selectedDirectory.toPath();
	
	        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath)) {
	            for (Path filePath : stream) {
	
	            	if (Files.isRegularFile(filePath)) {
                    	File currentFile = new File(String.valueOf(filePath));
	                    String fileName = filePath.getFileName().toString();
	
	                    if (fileName.endsWith(".jpg")|| fileName.endsWith(".jpeg")
	                    		|| fileName.endsWith(".png") ) {
	                
	                        currentBusinessFiles.add(currentFile);
	                    }
	                    
	                    // Split each pdf page to a seperate file	
	                    
	                    if (fileName.endsWith(".pdf")) {
	                    	uploadPdfFile(currentFile, fileName);
	                    }
	            	}
	            }
		        } catch (IOException | DirectoryIteratorException e) {
		            e.printStackTrace();
		        }
	        }
        
    }
    
    private void uploadPdfFile(File currentFile , String fileName) {
    	try {
            if (fileName.endsWith(".pdf")) {
                PDDocument document = Loader.loadPDF(currentFile);

                // Split the PDF into individual pages
                for (int i = 0; i < document.getNumberOfPages(); i++) {
                    PDDocument singlePageDocument = new PDDocument();
                    singlePageDocument.addPage(document.getPage(i));

                    // Create a new file name for the single-page PDF
                    String singlePageFileName = fileName.replace(".pdf", "_page_" + (i + 1) + ".pdf");
                    File singlePageFile = new File(currentFile.getParentFile().getPath(), singlePageFileName);

                    // Save the single-page PDF
                    singlePageDocument.save(singlePageFile);
                    singlePageDocument.close();

                    // Add the new single-page file to the upload list
                    currentBusinessFiles.add(singlePageFile);
                }
                // Close the original PDF document
                document.close();
            }
		    } catch (IOException | DirectoryIteratorException e) {
		        e.printStackTrace();
		    }
        }
    
}
