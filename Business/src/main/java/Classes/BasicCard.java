package Classes;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import Enums.CardTypes;

public class BasicCard extends BasicBusiness implements Serializable{
	


	private static final long serialVersionUID = 10L;
	
	private ArrayList<CardMovement> allMovements;
	private CardTypes type;
	
 	public ArrayList<CardMovement> getAllMovements() {
		return allMovements;
	}
	public void setAllMovements(ArrayList<CardMovement> allMovements) {
		this.allMovements = allMovements;
	}
	public CardTypes getCardType() {
		return type;
	}
	public void setCardType(CardTypes type) {
		this.type = type;
	}	

	
	public BasicCard(int number, String name, int id, CardTypes type) {
		super(number, name, id);
		this.allMovements = new ArrayList<>();
		this.type = type;
	}
	@Override
	public String toString() {
		return this.getNumber() + " - " + this.getName();
		
	}

	


	
	
	
}