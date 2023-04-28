package sv3advproject.erp_project.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "jobs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customer;
    @Column(name = "order_date")
    private LocalDate orderDate;
    @Column(name = "dead_line")
    private LocalDate deadline;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_type")
    private OrderType orderType;
    @Column(name = "estimated_machining_hours")
    private int estimatedMachiningHours;
    private double cost;
    @Column(name = "spent_machining_hours")
    private int spentMachiningHours;
    @Enumerated(EnumType.STRING)
    @Column(name = "job_status")
    private JobStatus status;
    @ElementCollection
    @CollectionTable(name = "spent_machining_hours_by_type")
    @MapKeyColumn(name = "machine_type")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "hours_sum")
    @ToString.Exclude
    private Map<MachineType,Integer> spentMachiningHoursByType = new HashMap<>();
    @ManyToMany
    @JoinTable(name="jobs_required_machines",
            joinColumns=@JoinColumn(name="job_id"),
            inverseJoinColumns=@JoinColumn(name="machine_id"))
    @ToString.Exclude
    private List<Machine> machinedOn = new ArrayList<>();

    public void addMachine(Machine machine){
        machinedOn.add(machine);
        machine.getRunningJob().add(this);
    }

    public void removeMachine(Machine machine){
        machinedOn.remove(machine);
    }

    public void addMachiningHours(MachineType machineType,int hours){
        if(!spentMachiningHoursByType.containsKey(machineType)){
            spentMachiningHoursByType.put(machineType,hours);
        } else {
        spentMachiningHoursByType.put(machineType,spentMachiningHoursByType.get(machineType) + hours);
        }

        spentMachiningHours += hours;
        cost += machineType.getOverheadCharge() * hours * orderType.getMultiplier();
    }
}
