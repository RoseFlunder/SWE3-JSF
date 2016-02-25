package de.hsb.app.moneydouble.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import de.hsb.app.moneydouble.model.Benutzer;
import de.hsb.app.moneydouble.model.Spielzug;

@ManagedBean
@ViewScoped
public class StatisticHandler implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Spielzug> spielzuege;
	
	@ManagedProperty("#{loginHandler}")
	private LoginHandler loginHandler;
	
	public void setLoginHandler(LoginHandler loginHandler) {
		this.loginHandler = loginHandler;
	}

	@PersistenceContext
	private EntityManager em;

	@PostConstruct
	public void init(){
		Benutzer b = loginHandler.getUser();
		
		TypedQuery<Spielzug> q = em.createNamedQuery("Spielzug.findByUser", Spielzug.class);
		q.setParameter("user", b);
		
		setSpielzuege(q.getResultList());
		
		for (Spielzug spielzug : spielzuege) {
			System.out.println(spielzug);
		}
	}

	public List<Spielzug> getSpielzuege() {
		return spielzuege;
	}

	public void setSpielzuege(List<Spielzug> spielzuege) {
		this.spielzuege = spielzuege;
	}
	
}
