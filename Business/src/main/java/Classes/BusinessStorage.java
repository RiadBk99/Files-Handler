package Classes;

import java.io.File;
import java.util.ArrayList;

public class BusinessStorage {
	
	
	private ArrayList<File>ready;
	private ArrayList<File>notReady;
	
	

	public BusinessStorage(ArrayList<File> ready, ArrayList<File> notReady) {
		super();
		this.ready = ready;
		this.notReady = notReady;
	}
	
	public BusinessStorage() {
		super();
		this.ready = new ArrayList<>();
		this.notReady = new ArrayList<>();
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
