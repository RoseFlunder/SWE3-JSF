package de.hsb.app.moneydouble.controller;

import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import de.hsb.app.moneydouble.model.Benutzer;
import de.hsb.app.moneydouble.model.Rolle;

/**
 * Hander der über die ganze Lebensdauer der Application gültig ist.
 * Dient dazu um zu prüfen, ob bereits Demo Nutzer in der Datenbank vorhanden sind und fügt
 * diese hinzu falls nicht.
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
			TypedQuery<Benutzer> q = em.createNamedQuery(Benutzer.FIND_ADMIN_USER, Benutzer.class);
			q.getSingleResult();
			System.out.println("admin user already exists");
		} catch (NoResultException e) {
			System.out.println("add admin user");
			try {
				utx.begin();
				em.persist(new Benutzer("admin", "admin", Rolle.ADMIN, new GregorianCalendar(1990, 5, 5).getTime(), 1000));
				em.persist(new Benutzer("user", "user", Rolle.BENUTZER, new GregorianCalendar(1990, 6, 6).getTime(), 500));
				utx.commit();
			} catch (Exception e2) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
