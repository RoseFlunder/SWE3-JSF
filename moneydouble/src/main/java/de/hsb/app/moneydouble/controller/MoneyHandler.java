package de.hsb.app.moneydouble.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import de.hsb.app.moneydouble.model.Benutzer;
import de.hsb.app.moneydouble.model.Kreditkarte;
import de.hsb.app.moneydouble.model.KreditkartenTransaktion;
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

	private List<KreditkartenTransaktion> transaktionen;

	public MoneyHandler() {
	}

	@PostConstruct
	public void init() {
		tmpKreditkarte = user.getKreditkarte();

		TypedQuery<KreditkartenTransaktion> tq = em.createNamedQuery(KreditkartenTransaktion.FIND_BY_USER,
				KreditkartenTransaktion.class);
		tq.setParameter("user", user);

		transaktionen = tq.getResultList();
	}

	private Kreditkarte tmpKreditkarte;

	public void saveCreditcard() {
		try {
			utx.begin();
			user.setKreditkarte(tmpKreditkarte);

			user = em.merge(user);
			tmpKreditkarte = user.getKreditkarte();
			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteCreditcard() {
		try {
			utx.begin();
			tmpKreditkarte = null;
			user.setKreditkarte(null);

			user = em.merge(user);
			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buyCredits(Integer numCredits) {
		try {
			utx.begin();

			KreditkartenTransaktion tr = new KreditkartenTransaktion(numCredits, new Date(), user.getKreditkarte(),
					user);
			em.persist(tr);
			transaktionen.add(0, tr);
			utx.commit();
			
			utx.begin();

			user.setMoney(user.getMoney() + numCredits);
			user = em.merge(user);

			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createNewCreditcard() {
		tmpKreditkarte = new Kreditkarte();
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

	public List<KreditkartenTransaktion> getTransaktionen() {
		return transaktionen;
	}

	public void setTransaktionen(List<KreditkartenTransaktion> transaktionen) {
		this.transaktionen = transaktionen;
	}

}
