package sv3advproject.erp_project.validators.customer;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class CurrencyValidator implements ConstraintValidator<CurrencyValid, sv3advproject.erp_project.models.Currency> {

    private sv3advproject.erp_project.models.Currency[] subset;

    @Override
    public boolean isValid(sv3advproject.erp_project.models.Currency currency, ConstraintValidatorContext constraintValidatorContext) {
        return currency != null && Arrays.asList(subset).contains(currency);
    }

    @Override
    public void initialize(CurrencyValid constraintAnnotation) {
        this.subset = constraintAnnotation.anyOf();
    }
}
