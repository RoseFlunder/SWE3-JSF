package de.hsb.app.moneydouble.model;

public enum RouletteColor {
	
	RED("F2463D"),
	BLACK("515557"),
	GREEN("3FBA6C");
	
	private final String statisticColor;
	
	private RouletteColor(String statisticColor){
		this.statisticColor = statisticColor;
	}
	
	public static RouletteColor getColorFromNumber(int number){
		if (number == 0)
			return GREEN;
		if (number <= 7)
			return RED;
		return BLACK;
	}
	
	public String getStatisticColor(){
		return statisticColor;
	}
	
	@Override
	public String toString(){
		return this.name();
	}

}
