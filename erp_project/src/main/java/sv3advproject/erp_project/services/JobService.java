package sv3advproject.erp_project.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sv3advproject.erp_project.dtos.employe_dto.EmployeeDto;
import sv3advproject.erp_project.dtos.job_dto.*;
import sv3advproject.erp_project.exceptions.JobNotFoundException;
import sv3advproject.erp_project.exceptions.JobNotRunningException;
import sv3advproject.erp_project.exceptions.MachineNotAddedException;
import sv3advproject.erp_project.exceptions.MachineNotFoundException;
import sv3advproject.erp_project.mappers.EmployeeMapper;
import sv3advproject.erp_project.mappers.JobMapper;
import sv3advproject.erp_project.models.Employee;
import sv3advproject.erp_project.models.Job;
import sv3advproject.erp_project.models.JobStatus;
import sv3advproject.erp_project.models.Machine;
import sv3advproject.erp_project.repository.JobRepository;
import sv3advproject.erp_project.repository.MachineRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JobService {

    private static final JobStatus INITIAL_JOB_STATUS = JobStatus.NEW;

    private JobRepository jobRepository;
    private MachineRepository machineRepository;
    private JobMapper jobMapper;
    private EmployeeMapper employeeMapper;

    public JobDto saveJob(CreateJobCommand command){
        Job job = buildJob(command);

        jobRepository.save(job);
        return jobMapper.toDto(job);
    }

    public List<JobDto> listAllJob(Optional<String> customer, Optional<JobStatus> jobStatus) {
        return jobMapper.toDto(jobRepository.findByCustomerAndStatus(customer,jobStatus));
    }

    @Transactional
    public JobWithMachinesDto addMachineToJob(AddMachineToJobCommand command) {
        Job job = findJobById(command.getJobId());
        Machine machine = findMachineById(command.getMachineId());
        job.addMachine(machine);
        return jobMapper.withMachines(job);
    }

    @Transactional
    public JobWithMachiningHoursDto addMachiningHours(AddMachiningHoursCommand command){
        Job job = findJobById(command.getJobId());
        if (!job.getStatus().equals(JobStatus.RUNNING)){
            throw new JobNotRunningException(job.getId());
        }
        Machine machine = findMachineById(command.getMachineId());
        validateAddMachine(job,machine);
        job.addMachiningHours(machine.getType(),command.getHours());

        return jobMapper.withHours(job);
    }

    @Transactional
    public JobWithMachinesDto updateJobStatus(UpdateJobStatusCommand command) {
        Job job = findJobById(command.getJobId());
        if(command.getJobStatus().equals(JobStatus.FINISHED)){
            job.getMachinedOn().clear();
        }
        job.setStatus(command.getJobStatus());
        return jobMapper.withMachines(job);
    }

    public List<EmployeeDto> findJobByIdMadeByEmployee(long jobId){
        List<Machine> result = jobRepository.findJobByIdMadeByEmployee(jobId);
        List<Employee> employees = result.stream()
                .flatMap(m -> m.getCanUse().stream())
                .distinct()
                .toList();
        return employeeMapper.toDto(employees);
    }

    public Double getAllCostBetweenDate(Optional<LocalDate> startDate, Optional<LocalDate> endDate) {
      Optional<Double> result = jobRepository.getAllCostBetweenDate(startDate,endDate);
       return result.orElse(0.0);
    }

    public JobWithMachinesAndHoursDto findJobByIdWithMachinesAndHours(long jobId){
        Job job = findJobById(jobId);
        return jobMapper.withMachinesAndHours(job);
    }

    private Job buildJob(CreateJobCommand command) {
        return Job.builder()
                .customer(command.getCustomer())
                .orderDate(command.getOrderDate())
                .deadline(command.getDeadline())
                .orderType(command.getOrderType())
                .estimatedMachiningHours(command.getEstimatedMachiningHours())
                .status(INITIAL_JOB_STATUS)
                .build();
    }

    private void validateAddMachine(Job job, Machine machine){
        job.getMachinedOn().stream()
                .filter(m-> m.getType().equals(machine.getType()))
                .findFirst().orElseThrow(()-> new MachineNotAddedException(machine.getName()));
    }

    private Machine findMachineById(long machineId) {
        return machineRepository.findById(machineId)
                .orElseThrow(()-> new MachineNotFoundException(machineId));
    }

    private Job findJobById(long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(()-> new JobNotFoundException(jobId));
    }
}
