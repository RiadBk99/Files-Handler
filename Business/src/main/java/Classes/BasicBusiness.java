package Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class BasicBusiness implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;
	
	private static int serial=1;		
	private final int number;	// PK
	private String name;
	private int id;
	private BusinessStorage businessFiles;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public BusinessStorage getBusinessFiles() {
		return businessFiles;
	}
	public void setBusinessFiles(BusinessStorage businessFiles) {
		this.businessFiles = businessFiles;
	}
	public int getNumber() {
		return number;
	}
	public BasicBusiness(int number, String name, int id, BusinessStorage businessFiles) {
		super();
		this.number = number;
		this.name = name;
		this.id = id;
		this.businessFiles = businessFiles;
	}
	public BasicBusiness(int number, String name, int id) {
		super();
		this.number = number;
		this.name = name;
		this.id = id;
		this.businessFiles = new BusinessStorage();
	}
	@Override
	public String toString() {
		return this.getNumber() + " - " + this.getName();
		
	}
	


	
	
	
}
