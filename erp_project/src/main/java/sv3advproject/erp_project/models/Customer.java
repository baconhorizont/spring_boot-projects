package sv3advproject.erp_project.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
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
    @Column(name = "vat_number")
    private String vatNumber;
    @Column(name = "registration_date")
    private LocalDate registrationDate;
    private Address address;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @OneToMany(mappedBy = "customer")
    @ToString.Exclude
    private Set<Job> jobs = new HashSet<>();

    public void addJob(Job job){
        jobs.add(job);
        job.setCustomer(this);
    }

    public void removeJob(Job job){
        jobs.remove(job);
        job.setCustomer(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
