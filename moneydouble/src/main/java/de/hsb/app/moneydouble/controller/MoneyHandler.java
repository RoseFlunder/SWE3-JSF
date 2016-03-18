package de.hsb.app.moneydouble.controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import de.hsb.app.moneydouble.model.Benutzer;
import de.hsb.app.moneydouble.model.Kreditkarte;
import de.hsb.app.moneydouble.model.Kreditkartentyp;

@ManagedBean
@ViewScoped
public class MoneyHandler {

	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction utx;

	@ManagedProperty("#{loginHandler.user}")
	private Benutzer user;

	public MoneyHandler() {
	}
	
	@PostConstruct
	public void init() {
		tmpKreditkarte = user.getKreditkarte() != null ? user.getKreditkarte() : new Kreditkarte();
	}

	private Kreditkarte tmpKreditkarte;

	public void saveCreditcard() {
		try {
			utx.begin();
			
			
			if (user.getKreditkarte() == null)
				user.setKreditkarte(tmpKreditkarte);
			
			em.merge(user);
			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Kreditkarte getTmpKreditkarte() {
		return tmpKreditkarte;
	}

	public void setTmpKreditkarte(Kreditkarte tmpKreditkarte) {
		this.tmpKreditkarte = tmpKreditkarte;
	}

	public Kreditkartentyp[] getKreditkartentypValues() {
		return Kreditkartentyp.values();
	}

	public Benutzer getUser() {
		return user;
	}

	public void setUser(Benutzer user) {
		this.user = user;
	}

}
