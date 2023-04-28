package sv3advproject.erp_project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sv3advproject.erp_project.dtos.employe_dto.CreateEmployeeCommand;
import sv3advproject.erp_project.dtos.employe_dto.EmployeeDto;
import sv3advproject.erp_project.dtos.employe_dto.EmployeeWithMachinesDto;
import sv3advproject.erp_project.dtos.employe_dto.UpdateEmployeeCommand;
import sv3advproject.erp_project.models.EmployeeQualification;
import sv3advproject.erp_project.services.EmployeeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
@Tag(name = "Operations on employees")
public class EmployeeController {

    private EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "creates an employee")
    @ApiResponse(responseCode = "201",description = "employee has been created")
    public EmployeeDto saveEmployee(@RequestBody @Valid CreateEmployeeCommand command){
       return employeeService.saveEmployee(command);
    }

    @GetMapping("/allEmployees")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "lists all employees")
    @ApiResponse(responseCode = "200",description = "employees listed")
    public List<EmployeeDto> listAllEmployee(
            @Parameter(description = "Name fragment of the employee",
            example = "Roland")
            @RequestParam Optional<String> nameFragment,
            @Parameter(description = "Qualification of the employee",
            example = "MILLING")
            @RequestParam Optional<EmployeeQualification> qualification) {
        return employeeService.listAllEmployee(nameFragment,qualification);
    }

    @GetMapping("/id/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "finds an employee by id")
    @ApiResponse(responseCode = "200",description = "employee found")
    @ApiResponse(responseCode = "404",description = "employee not found")
    public EmployeeWithMachinesDto findEmployeeById(
            @Parameter(description = "Id of the employee",
            example = "1")
            @PathVariable("employeeId") long employeeId) {
        return employeeService.findEmployeeById(employeeId);
    }

    @DeleteMapping("/remove/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "removes an employee")
    @ApiResponse(responseCode = "204",description = "employee deleted")
    @ApiResponse(responseCode = "404",description = "employee not found")
    public void removeEmployeeById(
            @Parameter(description = "Id of the employee",
            example = "1")
            @PathVariable("employeeId") long employeeId) {
        employeeService.removeEmployee(employeeId);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "updates an employee")
    @ApiResponse(responseCode = "200",description = "employee has been updated")
    @ApiResponse(responseCode = "404",description = "employee not found")
    public EmployeeDto updateEmployee(@RequestBody @Valid UpdateEmployeeCommand command){
       return employeeService.updateEmployee(command);
    }
}
