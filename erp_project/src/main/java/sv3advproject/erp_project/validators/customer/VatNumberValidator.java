package sv3advproject.erp_project.validators.customer;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VatNumberValidator implements ConstraintValidator<VatNumber,String>{

    @Override
    public boolean isValid(String taxId, ConstraintValidatorContext constraintValidatorContext) {
        return containsOnlyDigits(taxId) && validateLength(taxId) && validateTaxId(taxId);
    }

    private boolean containsOnlyDigits(String taxId){
        for (char c: taxId.toCharArray()) {
            if(!Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }

    private boolean validateLength(String taxId){
        return taxId.length() == 10;
    }

    private boolean validateTaxId(String taxId) {
        int counter = 0;
        for (int i = 0; i < taxId.length()-1; i++) {
            counter += Character.getNumericValue(taxId.charAt(i)) * i;
        }
        return counter % 11 == Character.getNumericValue(taxId.charAt(taxId.length() - 1));
    }
}
