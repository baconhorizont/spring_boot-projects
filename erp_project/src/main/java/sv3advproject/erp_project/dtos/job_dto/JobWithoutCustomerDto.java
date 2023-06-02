package sv3advproject.erp_project.dtos.job_dto;

import lombok.Data;
import sv3advproject.erp_project.models.JobStatus;
import sv3advproject.erp_project.models.OrderType;

import java.time.LocalDate;

@Data
public class JobWithoutCustomerDto {

    private Long id;
    private LocalDate orderDate;
    private LocalDate deadline;
    private OrderType orderType;
    private int estimatedMachiningHours;
    private double cost;
    private int spentMachiningHours;
    private JobStatus status;
}
