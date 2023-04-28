package sv3advproject.erp_project.dtos.machine_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import sv3advproject.erp_project.models.MachineType;
import sv3advproject.erp_project.validators.machine.MachineTypeValid;

@Data
@Builder
public class CreateMachineCommand {

    @NotBlank(message = "Machine name must not blank")
    @Size(min = 3,message = "Machine name size must be greater than 2")
    @Schema(description = "Name of the machine",example = "Hedelius")
    private String name;
    @MachineTypeValid(anyOf = {MachineType.MILL,MachineType.GRINDER,MachineType.LATHE})
    @Schema(description = "Type of the machine",example = "MILL")
    private MachineType type;
}
