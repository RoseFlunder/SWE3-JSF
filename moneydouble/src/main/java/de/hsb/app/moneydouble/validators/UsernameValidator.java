package de.hsb.app.moneydouble.validators;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import de.hsb.app.moneydouble.model.Benutzer;

/**
 * Making the validator a managed bean is common workaround in JSF 2.2 to make dependecy injection available.
 * In this case for the entity manager.
 * Will be changed for JSF 2.3
 * @see <a href="http://stackoverflow.com/questions/7572335/how-to-inject-in-facesvalidator-with-ejb-persistencecontext-inject-autow">stackoverflow.com</a>
 */
@ManagedBean
@RequestScoped
public class UsernameValidator implements Validator {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		TypedQuery<Benutzer> q = em.createNamedQuery(Benutzer.FIND_USER_BY_NAME, Benutzer.class);
		q.setParameter("username", value.toString());

		if (!q.getResultList().isEmpty()){
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username is already in use"));
		}
	}

}
