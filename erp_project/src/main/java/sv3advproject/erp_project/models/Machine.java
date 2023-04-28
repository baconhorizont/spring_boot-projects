package sv3advproject.erp_project.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "machines")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "machine_name")
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "machine_type")
    private MachineType type;
    @ManyToMany(mappedBy = "machinedOn")
    @ToString.Exclude
    private List<Job> runningJob = new ArrayList<>();
    @ManyToMany
    @JoinTable(name="machines_employees_can_use",
            joinColumns=@JoinColumn(name="machine_id"),
            inverseJoinColumns=@JoinColumn(name="employee_id"))
    @ToString.Exclude
    private List<Employee> canUse = new ArrayList<>();

    public void addEmployee(Employee employee){
        canUse.add(employee);
        employee.getCanWorkOn().add(this);
    }

    public void removeEmployee(Employee employee){
        canUse.remove(employee);
    }
}
