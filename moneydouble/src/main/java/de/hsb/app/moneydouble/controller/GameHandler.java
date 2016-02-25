package de.hsb.app.moneydouble.controller;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

@ManagedBean
@SessionScoped
public class GameHandler {
	
	private static final int MAX_NUMBER = 14;
	
	@ManagedProperty("#{applicationHandler}")
	private ApplicationHandler application;
	
	@PersistenceContext
	private EntityManager em;
	
	@Resource
	private UserTransaction utx;
	
	public void setApplication(ApplicationHandler application) {
		this.application = application;
	}

	/**
	 * @return Number between 0 and 14
	 */
	public int play(){
		return (int) (Math.random() * (MAX_NUMBER + 1));
	}

}
