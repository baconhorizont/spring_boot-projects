package sv3advproject.erp_project.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;

import org.mockito.junit.jupiter.MockitoExtension;
import sv3advproject.erp_project.dtos.employe_dto.EmployeeDto;
import sv3advproject.erp_project.dtos.machine_dto.AddEmpToMachineCommand;
import sv3advproject.erp_project.dtos.machine_dto.MachineWithEmpCanUseDto;
import sv3advproject.erp_project.exceptions.EmployeeNotQualifiedException;
import sv3advproject.erp_project.mappers.JobMapperImpl;
import sv3advproject.erp_project.mappers.MachineMapperImpl;
import sv3advproject.erp_project.models.Employee;
import sv3advproject.erp_project.models.EmployeeQualification;
import sv3advproject.erp_project.models.Machine;
import sv3advproject.erp_project.models.MachineType;
import sv3advproject.erp_project.repository.EmployeeRepository;
import sv3advproject.erp_project.repository.JobRepository;
import sv3advproject.erp_project.repository.MachineRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MachineServiceTest {

    @Mock
    MachineRepository machineRepository;
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    JobRepository jobRepository;

    @InjectMocks
    MachineService machineService = new MachineService(machineRepository,
            employeeRepository,jobRepository,new JobMapperImpl(),new MachineMapperImpl());

    @Test
    @DisplayName("Test add employee to machine")
    void testAddEmployeeToMachine() {
        when(machineRepository.findById(anyLong()))
                .thenReturn(Optional.of(Machine.builder()
                        .id(1L)
                        .name("Hermle")
                        .type(MachineType.MILL)
                        .canUse(new ArrayList<>())
                        .build()));
        when(employeeRepository.findById(anyLong()))
                .thenReturn(Optional.of(Employee.builder()
                        .id(1L)
                        .name("Nagy Roland")
                        .qualification(EmployeeQualification.MILLING)
                        .canWorkOn(new ArrayList<>())
                        .build()));

        MachineWithEmpCanUseDto result = machineService.addEmployeeToMachine(AddEmpToMachineCommand.builder()
                        .machineId(1)
                        .employeeId(1)
                        .build());

        assertEquals("Hermle",result.getName());
        assertThat(result.getCanUse())
                .hasSize(1)
                .extracting(EmployeeDto::getName)
                .containsExactly("Nagy Roland");

        verify(machineRepository,times(1)).findById(1L);
        verify(employeeRepository,times(1)).findById(1L);
    }

    @Test
    @DisplayName("Exception Test add employee to machine without qualification")
    void testAddEmployeeToMachineNotQualified() {
        when(machineRepository.findById(anyLong()))
                .thenReturn(Optional.of(Machine.builder()
                        .id(1L)
                        .name("Hermle")
                        .type(MachineType.MILL)
                        .canUse(new ArrayList<>())
                        .build()));
        when(employeeRepository.findById(anyLong()))
                .thenReturn(Optional.of(Employee.builder()
                        .id(1L)
                        .name("Iglói Péter")
                        .qualification(EmployeeQualification.TURNING)
                        .canWorkOn(new ArrayList<>())
                        .build()));

        assertThatThrownBy(()->machineService.addEmployeeToMachine(AddEmpToMachineCommand.builder()
                .machineId(1)
                .employeeId(1)
                .build()))
                .isInstanceOf(EmployeeNotQualifiedException.class)
                .hasMessage("Employee not qualified id: 1");

        verify(machineRepository,times(1)).findById(1L);
        verify(employeeRepository,times(1)).findById(1L);
    }
}