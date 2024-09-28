package Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MainBusiness extends BasicBusiness{
	
	private HashMap<Integer,BasicCard> businessCards;
	
	
	public boolean addBusinessCard(BasicCard card) {
		if(businessCards.containsKey(card.getNumber()))
			return false;
		else
			businessCards.put(card.getNumber(), card);
		
		return (businessCards.containsKey(card.getNumber()));
	}
	

	public MainBusiness(int number, String name, int id, BusinessStorage businessFiles,
			HashMap<Integer,BasicCard> businessCards) {
		super(number, name, id, businessFiles);
		this.businessCards = businessCards;
	}
	
	public MainBusiness(int number, String name, int id) {
		super(number, name, id);
		this.businessCards = new HashMap<>();
	}

	public HashMap<Integer,BasicCard> getBusinessCards() {
		return businessCards;
	}

	public void setBusinessCards(HashMap<Integer,BasicCard> businessCards) {
		this.businessCards = businessCards;
	}

	
	
}
