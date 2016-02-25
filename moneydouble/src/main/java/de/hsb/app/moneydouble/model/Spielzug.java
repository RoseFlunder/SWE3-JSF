package de.hsb.app.moneydouble.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Spielzug implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private Benutzer user;
	private Integer moneyAmount;
	private RouletteColor guess;
	private RouletteColor result;
	private Date timestamp;
	
	public Spielzug(){
		
	}
	
	public Spielzug(Benutzer user, Integer moneyAmount, RouletteColor guess, RouletteColor result, Date timestamp) {
		super();
		this.user = user;
		this.moneyAmount = moneyAmount;
		this.guess = guess;
		this.result = result;
		this.timestamp = timestamp;
	}
	
	public Benutzer getUser() {
		return user;
	}
	public void setUser(Benutzer user) {
		this.user = user;
	}
	public Integer getMoneyAmount() {
		return moneyAmount;
	}
	public void setMoneyAmount(Integer moneyAmount) {
		this.moneyAmount = moneyAmount;
	}
	public RouletteColor getGuess() {
		return guess;
	}
	public void setGuess(RouletteColor guess) {
		this.guess = guess;
	}
	public RouletteColor getResult() {
		return result;
	}
	public void setResult(RouletteColor result) {
		this.result = result;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Spielzug [id=" + id + ", user=" + user.getUsername() + ", moneyAmount=" + moneyAmount + ", guess=" + guess
				+ ", result=" + result + ", timestamp=" + timestamp + "]";
	}

}
