package de.hsb.app.moneydouble.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import de.hsb.app.moneydouble.model.Benutzer;
import de.hsb.app.moneydouble.model.RouletteColor;
import de.hsb.app.moneydouble.model.Spielzug;

@ManagedBean
@SessionScoped
public class GameHandler {
	
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
	
	public void setApplication(ApplicationHandler application) {
		this.application = application;
	}
	
	public void setLoginHandler(LoginHandler loginHandler) {
		this.loginHandler = loginHandler;
	}

	/**
	 * @return Number between 0 and 14
	 */
	public int play(){
		number = (int) (Math.random() * (MAX_NUMBER + 1));
		RouletteColor result = RouletteColor.getColorFromNumber(number);
		
		Benutzer user = loginHandler.getUser();
		
		Spielzug spielzug = new Spielzug(user, betAmount, guess, result, new Date());
		em.persist(spielzug);
		
		debugSpielzuege();
		
		return number;
	}
	
	private void debugSpielzuege(){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Spielzug> cq = cb.createQuery(Spielzug.class);
		Root<Spielzug> spielzug = cq.from(Spielzug.class);
		cq.select(spielzug);
		
		TypedQuery<Spielzug> q = em.createQuery(cq);
		List<Spielzug> resultList = q.getResultList();
		
//		Query query = em.createQuery("select s from spielzug s");
//		List<Spielzug> resultList = query.getResultList();
		
		for (Spielzug s : resultList) {
			System.out.println(s);
		}
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
