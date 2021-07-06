package it.uniroma3.siw.spring.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.spring.model.Visita;
import it.uniroma3.siw.spring.service.VisitaService;

@Component
public class VisitaValidator implements Validator {


	@Autowired
	private VisitaService visitaService;
	
    private static final Logger logger = LoggerFactory.getLogger(VisitaValidator.class);

	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cognome", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "data", "required");

		if (!errors.hasErrors()) {
			logger.debug("confermato: valori non nulli");
			if (this.visitaService.alreadyExists((Visita)o)) {
				logger.debug("e' un duplicato");
				errors.reject("duplicate.visita");
			}
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Visita.class.equals(aClass);
	}
}
