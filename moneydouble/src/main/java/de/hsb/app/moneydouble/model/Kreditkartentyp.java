package de.hsb.app.moneydouble.model;

/**
 * Enum für Kreditkartentypen
 */
public enum Kreditkartentyp {

	MASTER("MasterCard"),
	VISA("Visa"),
	AMEX("AmericanExpress");
	
	private final String label;
	
	private Kreditkartentyp(String label) {
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}
	
}
