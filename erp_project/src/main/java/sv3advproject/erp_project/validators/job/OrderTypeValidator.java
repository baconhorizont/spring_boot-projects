package sv3advproject.erp_project.validators.job;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sv3advproject.erp_project.models.OrderType;

import java.util.Arrays;

public class OrderTypeValidator implements ConstraintValidator<OrderTypeValid, OrderType> {

    private OrderType[] subset;

    @Override
    public boolean isValid(OrderType type, ConstraintValidatorContext constraintValidatorContext) {
        return type != null && Arrays.asList(subset).contains(type);
    }

    @Override
    public void initialize(OrderTypeValid constraintAnnotation) {
        this.subset = constraintAnnotation.anyOf();
    }
}
