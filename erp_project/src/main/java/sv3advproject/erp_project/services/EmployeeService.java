package sv3advproject.erp_project.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sv3advproject.erp_project.dtos.employe_dto.CreateEmployeeCommand;
import sv3advproject.erp_project.dtos.employe_dto.EmployeeDto;
import sv3advproject.erp_project.dtos.employe_dto.EmployeeWithMachinesDto;
import sv3advproject.erp_project.dtos.employe_dto.UpdateEmployeeCommand;
import sv3advproject.erp_project.exceptions.EmployeeNotFoundException;
import sv3advproject.erp_project.mappers.EmployeeMapper;
import sv3advproject.erp_project.models.Employee;
import sv3advproject.erp_project.models.EmployeeQualification;
import sv3advproject.erp_project.models.Machine;
import sv3advproject.erp_project.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private EmployeeMapper employeeMapper;

    public EmployeeDto saveEmployee(CreateEmployeeCommand command) {
        Employee employee = buildEmployee(command);
        employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }

    public List<EmployeeDto> listAllEmployee(Optional<String> nameFragment, Optional<EmployeeQualification> qualification) {
              List<Employee> result =  employeeRepository.findByNameAndQualification(nameFragment,qualification);
              return employeeMapper.toDto(result);
    }

    public EmployeeWithMachinesDto findEmployeeById(long employeeId) {
        return employeeMapper.withMachines(employeeRepository
                .findByIdWithMachines(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId))
        );
    }

    @Transactional
    public void removeEmployee(long id){
        Employee employee = findById(id);

        if(!employee.getCanWorkOn().isEmpty()){
            for (Machine machine : employee.getCanWorkOn()) {
               machine.removeEmployee(employee);
            }
        }
            employeeRepository.delete(employee);
    }

    @Transactional
    public EmployeeDto updateEmployee(UpdateEmployeeCommand command) {
        Employee employee = findById(command.getId());
        employee.setName(command.getName());
        employee.setQualification(command.getQualification());
        return employeeMapper.toDto(employee);
    }

    private Employee buildEmployee(CreateEmployeeCommand command) {
        return Employee.builder()
                .name(command.getName())
                .qualification(command.getQualification())
                .build();
    }

    private Employee findById(long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }
}
