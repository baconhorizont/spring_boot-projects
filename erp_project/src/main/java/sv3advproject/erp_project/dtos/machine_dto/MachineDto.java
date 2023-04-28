package sv3advproject.erp_project.dtos.machine_dto;

import lombok.Data;
import sv3advproject.erp_project.models.MachineType;

@Data
public class MachineDto {

    private Long id;
    private String name;
    private MachineType type;
}
