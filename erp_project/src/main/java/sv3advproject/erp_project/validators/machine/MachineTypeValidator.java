package sv3advproject.erp_project.validators.machine;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sv3advproject.erp_project.models.MachineType;

import java.util.Arrays;

public class MachineTypeValidator implements ConstraintValidator<MachineTypeValid, MachineType> {

    private MachineType[] subset;

    @Override
    public boolean isValid(MachineType type, ConstraintValidatorContext constraintValidatorContext) {
        return type != null && Arrays.asList(subset).contains(type);
    }

    @Override
    public void initialize(MachineTypeValid constraintAnnotation) {
        this.subset = constraintAnnotation.anyOf();
    }
}
