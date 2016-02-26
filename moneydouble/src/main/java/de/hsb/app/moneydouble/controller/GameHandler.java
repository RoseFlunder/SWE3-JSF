package de.hsb.app.moneydouble.controller;

import java.io.Serializable;
import java.util.Date;

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
	
	private RouletteColor guess;
	
	private Integer betAmount;
	
	private Integer number;
	
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
		System.out.println("postconstruct");
		betAmount =  10;
		guess = RouletteColor.RED;
	}

	/**
	 * @return Number between 0 and 14
	 */
	public void play(){
		System.out.println("play");
		setNumber((int) (Math.random() * (MAX_NUMBER + 1)));
		RouletteColor result = RouletteColor.getColorFromNumber(number);
		
		Benutzer user = loginHandler.getUser();
		
		try {
			utx.begin();
			Spielzug spielzug = new Spielzug(user, betAmount, guess, result, new Date());
			em.persist(spielzug);
			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		test();
		RequestContext.getCurrentInstance().execute("spin(" + getNumber() + ")");
	}
	
	public void test(){
		System.out.println("test " + getNumber());
	}

	public RouletteColor getGuess() {
		return guess;
	}

	public void setGuess(RouletteColor guess) {
		this.guess = guess;
	}

	public Integer getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(Integer betAmount) {
		this.betAmount = betAmount;
	}

}
