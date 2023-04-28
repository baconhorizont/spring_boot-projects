package sv3advproject.erp_project.dtos.machine_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RemoveEmpFromMachineCommand {

    @Positive(message = "Machine id must be greater than 0")
    @Schema(description = "Id of the machine",example = "1")
    private long machineId;
    @Positive(message = "Employee id must be greater than 0")
    @Schema(description = "Id of employee",example = "1")
    private long employeeId;
}
