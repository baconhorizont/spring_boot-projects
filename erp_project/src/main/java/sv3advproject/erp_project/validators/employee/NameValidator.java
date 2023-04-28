package sv3advproject.erp_project.validators.employee;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<Name,String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return name != null && !name.isBlank() && containsDigit(name) && name.length() > 3;
    }

    private boolean containsDigit(String name){
        for (char c: name.replaceAll(" ","").toCharArray()) {
            if(!Character.isLetter(c)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void initialize(Name constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
