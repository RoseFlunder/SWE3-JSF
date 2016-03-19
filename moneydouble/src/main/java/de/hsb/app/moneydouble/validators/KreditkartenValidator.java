package de.hsb.app.moneydouble.validators;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "kreditkartenValidator")
public class KreditkartenValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String kknr = String.valueOf(value);
		List<Integer> kkintarray = new ArrayList<>();
		
		FacesMessage message;
		
		for (int i = 0; i < kknr.length(); i++)
			kkintarray.add(kknr.charAt(i) - 48);
		
		if(! kknr.matches("[0-9]+")){
			message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Ungültige Kreditkartennummer",
							"Es dürfen nur Ziffern enthalten sein!");
			throw new ValidatorException(message);
		} else if (kknr.length() < 12 || kknr.length() > 16){
			message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Ungültige Kreditkartennummer",
					"Nummer ist zu lang oder zu kurz!");
			throw new ValidatorException(message);
		} else if (!isValid(kkintarray)){
			message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Ungültige Kreditkartennummer",
					"Die Kreditkartennummer ist ungültig!");
			throw new ValidatorException(message);
		}
	}
	
	private boolean isValid(List<Integer> digits){
		int sum = 0;
		int length = digits.size();
		for (int i = 0; i < length; i++) {
			Integer digit = digits.get(length-i-1);
			//jede 2. Ziffer multipliziert mit 2
			if (i % 2 == 1) digit *= 2;
			sum += digit > 9 ? digit - 9 : digit;
		}
		return sum % 10 == 0;
	}

}
