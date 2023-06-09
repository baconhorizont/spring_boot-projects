package sv3advproject.erp_project.dtos.customer_dto;

import lombok.Data;
import sv3advproject.erp_project.models.Address;
import sv3advproject.erp_project.models.Currency;

import java.time.LocalDate;

@Data
public class CustomerDto {

    private Long id;
    private String name;
    private String vatNumber;
    private LocalDate registrationDate;
    private Address address;
    private Currency currency;
}
