package sv3advproject.erp_project.validators.employee;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sv3advproject.erp_project.models.EmployeeQualification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = QualificationValidator.class)
public @interface Qualification {

    EmployeeQualification[] anyOf();
    String message() default "Employee qualification must be any of {anyOf}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
