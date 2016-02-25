package de.hsb.app.moneydouble.controller;

import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import de.hsb.app.moneydouble.model.Benutzer;
import de.hsb.app.moneydouble.model.Rolle;

/**
 * Weiß noch nicht ob wir den brauchen, könnte nützlich sein wenn wir was machen wollen, dass die Session überdauert und
 * global für die Application gültig ist
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class ApplicationHandler {
	
	@PersistenceContext
	private EntityManager em;
	
	@Resource
	private UserTransaction utx;
	
	@PostConstruct
	public void init(){
		try {
			utx.begin();
			em.persist(new Benutzer("admin", "admin", Rolle.ADMIN, new GregorianCalendar(1990, 5, 5).getTime(), 1000));
			em.persist(new Benutzer("user", "user", Rolle.ADMIN, new GregorianCalendar(1990, 6, 6).getTime(), 500));
			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
