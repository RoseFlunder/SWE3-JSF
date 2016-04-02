package de.hsb.app.moneydouble.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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

	@ManagedProperty("#{loginHandler.userId}")
	private Integer userId;

	/**
	 * Aktuell eingeloggter Benutzer
	 */
	private Benutzer user;

	/**
	 * Kreditkarten Transaktionshistorie des Benutzers
	 */
	private List<KreditkartenTransaktion> transaktionen;

	public MoneyHandler() {
	}

	/**
	 * Wird aufgerufen bei jedem aktualisieren der "Money" View
	 */
	@PostConstruct
	public void init() {
		if (userId == null)
			return;

		// aktuelle Benutzer Entität laden
		user = em.find(Benutzer.class, userId);
		tmpKreditkarte = user.getKreditkarte();

		// Kreditkarten Transaktionshistorie des Benutzers laden
		TypedQuery<KreditkartenTransaktion> tq = em.createNamedQuery(KreditkartenTransaktion.FIND_BY_USER,
				KreditkartenTransaktion.class);
		tq.setParameter("user", user);
		transaktionen = tq.getResultList();
	}

	private Kreditkarte tmpKreditkarte;

	/**
	 * Neue Kreditkarte speichern
	 */
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

	/**
	 * Vorhandene Kreditkarte vom Nutzer entfernen
	 */
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

	/**
	 * Methode zum Aufladen oder Abbuchen von Credits für den aktuell
	 * eingeloggten Benutzers.
	 * 
	 * @param numCredits
	 */
	public void modifiyCredits(Integer numCredits) {
		if (user.getKreditkarte() == null)
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Creditcard", "Missing creditcard"));
		
		if (numCredits < 0 && user.getMoney() < Math.abs(numCredits))
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not enough credits"));

		try {
			// Transaktion persistieren
			utx.begin();
			KreditkartenTransaktion tr = new KreditkartenTransaktion(numCredits, new Date(), user.getKreditkarte(),
					user);
			em.persist(tr);
			transaktionen.add(0, tr);
			utx.commit();

			// Kontostand des Benutzers anpassen
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
