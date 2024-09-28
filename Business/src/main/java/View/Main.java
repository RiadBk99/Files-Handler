package View;

import java.io.File;

import Classes.BasicBusiness;
import Classes.BusinessStorage;
import Classes.MainBusiness;
import Model.Control;

public class Main {

	public static void main(String[] args) {
/*
		BasicCard card1 = new BasicCard("sonol", CardTypes.Supplier, 1);
		BasicCard card2 = new BasicCard("paz", CardTypes.Supplier, 2);
		BasicCard card3 = new BasicCard("delek", CardTypes.Supplier, 3);
		BasicCard card4 = new BasicCard("rudy", CardTypes.Client, 4);

		Date date = new Date();
		CardMovement movement1 = new CardMovement(22 , date , "Something", card1, 1);
		CardMovement movement2 = new CardMovement(33 , date , "win", card1, 2);
		CardMovement movement3 = new CardMovement(44 , date , "nothing", card1, 3);
		CardMovement movement4 = new CardMovement(10 , date , "lose", card1, 4);

		card1.getAllMovements().add(movement1);
		card1.getAllMovements().add(movement2);
		card2.getAllMovements().add(movement3);
		card2.getAllMovements().add(movement4);

		Control.getInstance().getCardsById().put(1, card1);
		Control.getInstance().getCardsById().put(2, card2);
		Control.getInstance().getCardsById().put(3, card3);
		Control.getInstance().getCardsById().put(4, card4);
	*/	
		
		MainBusiness b1 = new MainBusiness(1 , "Riad Bkheet - CPA", 209141282);
		MainBusiness b2 = new MainBusiness(5 , "Adam Bkheet - CPA", 123456789);

		Control.getInstance().addBusiness(b1);
		Control.getInstance().addBusiness(b2);

		
		Admin l = new Admin();
		l.setVisible(true);
		l.setLocationRelativeTo(null);
		
		
	}

}
