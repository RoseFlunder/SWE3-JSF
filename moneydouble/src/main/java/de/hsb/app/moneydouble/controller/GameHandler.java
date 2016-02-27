package de.hsb.app.moneydouble.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.primefaces.context.RequestContext;

import de.hsb.app.moneydouble.model.Benutzer;
import de.hsb.app.moneydouble.model.RollResult;
import de.hsb.app.moneydouble.model.RouletteColor;
import de.hsb.app.moneydouble.model.Spielzug;

@ManagedBean
@SessionScoped
public class GameHandler implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int MAX_NUMBER = 14;
	
	@ManagedProperty("#{applicationHandler}")
	private ApplicationHandler application;
	
	@ManagedProperty("#{loginHandler}")
	private LoginHandler loginHandler;

	@PersistenceContext
	private EntityManager em;
	
	@Resource
	private UserTransaction utx;
	
	private Integer betAmount;
	
	private Integer number;
	
	private Queue<RollResult> lastRolls;
	
	public GameHandler(){
		
	}
	
	public void setApplication(ApplicationHandler application) {
		this.application = application;
	}
	
	public void setLoginHandler(LoginHandler loginHandler) {
		this.loginHandler = loginHandler;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
	@PostConstruct
	public void init(){
		lastRolls = new LinkedList<>();
		betAmount =  10;
	}

	public Queue<RollResult> getLastRolls() {
		return lastRolls;
	}

	/**
	 * @return Number between 0 and 14
	 */
	public void play(RouletteColor guess){
		System.out.println("play");
		setNumber((int) (Math.random() * (MAX_NUMBER + 1)));
		RouletteColor result = RouletteColor.getColorFromNumber(number);
		
		RollResult rr = new RollResult(result, number);
		if (lastRolls.size() >= 10)
			lastRolls.poll();
		lastRolls.offer(rr);
		
		try {
			utx.begin();
			Benutzer user = loginHandler.getUser();
			Spielzug spielzug = new Spielzug(user, betAmount, guess, result, new Date());
			em.persist(spielzug);
			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		RequestContext.getCurrentInstance().execute("spin(" + getNumber() + ")");
	}

	public Integer getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(Integer betAmount) {
		this.betAmount = betAmount;
	}

}
