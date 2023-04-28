package sv3advproject.erp_project.dtos.machine_dto;

import lombok.Data;
import lombok.ToString;
import sv3advproject.erp_project.dtos.employe_dto.EmployeeDto;
import sv3advproject.erp_project.models.MachineType;

import java.util.List;

@Data
public class MachineWithEmpCanUseDto {

    private Long id;
    private String name;
    private MachineType type;
    @ToString.Exclude
    private List<EmployeeDto> canUse;
}
