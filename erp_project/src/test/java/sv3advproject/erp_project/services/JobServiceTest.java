package sv3advproject.erp_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv3advproject.erp_project.dtos.job_dto.*;
import sv3advproject.erp_project.exceptions.JobNotRunningException;
import sv3advproject.erp_project.exceptions.MachineNotAddedException;
import sv3advproject.erp_project.mappers.EmployeeMapperImpl;
import sv3advproject.erp_project.mappers.JobMapperImpl;
import sv3advproject.erp_project.models.*;
import sv3advproject.erp_project.models.Currency;
import sv3advproject.erp_project.repository.CustomerRepository;
import sv3advproject.erp_project.repository.JobRepository;
import sv3advproject.erp_project.repository.MachineRepository;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    JobRepository jobRepository;
    @Mock
    MachineRepository machineRepository;

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    JobService jobService = new JobService(jobRepository,customerRepository,machineRepository,new JobMapperImpl(),new EmployeeMapperImpl());

    Machine machine;
    Customer customer;
    Job job;

    AddMachiningHoursCommand addMachiningHoursCommand;

    @BeforeEach
    void init(){
        machine = Machine.builder()
                .id(1L)
                .name("Hermle")
                .type(MachineType.MILL)
                .runningJob(new TreeSet<>())
                .build();

        customer = Customer.builder()
                .id(1L)
                .name("Robert Bosch Kft")
                .vatNumber("123456789")
                .registrationDate(LocalDate.now().minusYears(1))
                .address(new Address("Hungary","1213","Budapest","Gyömrői út","128"))
                .currency(Currency.EUR)
                .jobs(new HashSet<>())
                .build();

        job = Job.builder()
                .id(1L)
                .customer(customer)
                .orderDate(LocalDate.now().minusDays(1))
                .deadline(LocalDate.now().plusMonths(1))
                .orderType(OrderType.STANDARD)
                .estimatedMachiningHours(200)
                .status(JobStatus.NEW)
                .machinedOn(new HashSet<>())
                .spentMachiningHoursByType(new HashMap<>())
                .build();

        addMachiningHoursCommand = AddMachiningHoursCommand.builder()
                .jobId(1)
                .machineId(1)
                .hours(50)
                .build();
    }

    @Test
    @DisplayName("Test add machining hours by type")
    void testAddMachiningHoursByType(){
        job.setStatus(JobStatus.RUNNING);
        job.addMachine(machine);

        when(machineRepository.findById(anyLong()))
                .thenReturn(Optional.of(machine));

        when(jobRepository.findById(anyLong()))
                .thenReturn(Optional.of(job));

        JobWithMachiningHoursDto result = jobService.addMachiningHours(addMachiningHoursCommand);

        assertEquals("Robert Bosch Kft",result.getCustomer().getName());
        assertEquals(1250000,result.getCost());
        assertEquals(50,result.getSpentMachiningHours());
        assertThat(result.getSpentMachiningHoursByType())
                .hasSize(1)
                .containsKey(MachineType.MILL)
                .containsValue(50);

        verify(jobRepository,times(1)).findById(1L);
        verify(machineRepository,times(1)).findById(1L);
    }

    @Test
    @DisplayName("Exception Test add machining hours by type, machine not added")
    void testAddMachiningHoursByTypeMachineNotAdded(){
        job.setStatus(JobStatus.RUNNING);

        when(machineRepository.findById(anyLong()))
                .thenReturn(Optional.of(machine));

        when(jobRepository.findById(anyLong()))
                .thenReturn(Optional.of(job));

        assertThatThrownBy(()->jobService.addMachiningHours(addMachiningHoursCommand))
                .isInstanceOf(MachineNotAddedException.class)
                .hasMessage("Machine not added to this job: Hermle");

        verify(jobRepository,times(1)).findById(1L);
        verify(machineRepository,times(1)).findById(1L);
    }

    @Test
    @DisplayName("Exception Test add machining hours by type, job not running")
    void testAddMachiningHoursByTypeJobNotRunning(){
        when(jobRepository.findById(anyLong()))
                .thenReturn(Optional.of(job));

        assertThatThrownBy(()->jobService.addMachiningHours(addMachiningHoursCommand))
                .isInstanceOf(JobNotRunningException.class)
                .hasMessage("Job not running id: 1");

        verify(jobRepository,times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test set job status to finished")
    void SetJobStatusFinished() {
        job.addMachine(machine);

        when(jobRepository.findById(anyLong()))
                .thenReturn(Optional.of(job));

        JobWithMachinesDto resultRunning = jobService.updateJobStatus(UpdateJobStatusCommand.builder()
                .jobId(job.getId())
                .jobStatus(JobStatus.RUNNING)
                .build());

        assertEquals(JobStatus.RUNNING,resultRunning.getStatus());
        assertEquals(1,resultRunning.getMachinedOn().size());

        JobWithMachinesDto resultFinished = jobService.updateJobStatus(UpdateJobStatusCommand.builder()
                .jobId(job.getId())
                .jobStatus(JobStatus.FINISHED)
                .build());

        assertEquals(JobStatus.FINISHED,resultFinished.getStatus());
        assertEquals(0,resultFinished.getMachinedOn().size());

        verify(jobRepository,times(2)).findById(1L);
    }
}