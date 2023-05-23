package sv3advproject.erp_project.mappers;

import org.mapstruct.Mapper;
import sv3advproject.erp_project.dtos.customer_dto.CustomerDto;
import sv3advproject.erp_project.dtos.customer_dto.CustomerWithJobsDto;
import sv3advproject.erp_project.models.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto toDto(Customer customer);

    CustomerWithJobsDto toDtoWithJobs(Customer customer);
}
