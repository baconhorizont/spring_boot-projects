package sv3advproject.erp_project.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sv3advproject.erp_project.dtos.job_dto.JobDto;
import sv3advproject.erp_project.dtos.machine_dto.*;
import sv3advproject.erp_project.exceptions.EmployeeAlreadyAddedException;
import sv3advproject.erp_project.exceptions.EmployeeNotFoundException;
import sv3advproject.erp_project.exceptions.EmployeeNotQualifiedException;
import sv3advproject.erp_project.exceptions.MachineNotFoundException;
import sv3advproject.erp_project.mappers.JobMapper;
import sv3advproject.erp_project.mappers.MachineMapper;
import sv3advproject.erp_project.models.Employee;
import sv3advproject.erp_project.models.Job;
import sv3advproject.erp_project.models.Machine;
import sv3advproject.erp_project.models.MachineType;
import sv3advproject.erp_project.repository.EmployeeRepository;
import sv3advproject.erp_project.repository.JobRepository;
import sv3advproject.erp_project.repository.MachineRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class MachineService {

    private MachineRepository machineRepository;
    private EmployeeRepository employeeRepository;
    private JobRepository jobRepository;
    private JobMapper jobMapper;
    private MachineMapper machineMapper;

    public MachineDto saveMachine(CreateMachineCommand command){
        Machine machine = buildMAchine(command);
        machineRepository.save(machine);
        return machineMapper.toDto(machine);
    }

    public List<MachineDto> listAllMachine(Optional<MachineType> machineType){
            return machineMapper.toDto(machineRepository.findByType(machineType));
    }

    @Transactional
    public MachineWithEmpCanUseDto addEmployeeToMachine(AddEmpToMachineCommand command) {
        Machine machine = findMachineById(command.getMachineId());
        Employee employee = findEmployeeById(command.getEmployeeId());
        if(!machine.getType().toString().equals(employee.getQualification().getRequires())){
            throw new EmployeeNotQualifiedException(employee.getId());
        }
        if(machine.getCanUse().contains(employee)){
            throw new EmployeeAlreadyAddedException(command.getEmployeeId(),command.getMachineId());
        }
        machine.addEmployee(employee);
        return machineMapper.toDtoWithEmp(machine);
    }

    @Transactional
    public MachineWithEmpCanUseDto removeEmployeeFromMachine(RemoveEmpFromMachineCommand command){
        Machine machine = findMachineById(command.getMachineId());
        Employee employee = findEmployeeById(command.getEmployeeId());
        machine.removeEmployee(employee);
        return machineMapper.toDtoWithEmp(machine);
    }

    @Transactional
    public void removeMachine(long machineId) {
        Machine machine = findMachineById(machineId);
        if(!machine.getRunningJob().isEmpty()){
            for (Job job : machine.getRunningJob()) {
                job.removeMachine(machine);
            }
        }
        machineRepository.delete(machine);
    }

    public List<JobDto> findRunningJobs(long machineId) {
        List<Job> jobs = jobRepository.findJobByMachineId(machineId);
        return jobMapper.toDto(jobs);
    }

    private Employee findEmployeeById(long empId) {
        return employeeRepository.findById(empId)
                .orElseThrow(()-> new EmployeeNotFoundException(empId));
    }

    private Machine buildMAchine(CreateMachineCommand command) {
        return Machine.builder()
                .name(command.getName())
                .type(command.getType())
                .build();
    }

    private Machine findMachineById(long machineId) {
        return machineRepository.findById(machineId)
                .orElseThrow(()-> new MachineNotFoundException(machineId));
    }

}
