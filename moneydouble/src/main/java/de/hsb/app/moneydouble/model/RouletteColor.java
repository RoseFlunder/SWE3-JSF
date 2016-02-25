package de.hsb.app.moneydouble.model;

public enum RouletteColor {
	
	RED,
	BLACK,
	GREEN;
	
	public static RouletteColor getColorFromNumber(int number){
		if (number == 0)
			return GREEN;
		if (number <= 7)
			return RED;
		return BLACK;
	}
	
	@Override
	public String toString(){
		return this.name();
	}

}
