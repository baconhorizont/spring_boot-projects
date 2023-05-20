package sv3advproject.erp_project.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String vatNumber;
    private LocalDate registrationDate;
    private Address address;
    private Currency currency;
    @OneToMany(mappedBy = "customer")
    @ToString.Exclude
    private Set<Job> jobs = new HashSet<>();
}
