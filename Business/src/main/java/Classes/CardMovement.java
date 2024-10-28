package Classes;

import java.io.Serializable;
import java.util.Date;

public class CardMovement implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;
	
	private double money;
	private Date date;
	private String details;		// פרטי תנועה
	private BasicCard match;	// חשבון נגדי
	private int number;


	public CardMovement(double money, Date date, String details, BasicCard match, int number) {
		super();
		this.money = money;
		this.date = date;
		this.details = details;
		this.match = match;
		this.number = number;
	}


	public double getMoney() {
		return money;
	}


	public void setMoney(double money) {
		this.money = money;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getDetails() {
		return details;
	}


	public void setDetails(String details) {
		this.details = details;
	}


	public BasicCard getMatch() {
		return match;
	}


	public void setMatch(BasicCard match) {
		this.match = match;
	}


	public int getNumber() {
		return number;
	}


	public void setNumber(int number) {
		this.number = number;
	}



}
