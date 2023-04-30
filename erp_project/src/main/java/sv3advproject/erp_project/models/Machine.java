package sv3advproject.erp_project.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "machines")
@Getter
@Setter
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
    private Set<Job> runningJob = new TreeSet<>();
    @ManyToMany
    @JoinTable(name="machines_employees_can_use",
            joinColumns=@JoinColumn(name="machine_id"),
            inverseJoinColumns=@JoinColumn(name="employee_id"))
    @ToString.Exclude
    private Set<Employee> canUse = new HashSet<>();

    public void addEmployee(Employee employee){
        canUse.add(employee);
        employee.getCanWorkOn().add(this);
    }

    public void removeEmployee(Employee employee){
        canUse.remove(employee);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Machine machine = (Machine) o;
        return id.equals(machine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
