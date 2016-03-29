package de.hsb.app.moneydouble.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entität die alle Informationen zu einer getätigen Transaktion mit einer Kreditkarte abspeichert
 */
@Entity
@NamedQueries({
		@NamedQuery(name = KreditkartenTransaktion.FIND_BY_USER, query = "SELECT t FROM KreditkartenTransaktion t WHERE t.user = :user ORDER BY t.timestamp DESC") })
public class KreditkartenTransaktion implements Serializable {

	public static final String FIND_BY_USER = "findByUser";

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer cents;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	@ManyToOne
	private Kreditkarte kreditkarte;

	@ManyToOne
	private Benutzer user;

	public KreditkartenTransaktion() {
		super();
	}

	public KreditkartenTransaktion(Integer cents, Date timestamp, Kreditkarte kreditkarte, Benutzer user) {
		super();
		this.cents = cents;
		this.timestamp = timestamp;
		this.kreditkarte = kreditkarte;
		this.user = user;
	}

	public Kreditkarte getKreditkarte() {
		return kreditkarte;
	}

	public void setKreditkarte(Kreditkarte kreditkarte) {
		this.kreditkarte = kreditkarte;
	}

	public Integer getCents() {
		return cents;
	}

	public void setCents(Integer cents) {
		this.cents = cents;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getId() {
		return id;
	}

	public Benutzer getUser() {
		return user;
	}

	public void setUser(Benutzer user) {
		this.user = user;
	}
	
	public String getEuro(){
		String c = String.valueOf(cents);
		return c.substring(0, c.length() - 2) + "," + c.substring(c.length() - 2, c.length());
	}

}
