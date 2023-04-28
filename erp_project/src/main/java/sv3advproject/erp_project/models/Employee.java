package sv3advproject.erp_project.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private EmployeeQualification qualification;
    @ManyToMany(mappedBy = "canUse")
    @ToString.Exclude
    private List<Machine> canWorkOn = new ArrayList<>();
}
