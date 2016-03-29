package de.hsb.app.moneydouble.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.Queue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.primefaces.context.RequestContext;

import de.hsb.app.moneydouble.model.Benutzer;
import de.hsb.app.moneydouble.model.RollResult;
import de.hsb.app.moneydouble.model.RouletteColor;
import de.hsb.app.moneydouble.model.Spielzug;

/**
 * Handler für die Hauptseite zum Spielen.
 */
@ManagedBean
@ViewScoped
public class GameHandler implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int MAX_NUMBER = 14;
	
	@ManagedProperty("#{loginHandler.userId}")
	private Integer userId;
	
	@ManagedProperty("#{loginHandler.lastRolls}")
	private Queue<RollResult> lastRolls;

	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction utx;
	
	/**
	 * Aktuell eingeloggter Benutzers
	 */
	private Benutzer user;

	/**
	 * Einsatz des Benutzers
	 */
	private Integer betAmount;

	/**
	 * Aktuelle gewürfeltes Spielergenis
	 */
	private Integer number;

	/**
	 * Flag ob die Animation auf der Hauptseite aktiv ist.
	 */
	private boolean animationRunning;

	public GameHandler() {

	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@PostConstruct
	public void init() {
		//Entity des aktuelle eingeloggten Benutzers laden
		if (userId != null)
			user = em.find(Benutzer.class, userId);
		
		betAmount = 10;
	}

	/**
	 * Methode die von den Buttons zum Spielen aufgerufen wird.
	 * Es wird die gewählte Farbe übergeben und eine Zufallszahl zwischen 0 und 14 gewürfelt.
	 * Der Kontostand des Nutzers wird je nach Ausgang modifiziert und die JavaScript Animation gestartet.
	 */
	public void play(RouletteColor guess) {
		//Roulette Ergebnis
		setNumber((int) (Math.random() * (MAX_NUMBER + 1)));
		RouletteColor color = RouletteColor.getColorFromNumber(number);

		//Füge aktuelles Ergebnis der temporären Historie auf der Hauptseite hinzu
		RollResult rr = new RollResult(color, number);
		while (lastRolls.size() >= 10)
			lastRolls.poll();
		lastRolls.offer(rr);

		try {
			utx.begin();
			//Kontostand des Benutzers anpassen
			user.setMoney(user.getMoney() + (guess.equals(color)
					? (RouletteColor.GREEN.equals(color) ? betAmount * 14 : betAmount) : -betAmount));
			user = em.merge(user);
			//Spielzug persistieren für die Statistik
			Spielzug spielzug = new Spielzug(user, betAmount, guess, color, new Date());
			em.persist(spielzug);
			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Animation starten
		setAnimationRunning(true);
		RequestContext.getCurrentInstance().execute("spin(" + getNumber() + ")");
	}

	public Integer getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(Integer betAmount) {
		if (betAmount <= 0)
			this.betAmount = 1;
		else if (betAmount <= user.getMoney())
			this.betAmount = betAmount;
		else
			this.betAmount = user.getMoney();
	}
	
	public void multiplyBetAmount(Double factor){
		setBetAmount((int) (getBetAmount() * factor));
	}
	
	public void addToBetAmount(Integer num){
		setBetAmount(getBetAmount() + num);
	}

	public boolean isAnimationRunning() {
		return animationRunning;
	}

	public void setAnimationRunning(boolean animationRunning) {
		this.animationRunning = animationRunning;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
}
