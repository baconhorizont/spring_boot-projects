package sv3advproject.erp_project.models;

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
    private String postalCode;
    private String city;
    private String street;
    private String streetNumber;
}
