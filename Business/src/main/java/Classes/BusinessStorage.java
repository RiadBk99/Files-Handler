package Classes;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BusinessStorage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;
	
	private HashSet<String>catagories;
	private HashMap<String,ArrayList<File>>ready;
	private ArrayList<File>notReady;
	
	

	public BusinessStorage(HashMap<String,ArrayList<File>> ready, ArrayList<File> notReady, HashSet<String> catagories) {
		super();
		this.ready = ready;
		this.notReady = notReady;
		this.catagories = catagories;
	}
	
	public BusinessStorage() {
		super();
		this.ready = new HashMap<>();
		this.notReady = new ArrayList<>();
		this.catagories = new HashSet<>();
	}
	
	public HashMap<String,ArrayList<File>> getReady() {
		return ready;
	}
	public void setReady(HashMap<String,ArrayList<File>> ready) {
		this.ready = ready;
	}
	public ArrayList<File> getNotReady() {
		return notReady;
	}
	public void setNotReady(ArrayList<File> notReady) {
		this.notReady = notReady;
	}
	public HashSet<String> getCatagories() {
		return catagories;
	}
	public void setCatagories(HashSet<String> catagories) {
		this.catagories = catagories;
	}



	

}
