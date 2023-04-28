package sv3advproject.erp_project.mappers;

import org.mapstruct.Mapper;
import sv3advproject.erp_project.dtos.job_dto.JobDto;
import sv3advproject.erp_project.dtos.job_dto.JobWithMachinesAndHoursDto;
import sv3advproject.erp_project.dtos.job_dto.JobWithMachinesDto;
import sv3advproject.erp_project.dtos.job_dto.JobWithMachiningHoursDto;
import sv3advproject.erp_project.models.Job;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobMapper {

    JobDto toDto (Job job);
    List<JobDto> toDto(List<Job> jobs);
    JobWithMachinesDto withMachines(Job job);
    JobWithMachiningHoursDto withHours(Job job);
    JobWithMachinesAndHoursDto withMachinesAndHours(Job job);
}
