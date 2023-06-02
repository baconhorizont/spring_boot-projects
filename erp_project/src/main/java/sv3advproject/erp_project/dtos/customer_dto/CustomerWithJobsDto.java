package sv3advproject.erp_project.dtos.customer_dto;

import lombok.Data;
import lombok.ToString;
import sv3advproject.erp_project.dtos.job_dto.JobDto;
import sv3advproject.erp_project.dtos.job_dto.JobWithoutCustomerDto;
import sv3advproject.erp_project.models.Address;
import sv3advproject.erp_project.models.Currency;

import java.time.LocalDate;
import java.util.Set;

@Data
public class CustomerWithJobsDto {

    private Long id;
    private String name;
    private String vatNumber;
    private LocalDate registrationDate;
    private Address address;
    private Currency currency;
    @ToString.Exclude
    private Set<JobWithoutCustomerDto> jobs;
}
