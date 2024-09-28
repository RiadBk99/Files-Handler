package Model;

import java.util.HashMap;

import Classes.BasicBusiness;
import Classes.BasicCard;
import Classes.MainBusiness;

public class Control {
	
	private static Control instance ;

	private HashMap<Integer,MainBusiness> allBusinesses; // by business number


	
	private Control() {
		super();
		this.allBusinesses = new HashMap<>();

	}
	
	public static Control getInstance() {
		if (instance==null) {
			instance = new Control();			
		}
		return instance;
	}
	
	
	public boolean addBusiness(MainBusiness business) {
		if(allBusinesses.containsKey(business.getNumber()))
			return false;
		else
			allBusinesses.put(business.getNumber(), business);
		
		return (allBusinesses.containsKey(business.getNumber()));
	}
	
	
	


	public HashMap<Integer, MainBusiness> getAllBusinesses() {
		return allBusinesses;
	}

	public void setAllBusinesses(HashMap<Integer, MainBusiness> allBusinesses) {
		this.allBusinesses = allBusinesses;
	}
	
	
	
	
	
}
