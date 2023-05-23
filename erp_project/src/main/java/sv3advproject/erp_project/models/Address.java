package sv3advproject.erp_project.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    private String country;
    @Column(name = "postal_code")
    private String postalCode;
    private String city;
    private String street;
    @Column(name = "street_number")
    private String streetNumber;
}
