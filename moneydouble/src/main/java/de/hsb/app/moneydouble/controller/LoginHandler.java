package de.hsb.app.moneydouble.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import de.hsb.app.moneydouble.model.Benutzer;
import de.hsb.app.moneydouble.model.RollResult;
import de.hsb.app.moneydouble.model.Rolle;

/**
 * Handler für den Login/Logout und Registrierung.
 * Speichert zusätzlich Session Variablen ab.
 */
@ManagedBean
@SessionScoped
public class LoginHandler implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Geburtsdatum für die Registrierung
	 */
	private Date geburtstag;
	
	/**
	 * Benutzername für Login/Registrierung
	 */
	private String username;
	
	
	/**
	 * Password für Login/Registierung
	 */
	private String password;

	/**
	 * Id des aktuell eingeloggten Benutzers
	 */
	private Integer userId;

	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction utx;

	/**
	 * Temporäre Ergebnisliste für die letzten 10 Ergebnisse
	 */
	private Queue<RollResult> lastRolls;

	@PostConstruct
	public void init() {
		lastRolls = new LinkedList<>();
		userId = null;
	}

	/**
	 * Fügt einen neuen Benutzer hinzu und leitet auf die Login-Seite
	 */
	public String register() {
		try {
			utx.begin();
			Benutzer newUser = new Benutzer(username, password, Rolle.BENUTZER, geburtstag, 500);
			em.persist(newUser);
			utx.commit();
			return "/login.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Versucht einen Benutzer einzuloggen und leitet bei Erfolg auf die Hauptseite.
	 */
	public String login() {
		TypedQuery<Benutzer> q = em.createNamedQuery(Benutzer.GET_USER_LOGIN, Benutzer.class);
		q.setParameter("username", username);
		q.setParameter("password", password);

		try {
			userId = q.getSingleResult().getId();
			return "/index.jsf?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong credentials", "Wrong username or password"));
		}

		return null;
	}

	/**
	 * Prüft ob ein Nutzer eingeloggt und leitet auf die Login-Seite zurück falls nicht.
	 */
	public void checkLoggedIn(ComponentSystemEvent cse) {
		FacesContext context = FacesContext.getCurrentInstance();

		if (userId == null) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,
					"/login.jsf?faces-redirect=true");
		}
	}

	/**
	 * Der aktuelle Benutzer wird ausgeloggt und es wird die Login-Seite aufgerufen
	 */
	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/login.jsf?faces-redirect=true";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getMinGeburtstag() {
		return Date.from(LocalDate.now().minusYears(18).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public Date getMaxGeburtstag() {
		return Date.from(LocalDate.now().minusYears(100).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public Date getGeburtstag() {
		return geburtstag;
	}

	public void setGeburtstag(Date geburtstag) {
		this.geburtstag = geburtstag;
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Queue<RollResult> getLastRolls() {
		return lastRolls;
	}

	public void setLastRolls(Queue<RollResult> lastRolls) {
		this.lastRolls = lastRolls;
	}
}
