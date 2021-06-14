package it.uniroma3.siw.spring.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.spring.model.Responsabile;
import it.uniroma3.siw.spring.service.ResponsabileService;

@Component
public class ResponsabileValidator implements Validator {
	
	@Autowired
	private ResponsabileService responsabileService;
	
    private static final Logger logger = LoggerFactory.getLogger(AnimaleValidator.class);

	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "required");

		if (!errors.hasErrors()) {
			logger.debug("confermato: valori non nulli");
			if (this.responsabileService.alreadyExists((Responsabile)o)) {
				logger.debug("e' un duplicato");
				errors.reject("duplicato");
			}
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Responsabile.class.equals(aClass);
	}

}
