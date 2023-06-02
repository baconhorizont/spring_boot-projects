//package sv3advproject.erp_project.controllers;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ProblemDetail;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import sv3advproject.erp_project.dtos.employe_dto.CreateEmployeeCommand;
//import sv3advproject.erp_project.dtos.employe_dto.EmployeeDto;
//import sv3advproject.erp_project.dtos.job_dto.AddMachineToJobCommand;
//import sv3advproject.erp_project.dtos.job_dto.CreateJobCommand;
//import sv3advproject.erp_project.dtos.job_dto.JobDto;
//import sv3advproject.erp_project.dtos.job_dto.JobWithMachinesDto;
//import sv3advproject.erp_project.dtos.machine_dto.*;
//import sv3advproject.erp_project.models.EmployeeQualification;
//import sv3advproject.erp_project.models.MachineType;
//import sv3advproject.erp_project.models.OrderType;
//
//import java.net.URI;
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql(scripts = "/create-data.sql")
//@Sql(scripts = "/cleanup-data.sql", executionPhase = AFTER_TEST_METHOD)
//class MachineControllerTestIT {
//
//    @Autowired
//    WebTestClient client;
//
//    EmployeeDto savedEmployeeMill1;
//    EmployeeDto savedEmployeeMill2;
//    EmployeeDto savedEmployeeTurn;
//    MachineDto savedMachine;
//    JobDto savedJob;
//    JobDto savedJob2;
//
//    @BeforeEach
//    void initDb(){
//        saveEmployees();
//        saveMachines();
//        saveJobs();
//    }
//
//    @Test
//    @DisplayName("Test save machine")
//    void testSaveMachine() {
//        assertNotNull(savedMachine.getId());
//        assertEquals("Grobe",savedMachine.getName());
//        assertEquals(MachineType.MILL,savedMachine.getType());
//    }
//
//    @Test
//    @DisplayName("Exception Test save machine with invalid name to short")
//    void testSaveMachineInvalidNameToShort(){
//        ProblemDetail result = client.post()
//                .uri("/api/machines")
//                .bodyValue(CreateMachineCommand.builder()
//                        .name("ab")
//                        .type(MachineType.MILL)
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(406)
//                .expectBody(ProblemDetail.class)
//                .returnResult().getResponseBody();
//
//        assertEquals(URI.create("input-data/not-valid"),result.getType());
//        assertEquals("Machine name size must be greater than 2",result.getDetail());
//    }
//
//    @Test
//    @DisplayName("Exception Test save machine with invalid name blank")
//    void testSaveMachineInvalidNameBlank(){
//        ProblemDetail result = client.post()
//                .uri("/api/machines")
//                .bodyValue(CreateMachineCommand.builder()
//                        .name("        ")
//                        .type(MachineType.MILL)
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(406)
//                .expectBody(ProblemDetail.class)
//                .returnResult().getResponseBody();
//
//        assertEquals(URI.create("input-data/not-valid"),result.getType());
//        assertEquals("Machine name must not blank",result.getDetail());
//    }
//
//    @Test
//    @DisplayName("Exception Test save machine with invalid type")
//    void testSaveMachineInvalidType(){
//        ProblemDetail result = client.post()
//                .uri("/api/machines")
//                .bodyValue(CreateMachineCommand.builder()
//                        .name("Hermle")
//                        .type(MachineType.TEST_TYPE)
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(406)
//                .expectBody(ProblemDetail.class)
//                .returnResult().getResponseBody();
//
//        assertEquals(URI.create("input-data/not-valid"),result.getType());
//        assertEquals("Machine type must be any of [MILL, GRINDER, LATHE]",result.getDetail());
//    }
//
//    @Test
//    @DisplayName("Test list all machine")
//    void testListAllMachine() {
//        List<MachineDto> result = client.get()
//                .uri("/api/machines/allMachines")
//                .exchange()
//                .expectBodyList(MachineDto.class)
//                .returnResult().getResponseBody();
//
//        assertThat(result)
//                .hasSize(5)
//                .extracting(MachineDto::getName)
//                .containsOnly("Grobe","Hermle","Okamoto","EMCO","Mikron");
//    }
//
//    @Test
//    @DisplayName("Test list machines by type")
//    void testListMachinesByType() {
//        List<MachineDto> result = client.get()
//                .uri(b->b.path("/api/machines/allMachines").queryParam("machineType","MILL").build())
//                .exchange()
//                .expectBodyList(MachineDto.class)
//                .returnResult().getResponseBody();
//
//        assertThat(result)
//                .hasSize(3)
//                .extracting(MachineDto::getName)
//                .containsOnly("Grobe","Hermle","Mikron");
//    }
//
//    @Test
//    @DisplayName("Test list machines by type not found")
//    void testListMachinesByTypeNotExist() {
//        List<MachineDto> result = client.get()
//                .uri(b->b.path("/api/machines/allMachines").queryParam("machineType","TEST_TYPE").build())
//                .exchange()
//                .expectStatus().isEqualTo(200)
//                .expectBodyList(MachineDto.class)
//                .returnResult().getResponseBody();
//
//        assertEquals(0,result.size());
//    }
//
//    @Test
//    @DisplayName("Test add employee to machine")
//    void testAddEmployeeToMachine(){
//        MachineWithEmpCanUseDto result = client.post()
//                .uri("/api/machines/addEmployee")
//                .bodyValue(AddEmpToMachineCommand.builder()
//                        .machineId(savedMachine.getId())
//                        .employeeId(savedEmployeeMill1.getId())
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(201)
//                .expectBody(MachineWithEmpCanUseDto.class)
//                .returnResult().getResponseBody();
//
//        assertEquals("Grobe",result.getName());
//        assertThat(result.getCanUse())
//                .hasSize(1)
//                .extracting(EmployeeDto::getName)
//                .containsExactly("Kiss Roland");
//    }
//
//    @Test
//    @DisplayName("Test add employee to machine, same employee")
//    void testAddEmployeeToMachineSame(){
//        client.post()
//                .uri("/api/machines/addEmployee")
//                .bodyValue(AddEmpToMachineCommand.builder()
//                        .machineId(savedMachine.getId())
//                        .employeeId(savedEmployeeMill1.getId())
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(201)
//                .expectBody(MachineWithEmpCanUseDto.class)
//                .returnResult().getResponseBody();
//
//        ProblemDetail result = client.post()
//                .uri("/api/machines/addEmployee")
//                .bodyValue(AddEmpToMachineCommand.builder()
//                        .machineId(savedMachine.getId())
//                        .employeeId(savedEmployeeMill1.getId())
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(400)
//                .expectBody(ProblemDetail.class)
//                .returnResult().getResponseBody();
//
//        assertEquals(URI.create("machines/employee-already-added"),result.getType());
//        assertEquals(String.format("Employee already added id: %d to this machine id: %d",
//                        savedEmployeeMill1.getId(),savedMachine.getId()),
//                result.getDetail());
//    }
//
//    @Test
//    @DisplayName("Exception Test add employee to machine without qualification")
//    void testAddEmployeeToMachineWithoutQualification(){
//        ProblemDetail result = client.post()
//                .uri("/api/machines/addEmployee")
//                .bodyValue(AddEmpToMachineCommand.builder()
//                        .machineId(savedMachine.getId())
//                        .employeeId(savedEmployeeTurn.getId())
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(400)
//                .expectBody(ProblemDetail.class)
//                .returnResult().getResponseBody();
//
//        assertEquals(URI.create("machines/employee-not-qualified"),result.getType());
//        assertEquals(String.format("Employee not qualified id: %d",savedEmployeeTurn.getId()),result.getDetail());
//    }
//
//    @Test
//    @DisplayName("Exception Test add employee to machine, machine not exist")
//    void testAddEmployeeToMachineMachineNotFound(){
//      ProblemDetail result = client.post()
//                .uri("/api/machines/addEmployee")
//                .bodyValue(AddEmpToMachineCommand.builder()
//                        .machineId(Long.MAX_VALUE)
//                        .employeeId(savedEmployeeMill1.getId())
//                        .build())
//              .exchange()
//              .expectStatus().isEqualTo(404)
//              .expectBody(ProblemDetail.class)
//              .returnResult().getResponseBody();
//
//        assertEquals(URI.create("machines/machine-not-found"),result.getType());
//        assertEquals("Machine not found with id: 9223372036854775807",result.getDetail());
//    }
//
//    @Test
//    @DisplayName("Exception Test add employee to machine, employee not exist")
//    void testAddEmployeeToMachineEmployeeNotFound(){
//        ProblemDetail result = client.post()
//                .uri("/api/machines/addEmployee")
//                .bodyValue(AddEmpToMachineCommand.builder()
//                        .machineId(savedMachine.getId())
//                        .employeeId(Long.MAX_VALUE)
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(404)
//                .expectBody(ProblemDetail.class)
//                .returnResult().getResponseBody();
//
//        assertEquals(URI.create("employees/employee-not-found"),result.getType());
//        assertEquals("Employee not found with id: 9223372036854775807",result.getDetail());
//    }
//
//    @Test
//    @DisplayName("Exception Test add employee to machine, invalid machine id")
//    void testAddEmployeeToMachineInvalidMachineId(){
//        ProblemDetail result = client.post()
//                .uri("/api/machines/addEmployee")
//                .bodyValue(AddEmpToMachineCommand.builder()
//                        .machineId(-5)
//                        .employeeId(1)
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(406)
//                .expectBody(ProblemDetail.class)
//                .returnResult().getResponseBody();
//
//        assertEquals(URI.create("input-data/not-valid"),result.getType());
//        assertEquals("Machine id must be greater than 0",result.getDetail());
//    }
//
//    @Test
//    @DisplayName("Exception Test add employee to machine, invalid employee id")
//    void testAddEmployeeToMachineInvalidEmployeeId(){
//        ProblemDetail result = client.post()
//                .uri("/api/machines/addEmployee")
//                .bodyValue(AddEmpToMachineCommand.builder()
//                        .machineId(1)
//                        .employeeId(-5)
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(406)
//                .expectBody(ProblemDetail.class)
//                .returnResult().getResponseBody();
//
//        assertEquals(URI.create("input-data/not-valid"),result.getType());
//        assertEquals("Employee id must be greater than 0",result.getDetail());
//    }
//
//    @Test
//    @DisplayName("Remove employee from machine")
//    void testRemoveEmployeeFromMachine(){
//        client.post()
//                .uri("/api/machines/addEmployee")
//                .bodyValue(AddEmpToMachineCommand.builder()
//                        .machineId(savedMachine.getId())
//                        .employeeId(savedEmployeeMill1.getId())
//                        .build())
//                .exchange();
//
//        client.post()
//                .uri("/api/machines/addEmployee")
//                .bodyValue(AddEmpToMachineCommand.builder()
//                        .machineId(savedMachine.getId())
//                        .employeeId(savedEmployeeMill2.getId())
//                        .build())
//                .exchange();
//
//        MachineWithEmpCanUseDto result= client.put()
//                .uri("/api/machines/removeEmployee")
//                .bodyValue(RemoveEmpFromMachineCommand.builder()
//                        .employeeId(savedEmployeeMill1.getId())
//                        .machineId(savedMachine.getId())
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(200)
//                .expectBody(MachineWithEmpCanUseDto.class)
//                .returnResult().getResponseBody();
//
//        assertThat(result.getCanUse())
//                .hasSize(1)
//                .extracting(EmployeeDto::getName)
//                .containsExactly("Bede Róbert");
//    }
//
//    @Test
//    @DisplayName("Exception Test remove employee from machine, invalid employee id")
//    void testRemoveEmployeeFromMachineInvalidEmployeeId(){
//        ProblemDetail result = client.put()
//                .uri("/api/machines/removeEmployee")
//                .bodyValue(RemoveEmpFromMachineCommand.builder()
//                        .machineId(1)
//                        .employeeId(-5)
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(406)
//                .expectBody(ProblemDetail.class)
//                .returnResult().getResponseBody();
//
//        assertEquals(URI.create("input-data/not-valid"),result.getType());
//        assertEquals("Employee id must be greater than 0",result.getDetail());
//    }
//
//    @Test
//    @DisplayName("Exception Test remove employee from machine, invalid machine id")
//    void testRemoveEmployeeFromMachineInvalidMachineId(){
//        ProblemDetail result = client.put()
//                .uri("/api/machines/removeEmployee")
//                .bodyValue(RemoveEmpFromMachineCommand.builder()
//                        .machineId(-5)
//                        .employeeId(1)
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(406)
//                .expectBody(ProblemDetail.class)
//                .returnResult().getResponseBody();
//
//        assertEquals(URI.create("input-data/not-valid"),result.getType());
//        assertEquals("Machine id must be greater than 0",result.getDetail());
//    }
//
//    @Test
//    @DisplayName("Remove machine connected with job and employee")
//    void testRemoveMachineWithJobAndEmployee(){
//        client.post()
//                .uri("/api/machines/addEmployee")
//                .bodyValue(AddEmpToMachineCommand.builder()
//                        .machineId(savedMachine.getId())
//                        .employeeId(savedEmployeeMill1.getId())
//                        .build())
//                .exchange();
//
//        client.post()
//                .uri("/api/jobs/addMachine")
//                .bodyValue(AddMachineToJobCommand.builder()
//                        .jobId(savedJob.getId())
//                        .machineId(savedMachine.getId())
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(201)
//                .expectBody(JobWithMachinesDto.class)
//                .returnResult().getResponseBody();
//
//        List<MachineDto> resultBeforeDelete = client.get()
//                .uri("/api/machines/allMachines")
//                .exchange()
//                .expectBodyList(MachineDto.class)
//                .returnResult().getResponseBody();
//
//        assertThat(resultBeforeDelete)
//                .hasSize(5)
//                .extracting(MachineDto::getName)
//                .containsOnly("Grobe","Hermle","Okamoto","EMCO","Mikron");
//
//        client.delete()
//                .uri("/api/machines/remove/{machineId}",savedMachine.getId())
//                .exchange()
//                .expectStatus().isEqualTo(204);
//
//        List<MachineDto> resultAfterDelete = client.get()
//                .uri("/api/machines/allMachines")
//                .exchange()
//                .expectBodyList(MachineDto.class)
//                .returnResult().getResponseBody();
//
//        assertThat(resultAfterDelete)
//                .hasSize(4)
//                .extracting(MachineDto::getName)
//                .containsOnly("Hermle","Okamoto","EMCO","Mikron");
//    }
//
//    @Test
//    @DisplayName("Test find running jobs")
//    void testFindRunningJobs(){
//        client.post()
//                .uri("/api/jobs/addMachine")
//                .bodyValue(AddMachineToJobCommand.builder()
//                        .jobId(savedJob2.getId())
//                        .machineId(savedMachine.getId())
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(201)
//                .expectBody(JobWithMachinesDto.class)
//                .returnResult().getResponseBody();
//
//        client.post()
//                .uri("/api/jobs/addMachine")
//                .bodyValue(AddMachineToJobCommand.builder()
//                        .jobId(savedJob.getId())
//                        .machineId(savedMachine.getId())
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(201)
//                .expectBody(JobWithMachinesDto.class)
//                .returnResult().getResponseBody();
//
//        List<JobDto> result = client.get()
//                .uri("/api/machines/runningJobs/{machineId}",savedMachine.getId())
//                .exchange()
//                .expectStatus().isEqualTo(200)
//                .expectBodyList(JobDto.class)
//                .returnResult().getResponseBody();
//
//        assertThat(result)
//                .hasSize(2)
//                .extracting(JobDto::getCustomer)
//                .containsExactly("Z-form Szerszámgyártó Kft.","M+E Kft");
//    }
//
//    @Test
//    @DisplayName("Test find running jobs, no job")
//    void testFindRunningJobsNoJob(){
//        List<JobDto> result = client.get()
//                .uri("/api/machines/runningJobs/{machineId}",savedMachine.getId())
//                .exchange()
//                .expectStatus().isEqualTo(200)
//                .expectBodyList(JobDto.class)
//                .returnResult().getResponseBody();
//
//        assertThat(result)
//                .hasSize(0);
//    }
//
//    private void saveEmployees() {
//        savedEmployeeMill1 = client.post()
//                .uri("/api/employees")
//                .bodyValue(CreateEmployeeCommand.builder()
//                        .name("Kiss Roland")
//                        .qualification(EmployeeQualification.MILLING)
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(201)
//                .expectBody(EmployeeDto.class)
//                .returnResult().getResponseBody();
//
//        savedEmployeeMill2 = client.post()
//                .uri("/api/employees")
//                .bodyValue(CreateEmployeeCommand.builder()
//                        .name("Bede Róbert")
//                        .qualification(EmployeeQualification.MILLING)
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(201)
//                .expectBody(EmployeeDto.class)
//                .returnResult().getResponseBody();
//
//        savedEmployeeTurn = client.post()
//                .uri("/api/employees")
//                .bodyValue(CreateEmployeeCommand.builder()
//                        .name("Uj Péter")
//                        .qualification(EmployeeQualification.TURNING)
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(201)
//                .expectBody(EmployeeDto.class)
//                .returnResult().getResponseBody();
//    }
//
//    private void saveMachines() {
//        savedMachine = client.post()
//                .uri("/api/machines")
//                .bodyValue(CreateMachineCommand.builder()
//                        .name("Grobe")
//                        .type(MachineType.MILL)
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(201)
//                .expectBody(MachineDto.class)
//                .returnResult().getResponseBody();
//    }
//
//    private void saveJobs(){
//        savedJob = client.post()
//                .uri("/api/jobs")
//                .bodyValue(CreateJobCommand.builder()
//                        .customer("Z-form Szerszámgyártó Kft.")
//                        .orderDate(LocalDate.now().minusDays(1))
//                        .deadline(LocalDate.now().plusMonths(1))
//                        .orderType(OrderType.STANDARD)
//                        .estimatedMachiningHours(200)
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(201)
//                .expectBody(JobDto.class)
//                .returnResult().getResponseBody();
//
//        savedJob2 = client.post()
//                .uri("/api/jobs")
//                .bodyValue(CreateJobCommand.builder()
//                        .customer("M+E Kft")
//                        .orderDate(LocalDate.now().minusDays(1))
//                        .deadline(LocalDate.now().plusMonths(5))
//                        .orderType(OrderType.STANDARD)
//                        .estimatedMachiningHours(100)
//                        .build())
//                .exchange()
//                .expectStatus().isEqualTo(201)
//                .expectBody(JobDto.class)
//                .returnResult().getResponseBody();
//    }
//
//
//}