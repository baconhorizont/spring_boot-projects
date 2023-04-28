package sv3advproject.erp_project.dtos.employe_dto;

import lombok.Data;
import sv3advproject.erp_project.models.EmployeeQualification;

@Data
public class EmployeeDto {

    private Long id;
    private String name;
    private EmployeeQualification qualification;
}
