package sv3advproject.erp_project.validators.customer;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sv3advproject.erp_project.models.Currency;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = CurrencyValidator.class)
public @interface CurrencyValid {

    Currency[] anyOf();
    String message() default "Currency must be any of {anyOf}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
