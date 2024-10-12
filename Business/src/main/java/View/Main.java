package View;

import java.io.File;

import Classes.BasicBusiness;
import Classes.BusinessStorage;
import Classes.MainBusiness;
import Model.Control;

public class Main {

	public static void main(String[] args) {
		
		MainBusiness b1 = new MainBusiness(1 , "Riad Bkheet - CPA", 209141282);
		MainBusiness b2 = new MainBusiness(5 , "Adam Bkheet - CPA", 123456789);

		Control.getInstance().addBusiness(b1);
		Control.getInstance().addBusiness(b2);

		
		Admin l = new Admin();
		l.setVisible(true);
		l.setLocationRelativeTo(null);
		
		
	}

}
