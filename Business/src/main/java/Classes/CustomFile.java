package Classes;

import java.io.File;
import java.io.Serializable;

public class CustomFile extends File implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;

	public CustomFile(String pathname) {
		super(pathname);
	}
	
	public CustomFile(File file) {
		super(file.getPath());
	}

	@Override
	public String toString() {
		return this.getName();
	}
	
	
	
	

}
