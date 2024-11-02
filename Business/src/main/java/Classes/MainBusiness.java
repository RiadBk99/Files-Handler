package Classes;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MainBusiness extends BasicBusiness implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;
	
	private HashMap<Integer,BasicCard> businessCards; // all cards by number
	private HashSet<String>catagories;
	private HashMap<String,ArrayList<File>>businessReadyFilesByCatagory; // all files by catagory
	

	public MainBusiness(int number, String name, int id, BusinessStorage businessFiles,
			HashMap<Integer, BasicCard> businessCards, HashSet<String> catagories,
			HashMap<String, ArrayList<File>> businessReadyFilesByCatagory) {
		super(number, name, id, businessFiles);
		this.businessCards = businessCards;
		this.catagories = catagories;
		this.businessReadyFilesByCatagory = businessReadyFilesByCatagory;
	}
	
	public MainBusiness(int number, String name, int id) {
		super(number, name, id);
		this.businessCards = new HashMap<>();
		this.catagories = new HashSet<>();
		this.businessReadyFilesByCatagory = new HashMap<>();
	}


	public boolean addBusinessCard(BasicCard card) {
		
		if(businessCards.containsKey(card.getNumber()))
			return false;
		else
			businessCards.put(card.getNumber(), card);
		
		return (businessCards.containsKey(card.getNumber()));
	}
	
	public boolean removeBusinessCard(BasicCard card) {
		
		if(!businessCards.containsKey(card.getNumber()))
			return false;
		else
			businessCards.remove(card.getNumber());
		
		return (!businessCards.containsKey(card.getNumber()));
	}
	
	public boolean addCardFile(String catagory, File file) {
		// Check if current mainbusiness has the catagory, if so add the file to the current assigned array
		if(businessReadyFilesByCatagory.containsKey(catagory))
			businessReadyFilesByCatagory.get(catagory).add(file);
		else {
		// Make new arraylist and add it to the mainbusiness with the new catagory
			ArrayList<File> temp= new ArrayList<>();
			temp.add(file);
			businessReadyFilesByCatagory.put(catagory,temp);
		}
		// Check if the file was added properly (catagory exists and contains the new file)
		if(businessReadyFilesByCatagory.containsKey(catagory))
			return(businessReadyFilesByCatagory.get(catagory).contains(file));
		else
			return false;
	}
	
	public boolean removeCardFile(String catagory, File file, Integer cardNumber) {
		
		// Remove file from current card
		if(businessCards.containsKey(cardNumber))
			businessCards.get(cardNumber).getBusinessFiles().removeCardFile(catagory, file);
		
		if(businessReadyFilesByCatagory.containsKey(catagory)) {
			if(businessReadyFilesByCatagory.get(catagory).contains(file))
				businessReadyFilesByCatagory.get(catagory).remove(file);
		}
		else 
			return true;
		
		// Add file back to the not ready business files
		getBusinessFiles().getNotReady().add(file);


		return businessReadyFilesByCatagory.get(catagory).contains(file);
	}
	

	public HashMap<Integer,BasicCard> getBusinessCards() {
		return businessCards;
	}

	public void setBusinessCards(HashMap<Integer,BasicCard> businessCards) {
		this.businessCards = businessCards;
	}

	public HashSet<String> getCatagories() {
		return catagories;
	}

	public void setCatagories(HashSet<String> catagories) {
		this.catagories = catagories;
	}

	public HashMap<String, ArrayList<File>> getBusinessReadyFilesByCatagory() {
		return businessReadyFilesByCatagory;
	}
	
	public void setBusinessReadyFilesByCatagory(HashMap<String, ArrayList<File>> businessReadyFilesByCatagory) {
		this.businessReadyFilesByCatagory = businessReadyFilesByCatagory;
	}

	
	
	
}
