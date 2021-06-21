package it.uniroma3.siw.spring.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.spring.model.Area;
import it.uniroma3.siw.spring.service.AreaService;

@Component
public class AreaValidator implements Validator {

	@Autowired
	private AreaService areaService;
	
    private static final Logger logger = LoggerFactory.getLogger(AnimaleValidator.class);

	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "orario", "required");

		if (!errors.hasErrors()) {
			logger.debug("confermato: valori non nulli");
			if (this.areaService.alreadyExists((Area)o)) {
				logger.debug("e' un duplicato");
				errors.reject("duplicate.area");
			}
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Area.class.equals(aClass);
	}
}
