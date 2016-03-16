package de.hsb.app.moneydouble.controller;

import java.io.Serializable;
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

@ManagedBean
@SessionScoped
public class LoginHandler implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private Benutzer user;

	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction utx;
	
	private Queue<RollResult> lastRolls;

	@PostConstruct
	public void init() {
		lastRolls = new LinkedList<>();
	}

	public String register() {
		try {
			utx.begin();
			Benutzer newUser = new Benutzer(username, password, Rolle.BENUTZER, new Date(), 500);
			em.persist(newUser);
			utx.commit();
			return "/login.xhtml?faces-redirect=true";
		} catch (Exception e) {
		}

		return null;
	}

	public String login() {
		TypedQuery<Benutzer> q = em.createNamedQuery(Benutzer.GET_USER_LOGIN, Benutzer.class);
		q.setParameter("username", username);
		q.setParameter("password", password);

		try {
			user = q.getSingleResult();
			utx.begin();
			user.setMoney(user.getMoney() + 1000);
//			em.merge(user);
			utx.commit();
			
			return "/index.jsf?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong credentials", "Wrong username or password"));
		}

		return null;
	}

	public void checkLoggedIn(ComponentSystemEvent cse) {
		FacesContext context = FacesContext.getCurrentInstance();

		if (user == null) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,
					"/login.jsf?faces-redirect=true");
		}
	}

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

	public Benutzer getUser() {
		return user;
	}

	public void setUser(Benutzer user) {
		this.user = user;
	}
	
	public Queue<RollResult> getLastRolls() {
		return lastRolls;
	}

	public void setLastRolls(Queue<RollResult> lastRolls) {
		this.lastRolls = lastRolls;
	}
}
