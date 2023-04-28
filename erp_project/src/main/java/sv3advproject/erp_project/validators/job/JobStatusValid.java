package sv3advproject.erp_project.validators.job;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sv3advproject.erp_project.models.JobStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = JobStatusValidator.class)
public @interface JobStatusValid {

    JobStatus[] anyOf();
    String message() default "Job status must be any of {anyOf}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
