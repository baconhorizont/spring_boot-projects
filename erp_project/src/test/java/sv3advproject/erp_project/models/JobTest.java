package sv3advproject.erp_project.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JobTest {

    Job jobStandard;
    Job jobPriority;

    Customer customer;

    @BeforeEach
    void init(){
        customer = Customer.builder()
                .id(1L)
                .name("Robert Bosch Kft")
                .vatNumber("123456789")
                .registrationDate(LocalDate.now().minusYears(1))
                .address(new Address("Hungary","1213","Budapest","Gyömrői út","128"))
                .currency(Currency.EUR)
                .jobs(new HashSet<>())
                .build();

        jobStandard = Job.builder()
                .id(1L)
                .customer(customer)
                .orderDate(LocalDate.now().minusDays(1))
                .deadline(LocalDate.now().plusMonths(1))
                .orderType(OrderType.STANDARD)
                .estimatedMachiningHours(200)
                .machinedOn(new HashSet<>())
                .spentMachiningHoursByType(new HashMap<>())
                .build();

        jobStandard.addMachine(Machine.builder().name("Hermle").type(MachineType.MILL).runningJob(new TreeSet<>()).build());

        jobPriority = Job.builder()
                .id(2L)
                .customer(customer)
                .orderDate(LocalDate.now().minusDays(1))
                .deadline(LocalDate.now().plusMonths(1))
                .orderType(OrderType.PRIORITY)
                .estimatedMachiningHours(200)
                .machinedOn(new HashSet<>())
                .spentMachiningHoursByType(new HashMap<>())
                .build();

        jobPriority.addMachine(Machine.builder().name("Okuma").type(MachineType.LATHE).runningJob(new TreeSet<>()).build());
    }

    @Test
    @DisplayName("Test add machining hour by machine type job standard")
    void testAddMachiningHourJobStandard(){
        jobStandard.addMachiningHours(MachineType.MILL,50);

        assertEquals(1250000,jobStandard.getCost());
        assertEquals(50,jobStandard.getSpentMachiningHours());
        assertThat(jobStandard.getSpentMachiningHoursByType())
                .hasSize(1)
                .containsKey(MachineType.MILL)
                .containsValue(50);
    }

    @Test
    @DisplayName("Test add machining hour by machine type job priority")
    void testAddMachiningHourJobPriority(){
        jobPriority.addMachiningHours(MachineType.MILL,50);

        assertEquals(1625000,jobPriority.getCost());
        assertEquals(50,jobPriority.getSpentMachiningHours());
        assertThat(jobPriority.getSpentMachiningHoursByType())
                .hasSize(1)
                .containsKey(MachineType.MILL)
                .containsValue(50);
    }

    @Test
    @DisplayName("Test add machining hour by machine type different machine types")
    void testAddMachiningHourDifferentTypes(){
        jobPriority.addMachiningHours(MachineType.MILL,50);
        jobPriority.addMachiningHours(MachineType.LATHE,10);

        assertEquals(1859000,jobPriority.getCost());
        assertEquals(60,jobPriority.getSpentMachiningHours());
        assertThat(jobPriority.getSpentMachiningHoursByType())
                .hasSize(2)
                .containsKeys(MachineType.MILL,MachineType.LATHE)
                .containsValues(50,10);
    }
}