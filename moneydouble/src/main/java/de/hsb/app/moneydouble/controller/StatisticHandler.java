package de.hsb.app.moneydouble.controller;

import java.io.Serializable;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import de.hsb.app.moneydouble.model.Benutzer;
import de.hsb.app.moneydouble.model.RouletteColor;
import de.hsb.app.moneydouble.model.Spielzug;

/**
 * Handler für die Statistikseiten
 */
@ManagedBean
@ViewScoped
public class StatisticHandler implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Liste mit der Spielzughistorie des aktuellen Benutzers
	 */
	private List<Spielzug> spielzuege;

	/**
	 * Id des aktuell eingeloggten Benutzers
	 */
	@ManagedProperty("#{loginHandler.userId}")
	private Integer userId;

	@PersistenceContext
	private EntityManager em;

	/**
	 * Chart Model für Statistik welche Farben vom Nutzer gewählt wurden
	 */
	private PieChartModel userPieModel;

	/**
	 * Chart Model für Statistik welche Farben als Ergebnis gewürfelt wurden
	 */
	private PieChartModel resultPieModel;

	/**
	 * Chart Model für kummulierte Gewinne
	 */
	private LineChartModel winningsLineModel;

	@PostConstruct
	public void init() {
		if (userId == null)
			return;
		Benutzer b = em.find(Benutzer.class, userId);

		TypedQuery<Spielzug> tq = em.createNamedQuery(Spielzug.FIND_BY_USER, Spielzug.class);
		tq.setParameter("user", b);
		setSpielzuege(tq.getResultList());

		initDistributionPieModel(b, userPieModel = new PieChartModel(), "My guess distribution",
				Spielzug.COUNT_GUESS_DISTRIBUTION_BY_USER);
		initDistributionPieModel(b, resultPieModel = new PieChartModel(), "Results distribution",
				Spielzug.COUNT_RESULT_DISTRIBUTION_BY_USER);

		initWinningsLineModel();
	}

	private void initWinningsLineModel() {
		winningsLineModel = new LineChartModel();
		LineChartSeries series = new LineChartSeries();

		ListIterator<Spielzug> listIterator = spielzuege.listIterator(spielzuege.size());

		int pos = 0;
		int currentWinnings = 0;
		series.set(pos++, currentWinnings);
		while (listIterator.hasPrevious()) {
			Spielzug spielzug = listIterator.previous();
			currentWinnings += spielzug.getGuess().equals(spielzug.getResult()) ? spielzug.getMoneyAmount()
					: -spielzug.getMoneyAmount();
			
			series.set(pos++, currentWinnings);
		}

		winningsLineModel.addSeries(series);
		winningsLineModel.setTitle("Cummulative Profit");
		winningsLineModel.getAxis(AxisType.X).setMin(0);
		winningsLineModel.getAxis(AxisType.X).setTickInterval("1");
		winningsLineModel.getAxis(AxisType.X).setLabel("Game");
		
		winningsLineModel.getAxis(AxisType.Y).setLabel("Profit");
	}

	private void initDistributionPieModel(Benutzer b, PieChartModel model, String title, String query) {
		model.setTitle(title);
		model.setShowDataLabels(true);

		String seriesColors = new String();
		// Query um die Statistik der vom Benutzer gewählten Farben aufzubauen
		Query q = em.createNamedQuery(query);
		q.setParameter("user", b);
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = q.getResultList();
		for (Object[] line : resultList) {
			RouletteColor color = (RouletteColor) line[0];
			model.set(color.toString(), (Number) line[1]);
			seriesColors += (color.getStatisticColor() + ",");
		}

		// Chart Model mit Farben versehen
		if (!seriesColors.isEmpty()) {
			seriesColors = seriesColors.substring(0, seriesColors.length() - 1);
			model.setSeriesColors(seriesColors);
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

	public PieChartModel getResultPieModel() {
		return resultPieModel;
	}

	public void setResultPieModel(PieChartModel resultPieModel) {
		this.resultPieModel = resultPieModel;
	}

	public LineChartModel getWinningsLineModel() {
		return winningsLineModel;
	}

	public void setWinningsLineModel(LineChartModel winningsLineModel) {
		this.winningsLineModel = winningsLineModel;
	}
	
	

}
