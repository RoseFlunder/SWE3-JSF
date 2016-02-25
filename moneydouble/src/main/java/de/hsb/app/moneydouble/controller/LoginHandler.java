package de.hsb.app.moneydouble.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.RollbackException;
import javax.transaction.UserTransaction;

import de.hsb.app.moneydouble.model.Benutzer;
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

	@PostConstruct
	public void init() {

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

	public void checkUsernameAvailable() {
		TypedQuery<Benutzer> q = em.createNamedQuery(Benutzer.FIND_USER_BY_NAME, Benutzer.class);
		q.setParameter("username", username);

		if (!q.getResultList().isEmpty())
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username is already in use"));
	}

	public String login() {
		Query query = em
				.createQuery("select b from Benutzer b " + "where b.username = :username and b.password = :password ");

		query.setParameter("username", username);
		query.setParameter("password", password);

		System.out.println(username + " " + password);

		@SuppressWarnings("unchecked")
		List<Benutzer> benutzer = query.getResultList();

		if (benutzer.size() == 1) {
			user = benutzer.get(0);
			return "/index.xhtml?faces-redirect=true";
		}

		return null;
	}

	public void checkLoggedIn(ComponentSystemEvent cse) {
		FacesContext context = FacesContext.getCurrentInstance();

		if (user == null) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,
					"/login.xhtml?faces-redirect=true");
		}
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/index.xhtml?faces-redirect=true";
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
}
