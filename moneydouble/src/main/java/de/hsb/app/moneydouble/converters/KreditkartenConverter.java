package de.hsb.app.moneydouble.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Entfernt alle Leerzeichen und Bindestriche aus dem übergeben Text
 */
@FacesConverter("kreditkartenConverter")
public class KreditkartenConverter implements Converter {

	public KreditkartenConverter() {
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		if (value == null) return null;
		String s = value;
		if (value.contains(" "))
			s = value.replace(" ", "");
		if (value.contains("-"))
			s = value.replace("-", "");
		return s;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		return (String) value;
	}

}
