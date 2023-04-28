package sv3advproject.erp_project.dtos.employe_dto;

import lombok.Data;
import sv3advproject.erp_project.dtos.machine_dto.MachineDto;
import sv3advproject.erp_project.models.EmployeeQualification;

import java.util.List;

@Data
public class EmployeeWithMachinesDto {

    private Long id;
    private String name;
    private EmployeeQualification qualification;
    private List<MachineDto> canWorkOn;
}
