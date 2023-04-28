package sv3advproject.erp_project.validators.machine;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sv3advproject.erp_project.models.MachineType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = MachineTypeValidator.class)
public @interface MachineTypeValid {

    MachineType[] anyOf();
    String message() default "Machine type must be any of {anyOf}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
