package sv3advproject.erp_project.dtos.customer_dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import sv3advproject.erp_project.models.Currency;
import sv3advproject.erp_project.validators.customer.CurrencyValid;
import sv3advproject.erp_project.validators.customer.VatNumber;

import java.time.LocalDate;

@Data
@Builder
public class CreateCustomerCommand {
    @NotBlank(message = "Customer name must not blank")
    @Size(min = 3,message = "Customer name size must be greater than 2")
    private String name;
    @VatNumber(message = "Invalid vat number!")
    private String vatNumber;
    @PastOrPresent(message = "Registration date must be past or present!")
    private LocalDate registrationDate;
    @NotBlank(message = "Customer country must not blank")
    @Pattern(regexp = "^[a-zA-Z ÁÉÍÓÖŐÚÜŰáéíóöőúüű]+$",message = "Invalid country name!")
    private String country;
    @Digits(integer = 8, fraction = 0,message = "Invalid postal code!")
    private String postalCode;
    @NotBlank(message = "Customer city must not blank")
    @Pattern(regexp = "^[a-zA-Z ÁÉÍÓÖŐÚÜŰáéíóöőúüű]+$",message = "Invalid city name!")
    private String city;
    @NotBlank(message = "Customer street must not blank")
    @Pattern(regexp = "^[a-zA-Z ÁÉÍÓÖŐÚÜŰáéíóöőúüű0-9]+$",message = "Invalid street name!")
    private String street;
    @Pattern(regexp = "^[a-zA-Z0-9]+$",message = "Invalid street number!")
    private String streetNumber;
    @CurrencyValid(anyOf = {Currency.HUF, Currency.EUR})
    private sv3advproject.erp_project.models.Currency currency;
}
