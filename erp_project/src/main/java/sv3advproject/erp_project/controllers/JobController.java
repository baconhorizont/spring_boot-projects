package sv3advproject.erp_project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sv3advproject.erp_project.dtos.employe_dto.EmployeeDto;
import sv3advproject.erp_project.dtos.job_dto.*;
import sv3advproject.erp_project.models.JobStatus;
import sv3advproject.erp_project.services.JobService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
@AllArgsConstructor
@Tag(name = "Operations on jobs")
public class JobController {

    private JobService jobService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "creates a job")
    @ApiResponse(responseCode = "201",description = "job has been created")
    public JobDto saveJob(@RequestBody @Valid CreateJobCommand command){
        return jobService.saveJob(command);
    }

    @GetMapping("/allJobs")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "lists all jobs")
    @ApiResponse(responseCode = "200",description = "jobs listed")
    public List<JobDto> listAllJob(
            @Parameter(description = "Name of customer",
            example = "Denso")
            @RequestParam Optional<String> customer,
            @Parameter(description = "Status of the job",
                    example = "RUNNING")
            @RequestParam Optional<JobStatus> jobStatus){
        return jobService.listAllJob(customer,jobStatus);
    }

    @PostMapping("/addMachine")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "adds a machine to job")
    @ApiResponse(responseCode = "201",description = "machine added to job")
    @ApiResponse(responseCode = "404",description = "job not found")
    @ApiResponse(responseCode = "404",description = "machine not found")
    public JobWithMachinesDto addMachineToJob(@RequestBody @Valid AddMachineToJobCommand command){
        return jobService.addMachineToJob(command);
    }

    @PostMapping("/addMachiningHours")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "adds a machining hours to job")
    @ApiResponse(responseCode = "201",description = "hours added to job")
    @ApiResponse(responseCode = "400",description = "job not running")
    @ApiResponse(responseCode = "400",description = "machine not added to job")
    @ApiResponse(responseCode = "404",description = "job not found")
    public JobWithMachiningHoursDto addMachiningHours(@RequestBody @Valid AddMachiningHoursCommand command){
        return jobService.addMachiningHours(command);
    }

    @PutMapping("/updateStatus")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "updates job status")
    @ApiResponse(responseCode = "200",description = "job status updated")
    @ApiResponse(responseCode = "404",description = "job not found")
    public JobWithMachinesDto updateJobStatus(@RequestBody @Valid UpdateJobStatusCommand command){
        return jobService.updateJobStatus(command);
    }

    @GetMapping("/{jobId}/madeByEmployee")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "list employees made the job")
    @ApiResponse(responseCode = "200",description = "employees listed")
    @ApiResponse(responseCode = "404",description = "job not found")
    public List<EmployeeDto> findJobByIdMadeByEmployee(
            @Parameter(description = "Id of the job",
            example = "1")
            @PathVariable("jobId") long jobId) {
        return jobService.findJobByIdMadeByEmployee(jobId);
    }

    @GetMapping("/allCost")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "print all cost between given dates")
    @ApiResponse(responseCode = "200",description = "cost printed")
    public Double getAllCostBetweenDate(
            @Parameter(description = "start date",
            example = "2022-05-05")
            @RequestParam Optional<LocalDate> startDate,
            @Parameter(description = "end date",
                    example = "2024-05-05")
            @RequestParam Optional<LocalDate> endDate) {
        return jobService.getAllCostBetweenDate(startDate,endDate);
    }

    @GetMapping("/id/{jobId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "finds job by id with machines and machining hours")
    @ApiResponse(responseCode = "200",description = "job found")
    @ApiResponse(responseCode = "404",description = "job not found")
    public JobWithMachinesAndHoursDto findJobById(
            @Parameter(description = "Id of the job",
            example = "1")
            @PathVariable("jobId") long jobId) {
        return jobService.findJobByIdWithMachinesAndHours(jobId);
    }
}
