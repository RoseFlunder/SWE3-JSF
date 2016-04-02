package de.hsb.app.moneydouble.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entität mit allen Informationen zu einer Kreditkarte
 */
@Entity
public class Kreditkarte implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer id;
	private Kreditkartentyp typ;
	@NotNull
	private String nummer;
	@Temporal(TemporalType.DATE) @Future @NotNull
	private Date gueltigBis;
	@NotNull @Size(min = 2, message="Minium length of two characters")
	private String inhaber;

	public Kreditkarte() {
		typ = Kreditkartentyp.MASTER;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Kreditkartentyp getTyp() {
		return typ;
	}

	public void setTyp(Kreditkartentyp typ) {
		this.typ = typ;
	}

	public String getNummer() {
		return nummer;
	}

	public void setNummer(String nummer) {
		this.nummer = nummer;
	}

	public Date getGueltigBis() {
		return gueltigBis;
	}

	public void setGueltigBis(Date gueltigBis) {
		this.gueltigBis = gueltigBis;
	}

	public String getInhaber() {
		return inhaber;
	}

	public void setInhaber(String inhaber) {
		this.inhaber = inhaber;
	}
}
