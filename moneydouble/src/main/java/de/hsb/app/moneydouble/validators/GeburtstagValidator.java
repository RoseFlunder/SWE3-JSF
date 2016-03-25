package de.hsb.app.moneydouble.validators;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "geburtstagsValidator")
public class GeburtstagValidator implements Validator {
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		LocalDate date = ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate minDate = LocalDate.now().minusYears(18);
		FacesMessage message;
		
		if(date.isAfter(minDate)){
			message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Ungültiges Geburtsdatum",
					"Sie müssen mindestens 18 Jahre alt sein!");
			throw new ValidatorException(message);
		}
	}
}
