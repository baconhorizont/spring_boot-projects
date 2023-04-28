package sv3advproject.erp_project.mappers;

import org.mapstruct.Mapper;
import sv3advproject.erp_project.dtos.machine_dto.MachineDto;
import sv3advproject.erp_project.dtos.machine_dto.MachineWithEmpCanUseDto;
import sv3advproject.erp_project.models.Machine;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MachineMapper {

    MachineDto toDto(Machine machine);
    List<MachineDto> toDto(List<Machine> machines);
    MachineWithEmpCanUseDto toDtoWithEmp(Machine machine);
}
