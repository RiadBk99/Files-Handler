package Model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import Classes.BasicBusiness;
import Classes.BasicCard;
import Classes.MainBusiness;

public class Control implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;

	private static Control instance ;
	private ObjectOutputStream output;

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
	
	
	
	public void openFile() {
		try {
			output = new ObjectOutputStream(
					new FileOutputStream("MyBusiness.ser"));
		}catch(IOException ex) {
			System.err.println("Error Opening file");
		}
	}

	public void closeFile() {
		try {
			if(output != null) {
				output.close();
			}
		}catch(IOException ex) {
			System.err.println("Error Closing file");
			System.exit(1);
		}
	}
	
	public static void setInstance(Control MyBusinessFromFile) {
		instance = MyBusinessFromFile;
	}
	public ObjectOutputStream getOutput() {
		return output;
	}
	public void setOutput(ObjectOutputStream output) {
		this.output = output;
	}
	
}
