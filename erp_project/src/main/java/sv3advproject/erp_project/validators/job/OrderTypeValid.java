package sv3advproject.erp_project.validators.job;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sv3advproject.erp_project.models.OrderType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = OrderTypeValidator.class)
public @interface OrderTypeValid {

    OrderType[] anyOf();
    String message() default "Order type must be any of {anyOf}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
