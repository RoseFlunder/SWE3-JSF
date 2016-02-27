package de.hsb.app.moneydouble.model;

public class RollResult {
	
	private RouletteColor color;
	private Integer number;
	
	public RollResult(RouletteColor color, Integer number) {
		super();
		this.color = color;
		this.number = number;
	}
	
	public RouletteColor getColor() {
		return color;
	}
	public void setColor(RouletteColor color) {
		this.color = color;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
}
