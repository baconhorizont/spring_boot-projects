package sv3advproject.erp_project.dtos.job_dto;

import lombok.Data;
import lombok.ToString;
import sv3advproject.erp_project.dtos.machine_dto.MachineDto;
import sv3advproject.erp_project.models.JobStatus;
import sv3advproject.erp_project.models.MachineType;
import sv3advproject.erp_project.models.OrderType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class JobWithMachinesAndHoursDto {

    private Long id;
    private String customer;
    private LocalDate orderDate;
    private LocalDate deadline;
    private OrderType orderType;
    private int estimatedMachiningHours;
    private double cost;
    private int spentMachiningHours;
    private JobStatus status;
    @ToString.Exclude
    private Map<MachineType,Integer> spentMachiningHoursByType;
    @ToString.Exclude
    private List<MachineDto> machinedOn;
}
