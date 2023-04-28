package sv3advproject.erp_project.validators.employee;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sv3advproject.erp_project.models.EmployeeQualification;

import java.util.Arrays;

public class QualificationValidator implements ConstraintValidator<Qualification, EmployeeQualification> {

    private EmployeeQualification[] subset;

    @Override
    public boolean isValid(EmployeeQualification qualification, ConstraintValidatorContext constraintValidatorContext) {
        return qualification != null && Arrays.asList(subset).contains(qualification);
    }

    @Override
    public void initialize(Qualification constraintAnnotation) {
        this.subset = constraintAnnotation.anyOf();
    }
}
