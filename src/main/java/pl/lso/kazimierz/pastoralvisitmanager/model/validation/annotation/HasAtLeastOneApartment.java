package pl.lso.kazimierz.pastoralvisitmanager.model.validation.annotation;

import pl.lso.kazimierz.pastoralvisitmanager.model.validation.validator.HasAtLeastOneApartmentValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HasAtLeastOneApartmentValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasAtLeastOneApartment {
    String message() default "Address has no apartments";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
