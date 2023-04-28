package sv3advproject.erp_project.dtos.employe_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import sv3advproject.erp_project.models.EmployeeQualification;
import sv3advproject.erp_project.validators.employee.Name;
import sv3advproject.erp_project.validators.employee.Qualification;

@Data
@Builder
public class UpdateEmployeeCommand {

    @Positive(message = "Employee id must be greater than 0")
    @Schema(description = "Id of employee",example = "1")
    private Long id;
    @Name(message = "Invalid employee name")
    @Schema(description = "Name of employee",example = "Nagy Roland")
    private String name;
    @Qualification(anyOf = {EmployeeQualification.MILLING,EmployeeQualification.TURNING,EmployeeQualification.GRINDING})
    @Schema(description = "Qualification of employee",example = "TURNING")
    private EmployeeQualification qualification;
}
