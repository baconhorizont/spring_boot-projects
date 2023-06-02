package sv3advproject.erp_project.dtos.customer_dto;

import lombok.Builder;
import lombok.Data;
import sv3advproject.erp_project.models.Currency;

import java.time.LocalDate;

@Data
@Builder
public class CreateCustomerCommand {

    private String name;
    private String vatNumber;
    private LocalDate registrationDate;
    private String country;
    private String postalCode;
    private String city;
    private String street;
    private String streetNumber;
    private Currency currency;
}
