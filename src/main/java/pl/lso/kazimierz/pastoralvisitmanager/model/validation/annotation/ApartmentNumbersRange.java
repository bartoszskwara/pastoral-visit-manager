package pl.lso.kazimierz.pastoralvisitmanager.model.validation.annotation;

import pl.lso.kazimierz.pastoralvisitmanager.model.validation.validator.ApartmentNumberRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ApartmentNumberRangeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApartmentNumbersRange {
    String message() default "Invalid range";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
