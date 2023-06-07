package sv3advproject.erp_project.validators.customer;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = VatNumberValidator.class)
public @interface VatNumber {

    String message() default "Invalid tax id!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
