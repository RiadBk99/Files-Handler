package View;

import java.io.File;

import Classes.BasicBusiness;
import Classes.BusinessStorage;
import Classes.MainBusiness;
import Model.Control;
import View.SerializationHelper;

public class Main {

	public static void main(String[] args) {
		

		
		Control myBusinessFromFile = SerializationHelper.loadMyBusiness();
		if(myBusinessFromFile!=null) {
			Control.setInstance(myBusinessFromFile);
		}
		Control.getInstance();
		Admin l = new Admin();
		l.setVisible(true);
		l.setLocationRelativeTo(null);
		SerializationHelper.saveMyBusiness(Control.getInstance());


		
		
	}

}
