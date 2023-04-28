package sv3advproject.erp_project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sv3advproject.erp_project.dtos.job_dto.JobDto;
import sv3advproject.erp_project.dtos.machine_dto.*;
import sv3advproject.erp_project.models.MachineType;
import sv3advproject.erp_project.services.MachineService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/machines")
@AllArgsConstructor
@Tag(name = "Operations on machines")
public class MachineController {

    private MachineService machineService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "creates a machine")
    @ApiResponse(responseCode = "201",description = "machine has been created")
    public MachineDto saveMachine(@RequestBody @Valid CreateMachineCommand command){
        return machineService.saveMachine(command);
    }

    @GetMapping("/allMachines")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "lists all machines")
    @ApiResponse(responseCode = "200",description = "machines listed")
    public List<MachineDto> listAllMachine(
            @Parameter(description = "Type of the machine",
            example = "MILL")
            @RequestParam Optional<MachineType> machineType){
        return machineService.listAllMachine(machineType);
    }

    @PostMapping("/addEmployee")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "adds an employee to machine")
    @ApiResponse(responseCode = "201",description = "employee added to machine")
    @ApiResponse(responseCode = "400",description = "employee not qualified to machine")
    @ApiResponse(responseCode = "404",description = "employee not found")
    @ApiResponse(responseCode = "404",description = "machine not found")
    public MachineWithEmpCanUseDto addEmployeeToMachine(@RequestBody @Valid AddEmpToMachineCommand command){
        return machineService.addEmployeeToMachine(command);
    }

    @GetMapping("/runningJobs/{machineId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "lists running jobs of the machine")
    @ApiResponse(responseCode = "200",description = "running jobs listed")
    @ApiResponse(responseCode = "404",description = "machine not found")
    public List<JobDto> findRunningJobs(
            @Parameter(description = "Id of the machine",
            example = "1")
            @PathVariable("machineId")long machineId){
        return machineService.findRunningJobs(machineId);
    }

    @PutMapping("/removeEmployee")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "removes employee from the machine")
    @ApiResponse(responseCode = "200",description = "employee removed")
    @ApiResponse(responseCode = "404",description = "employee not found")
    @ApiResponse(responseCode = "404",description = "machine not found")
    public MachineWithEmpCanUseDto removeEmployeeFromMachine(@RequestBody @Valid RemoveEmpFromMachineCommand command){
        return machineService.removeEmployeeFromMachine(command);
    }

    @DeleteMapping("/remove/{machineId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "removes a machine")
    @ApiResponse(responseCode = "204",description = "machine removed")
    @ApiResponse(responseCode = "404",description = "machine not found")
    public void removeMachineById(
            @Parameter(description = "Id of the machine",
            example = "1")
            @PathVariable("machineId") long machineId){
        machineService.removeMachine(machineId);
    }
}
