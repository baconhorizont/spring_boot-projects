package sv3advproject.erp_project.validators.job;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sv3advproject.erp_project.models.JobStatus;

import java.util.Arrays;

public class JobStatusValidator implements ConstraintValidator<JobStatusValid, JobStatus> {

    private JobStatus[] subset;

    @Override
    public boolean isValid(JobStatus status, ConstraintValidatorContext constraintValidatorContext) {
        return status != null && Arrays.asList(subset).contains(status);
    }

    @Override
    public void initialize(JobStatusValid constraintAnnotation) {
        this.subset = constraintAnnotation.anyOf();
    }
}
