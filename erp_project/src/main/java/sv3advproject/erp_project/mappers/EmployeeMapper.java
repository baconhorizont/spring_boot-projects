package sv3advproject.erp_project.mappers;

import org.mapstruct.Mapper;
import sv3advproject.erp_project.dtos.employe_dto.EmployeeDto;
import sv3advproject.erp_project.dtos.employe_dto.EmployeeWithMachinesDto;
import sv3advproject.erp_project.models.Employee;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDto toDto(Employee employee);
    List<EmployeeDto> toDto(List<Employee> employees);
    EmployeeWithMachinesDto withMachines(Employee employee);
}
