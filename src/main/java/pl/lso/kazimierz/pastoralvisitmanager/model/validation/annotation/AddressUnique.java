package pl.lso.kazimierz.pastoralvisitmanager.model.validation.annotation;

import pl.lso.kazimierz.pastoralvisitmanager.model.validation.validator.AddressUniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = AddressUniqueValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AddressUnique {
    String message() default "Address already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
