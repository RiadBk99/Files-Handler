package Classes;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class BusinessStorage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;
	
	
	private ArrayList<File>ready;
	private ArrayList<File>notReady;
	private HashMap<String,ArrayList<File>>cardReadyFilesByCatagory; // all card ready files by catagory

	

	public BusinessStorage(ArrayList<File> ready, ArrayList<File> notReady,
			HashMap<String,ArrayList<File>>cardReadyFilesByCatagory) {
		super();
		this.ready = ready;
		this.notReady = notReady;
		this.cardReadyFilesByCatagory = cardReadyFilesByCatagory;
	}
	
	public BusinessStorage() {
		super();
		this.ready = new ArrayList<>();
		this.notReady = new ArrayList<>();
		this.cardReadyFilesByCatagory = new HashMap<String,ArrayList<File>>();
	}
	
	public boolean addCardFile(String catagory, File file) {
		// Check if current card has the catagory, if so add the file to the current assigned array
		if(cardReadyFilesByCatagory.containsKey(catagory))
			cardReadyFilesByCatagory.get(catagory).add(file);
		else {
		// Make new arraylist and add it to the card with the new catagory
			ArrayList<File> temp= new ArrayList<>();
			temp.add(file);
			cardReadyFilesByCatagory.put(catagory,temp);
		}
		// Check if the file was added properly (catagory exists and contains the new file)
		if(cardReadyFilesByCatagory.containsKey(catagory))
			return(cardReadyFilesByCatagory.get(catagory).contains(file));
		else
			return false;
	}
	
	public boolean removeCardFile(String catagory, File file) {
		if(cardReadyFilesByCatagory.containsKey(catagory)) {
			if(cardReadyFilesByCatagory.get(catagory).contains(file))
				cardReadyFilesByCatagory.get(catagory).remove(file);
		}
		else return true;
		
		return cardReadyFilesByCatagory.get(catagory).contains(file);
	}
	
	
	
	public HashMap<String, ArrayList<File>> getCardReadyFilesByCatagory() {
		return cardReadyFilesByCatagory;
	}

	public void setCardReadyFilesByCatagory(HashMap<String, ArrayList<File>> cardReadyFilesByCatagory) {
		this.cardReadyFilesByCatagory = cardReadyFilesByCatagory;
	}

	public ArrayList<File> getReady() {
		return ready;
	}
	public void setReady(ArrayList<File> ready) {
		this.ready = ready;
	}
	public ArrayList<File> getNotReady() {
		return notReady;
	}
	public void setNotReady(ArrayList<File> notReady) {
		this.notReady = notReady;
	}




	

}
