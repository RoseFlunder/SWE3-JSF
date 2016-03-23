package de.hsb.app.moneydouble.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.primefaces.model.chart.PieChartModel;

import de.hsb.app.moneydouble.model.Benutzer;
import de.hsb.app.moneydouble.model.RouletteColor;
import de.hsb.app.moneydouble.model.Spielzug;

@ManagedBean
@ViewScoped
public class StatisticHandler implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Spielzug> spielzuege;
	
	@ManagedProperty("#{loginHandler.userId}")
	private Integer userId;

	@PersistenceContext
	private EntityManager em;
	
	private PieChartModel userPieModel;

	@PostConstruct
	public void init(){
		if (userId == null)
			return;
		Benutzer b = em.find(Benutzer.class, userId);
		
		TypedQuery<Spielzug> tq = em.createNamedQuery(Spielzug.FIND_BY_USER, Spielzug.class);
		tq.setParameter("user", b);
		setSpielzuege(tq.getResultList());
		
		userPieModel = new PieChartModel();
		userPieModel.setTitle("My guesses");
		userPieModel.setShowDataLabels(true);
		
		
		String seriesColors = new String();
		
		Query q = em.createNamedQuery(Spielzug.COUNT_GUESS_DISTRIBUTION_BY_USER);
		q.setParameter("user", b);
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = q.getResultList();
		for (Object[] line : resultList) {
			RouletteColor color = (RouletteColor) line[0];
			userPieModel.set(color.toString(), (Number) line[1]);
			seriesColors += (color.getStatisticColor() + ",");
		}
		
		if (!seriesColors.isEmpty()){
			seriesColors = seriesColors.substring(0, seriesColors.length() - 1);
			userPieModel.setSeriesColors(seriesColors);
		}		
	}

	public List<Spielzug> getSpielzuege() {
		return spielzuege;
	}

	public void setSpielzuege(List<Spielzug> spielzuege) {
		this.spielzuege = spielzuege;
	}

	public PieChartModel getUserPieModel() {
		return userPieModel;
	}

	public void setUserPieModel(PieChartModel userPieModel) {
		this.userPieModel = userPieModel;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
