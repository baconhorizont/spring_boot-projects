package sv3advproject.erp_project.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Job implements Comparable<Job> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Customer customer;
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
    private Set<Machine> machinedOn = new HashSet<>();

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

    @Override
    public int compareTo(Job otherJob) {
        return this.deadline.compareTo(otherJob.getDeadline());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return id.equals(job.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
