package sv3advproject.erp_project.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import sv3advproject.erp_project.dtos.customer_dto.CreateCustomerCommand;
import sv3advproject.erp_project.dtos.customer_dto.CustomerDto;
import sv3advproject.erp_project.dtos.employe_dto.CreateEmployeeCommand;
import sv3advproject.erp_project.dtos.employe_dto.EmployeeDto;
import sv3advproject.erp_project.dtos.job_dto.*;
import sv3advproject.erp_project.dtos.machine_dto.AddEmpToMachineCommand;
import sv3advproject.erp_project.dtos.machine_dto.CreateMachineCommand;
import sv3advproject.erp_project.dtos.machine_dto.MachineDto;
import sv3advproject.erp_project.models.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/create-data.sql")
@Sql(scripts = "/cleanup-data.sql", executionPhase = AFTER_TEST_METHOD)
class JobControllerTestIT {

    @Autowired
    WebTestClient client;

    EmployeeDto savedEmployeeMill1;
    EmployeeDto savedEmployeeMill2;
    EmployeeDto savedEmployeeTurn;
    MachineDto savedMachine;
    JobDto savedJob;
    CustomerDto savedCustomer;

    @BeforeEach
    void initDb(){
        saveCustomers();
        saveEmployees();
        saveMachines();
        saveJobs();
    }

    @Test
    @DisplayName("Test save job")
    void testSaveJob() {
        assertNotNull(savedJob.getId());
        assertEquals("Z-form Szerszámgyártó Kft.", savedJob.getCustomer().getName());
        assertEquals(OrderType.STANDARD, savedJob.getOrderType());
    }

    @Test
    @DisplayName("Exception, Test save job, customer not found")
    void testSaveJobCustomerNotFound() {
        ProblemDetail result = client.post()
                .uri("/api/jobs")
                .bodyValue(CreateJobCommand.builder()
                        .customer("Alma")
                        .orderDate(LocalDate.now().minusDays(1))
                        .deadline(LocalDate.now().plusMonths(1))
                        .orderType(OrderType.STANDARD)
                        .estimatedMachiningHours(200)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("customers/customer-not-found"), result.getType());
        assertEquals("Customer not found with name: Alma",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save job with invalid name blank")
    void testSaveJobInvalidNameBlank() {
        ProblemDetail result = client.post()
                .uri("/api/jobs")
                .bodyValue(CreateJobCommand.builder()
                        .customer("     ")
                        .orderDate(LocalDate.now().minusDays(1))
                        .deadline(LocalDate.now().plusMonths(1))
                        .orderType(OrderType.STANDARD)
                        .estimatedMachiningHours(200)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Customer name must not blank",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save job with invalid name to short")
    void testSaveJobInvalidNameToShort() {
        ProblemDetail result = client.post()
                .uri("/api/jobs")
                .bodyValue(CreateJobCommand.builder()
                        .customer("     ")
                        .orderDate(LocalDate.now().minusDays(1))
                        .deadline(LocalDate.now().plusMonths(1))
                        .orderType(OrderType.STANDARD)
                        .estimatedMachiningHours(200)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Customer name must not blank",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save job with invalid type")
    void testSaveJobInvalidType() {
        ProblemDetail result = client.post()
                .uri("/api/jobs")
                .bodyValue(CreateJobCommand.builder()
                        .customer("Z-form Szerszámgyártó Kft.")
                        .orderDate(LocalDate.now().minusDays(1))
                        .deadline(LocalDate.now().plusMonths(1))
                        .orderType(OrderType.TEST_TYPE)
                        .estimatedMachiningHours(200)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Order type must be any of [STANDARD, PRIORITY, DISCOUNTED]",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save job with invalid machining hours")
    void testSaveJobInvalidHours() {
        ProblemDetail result = client.post()
                .uri("/api/jobs")
                .bodyValue(CreateJobCommand.builder()
                        .customer("Z-form Szerszámgyártó Kft.")
                        .orderDate(LocalDate.now().minusDays(1))
                        .deadline(LocalDate.now().plusMonths(1))
                        .orderType(OrderType.STANDARD)
                        .estimatedMachiningHours(0)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Machining hours must be positive and greater than 0",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save job with invalid order date")
    void testSaveJobInvalidOrderDate() {
        ProblemDetail result = client.post()
                .uri("/api/jobs")
                .bodyValue(CreateJobCommand.builder()
                        .customer("Z-form Szerszámgyártó Kft.")
                        .orderDate(LocalDate.now().plusDays(1))
                        .deadline(LocalDate.now().plusMonths(1))
                        .orderType(OrderType.STANDARD)
                        .estimatedMachiningHours(200)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Order date must be int the past or present",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save job with invalid deadline date")
    void testSaveJobInvalidDeadlineDate() {
        ProblemDetail result = client.post()
                .uri("/api/jobs")
                .bodyValue(CreateJobCommand.builder()
                        .customer("Z-form Szerszámgyártó Kft.")
                        .orderDate(LocalDate.now().minusDays(1))
                        .deadline(LocalDate.now().minusDays(1))
                        .orderType(OrderType.STANDARD)
                        .estimatedMachiningHours(200)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Deadline date must be in the future",result.getDetail());
    }

    @Test
    @DisplayName("Test list all job")
    void testListAllJob() {
        List<JobDto> result = client.get()
                .uri("/api/jobs/allJobs")
                .exchange()
                .expectBodyList(JobDto.class)
                .returnResult().getResponseBody();

        assertThat(result)
                .hasSize(4)
                .extracting(JobDto::getCustomer)
                .extracting(CustomerDto::getName)
                .contains("Z-form Szerszámgyártó Kft.","BPW Hungary", "Continental Hungary","Euroform Kft");
    }

    @Test
    @DisplayName("Test list all job by customer")
    void testListAllJobByCustomer() {
        List<JobDto> result = client.get()
                .uri(b->b.path("/api/jobs/allJobs").queryParam("customerName","form").build())
                .exchange()
                .expectBodyList(JobDto.class)
                .returnResult().getResponseBody();

        assertThat(result)
                .hasSize(2)
                .extracting(JobDto::getCustomer)
                .extracting(CustomerDto::getName)
                .contains("Z-form Szerszámgyártó Kft.","Euroform Kft");
    }

    @Test
    @DisplayName("Test list all job by status")
    void testListAllJobByStatus() {
        List<JobDto> result = client.get()
                .uri(b->b.path("/api/jobs/allJobs").queryParam("jobStatus","NEW").build())
                .exchange()
                .expectBodyList(JobDto.class)
                .returnResult().getResponseBody();

        assertThat(result)
                .hasSize(3)
                .extracting(JobDto::getCustomer)
                .extracting(CustomerDto::getName)
                .contains("Z-form Szerszámgyártó Kft.","BPW Hungary", "Continental Hungary");
    }

    @Test
    @DisplayName("Test list all job not found")
    void testListAllJobNotFound() {
        List<JobDto> result = client.get()
                .uri(b->b.path("/api/jobs/allJobs").queryParam("customer","form").queryParam("jobStatus","FINISHED").build())
                .exchange()
                .expectBodyList(JobDto.class)
                .returnResult().getResponseBody();

        assertThat(result)
                .hasSize(0);
    }

    @Test
    @DisplayName("Test list all job by customer and status ")
    void testListAllJobByCustomerAndStatus() {
        List<JobDto> result = client.get()
                .uri(b->b.path("/api/jobs/allJobs").queryParam("customerName","form").queryParam("jobStatus","NEW").build())
                .exchange()
                .expectBodyList(JobDto.class)
                .returnResult().getResponseBody();

        assertThat(result)
                .hasSize(1)
                .extracting(JobDto::getCustomer)
                .extracting(CustomerDto::getName)
                .contains("Z-form Szerszámgyártó Kft.");
    }

    @Test
    @DisplayName("Test add machine to job")
    void testAddMachineToJob() {
        JobWithMachinesDto result = client.post()
                .uri("/api/jobs/addMachine")
                .bodyValue(AddMachineToJobCommand.builder()
                        .jobId(savedJob.getId())
                        .machineId(savedMachine.getId())
                        .build())
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(JobWithMachinesDto.class)
                .returnResult().getResponseBody();

        assertEquals("Z-form Szerszámgyártó Kft.", result.getCustomer().getName());
        assertThat(result.getMachinedOn())
                .hasSize(1)
                .extracting(MachineDto::getName)
                .containsExactly("Grobe");
    }

    @Test
    @DisplayName("Exception Test add machine to job, same machine")
    void testAddMachineToJobSameMachine() {
        client.post()
                .uri("/api/jobs/addMachine")
                .bodyValue(AddMachineToJobCommand.builder()
                        .jobId(savedJob.getId())
                        .machineId(savedMachine.getId())
                        .build())
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(JobWithMachinesDto.class)
                .returnResult().getResponseBody();

        ProblemDetail result = client.post()
                .uri("/api/jobs/addMachine")
                .bodyValue(AddMachineToJobCommand.builder()
                        .jobId(savedJob.getId())
                        .machineId(savedMachine.getId())
                        .build())
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("jobs/machine-already-added"), result.getType());
        assertEquals(String.format("Machine already added id: %d to this job id: %d",savedMachine.getId(),savedJob.getId()),result.getDetail());
    }

    @Test
    @DisplayName("Exception Test add machine to job, job not exist")
    void testAddMachineToJobJobNotFound() {
        ProblemDetail result = client.post()
                .uri("/api/jobs/addMachine")
                .bodyValue(AddMachineToJobCommand.builder()
                        .jobId(Long.MAX_VALUE)
                        .machineId(savedMachine.getId())
                        .build())
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("jobs/job-not-found"), result.getType());
        assertEquals("Job not found with id: 9223372036854775807",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test add machine to job, machine not exist")
    void testAddMachineToJobMachineNotFound() {
        ProblemDetail result = client.post()
                .uri("/api/jobs/addMachine")
                .bodyValue(AddMachineToJobCommand.builder()
                        .jobId(savedJob.getId())
                        .machineId(Long.MAX_VALUE)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("machines/machine-not-found"), result.getType());
        assertEquals("Machine not found with id: 9223372036854775807",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test add machine to job, unacceptable job id")
    void testAddMachineToJobUnacceptableJobId(){
        ProblemDetail result = client.post()
                .uri("/api/jobs/addMachine")
                .bodyValue(AddMachineToJobCommand.builder()
                        .jobId(-5)
                        .machineId(1)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"),result.getType());
        assertEquals("Job id must be greater than 0",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test add machine to job, unacceptable machine id")
    void testAddMachineToJobUnacceptableMachineId(){
        ProblemDetail result = client.post()
                .uri("/api/jobs/addMachine")
                .bodyValue(AddMachineToJobCommand.builder()
                        .jobId(1)
                        .machineId(-5)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"),result.getType());
        assertEquals("Machine id must be greater than 0",result.getDetail());
    }

    @Test
    @DisplayName("Test add machining hours to job")
    void testAddMachiningHoursToJob() {
        client.put()
                .uri("/api/jobs/updateStatus")
                .bodyValue(UpdateJobStatusCommand.builder()
                        .jobId(savedJob.getId())
                        .jobStatus(JobStatus.RUNNING)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(JobDto.class)
                .returnResult().getResponseBody();

        client.post()
                .uri("/api/jobs/addMachine")
                .bodyValue(AddMachineToJobCommand.builder()
                        .jobId(savedJob.getId())
                        .machineId(savedMachine.getId())
                        .build())
                .exchange();

        JobWithMachiningHoursDto result = client.post()
                .uri("/api/jobs/addMachiningHours")
                .bodyValue(AddMachiningHoursCommand.builder()
                        .jobId(savedJob.getId())
                        .machineId(savedMachine.getId())
                        .hours(50)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(JobWithMachiningHoursDto.class)
                .returnResult().getResponseBody();

        assertEquals("Z-form Szerszámgyártó Kft.", result.getCustomer().getName());
        assertEquals(1250000,result.getCost());
        assertEquals(50,result.getSpentMachiningHours());
        assertThat(result.getSpentMachiningHoursByType())
                .hasSize(1)
                .containsKey(MachineType.MILL)
                .containsValue(50);
    }

    @Test
    @DisplayName("Exception Test add machining hours to job, machine not added")
    void testAddMachiningHoursToJobMachineNotAdded() {
        client.put()
                .uri("/api/jobs/updateStatus")
                .bodyValue(UpdateJobStatusCommand.builder()
                        .jobId(savedJob.getId())
                        .jobStatus(JobStatus.RUNNING)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(JobDto.class)
                .returnResult().getResponseBody();


        ProblemDetail result = client.post()
                .uri("/api/jobs/addMachiningHours")
                .bodyValue(AddMachiningHoursCommand.builder()
                        .jobId(savedJob.getId())
                        .machineId(savedMachine.getId())
                        .hours(50)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("jobs/machine-not-added"),result.getType());
        assertEquals("Machine not added to this job: Grobe",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test add machining hours to job, invalid machining hours")
    void testAddMachiningHoursToJobMachineInvalidHours() {
        ProblemDetail result = client.post()
                .uri("/api/jobs/addMachiningHours")
                .bodyValue(AddMachiningHoursCommand.builder()
                        .jobId(savedJob.getId())
                        .machineId(savedMachine.getId())
                        .hours(-50)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"),result.getType());
        assertEquals("Machining hour must be greater than 0",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test add machining hours to job, invalid job id")
    void testAddMachiningHoursToJobMachineInvalidJobId() {
        ProblemDetail result = client.post()
                .uri("/api/jobs/addMachiningHours")
                .bodyValue(AddMachiningHoursCommand.builder()
                        .jobId(-5)
                        .machineId(savedMachine.getId())
                        .hours(50)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"),result.getType());
        assertEquals("Job id must be greater than 0",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test add machining hours to job, invalid machine id")
    void testAddMachiningHoursToJobMachineInvalidMachineId() {
        ProblemDetail result = client.post()
                .uri("/api/jobs/addMachiningHours")
                .bodyValue(AddMachiningHoursCommand.builder()
                        .jobId(savedJob.getId())
                        .machineId(-5)
                        .hours(50)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"),result.getType());
        assertEquals("Machine id must be greater than 0",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test add machining hours to job, job not running")
    void testAddMachiningHoursToJobNotRunning() {
        client.post()
                .uri("/api/jobs/addMachine")
                .bodyValue(AddMachineToJobCommand.builder()
                        .jobId(savedJob.getId())
                        .machineId(savedMachine.getId())
                        .build())
                .exchange();

        ProblemDetail result = client.post()
                .uri("/api/jobs/addMachiningHours")
                .bodyValue(AddMachiningHoursCommand.builder()
                        .jobId(savedJob.getId())
                        .machineId(savedMachine.getId())
                        .hours(50)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("jobs/job-not-running"),result.getType());
        assertEquals(String.format("Job not running id: %d",savedJob.getId()),result.getDetail());
    }

    @Test
    @DisplayName("Test update job status")
    void testUpdateStatus() {
        assertEquals(JobStatus.NEW, savedJob.getStatus());

        JobDto result = client.put()
                .uri("/api/jobs/updateStatus")
                .bodyValue(UpdateJobStatusCommand.builder()
                        .jobId(savedJob.getId())
                        .jobStatus(JobStatus.RUNNING)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(JobDto.class)
                .returnResult().getResponseBody();

        assertEquals("Z-form Szerszámgyártó Kft.", result.getCustomer().getName());
        assertEquals(JobStatus.RUNNING, result.getStatus());
    }

    @Test
    @DisplayName("Exception Test update job status, invalid status")
    void testUpdateStatusInvalidStatus() {
        ProblemDetail result = client.put()
                .uri("/api/jobs/updateStatus")
                .bodyValue(UpdateJobStatusCommand.builder()
                        .jobId(savedJob.getId())
                        .jobStatus(JobStatus.TEST_STATUS)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"),result.getType());
        assertEquals("Job status must be any of [NEW, RUNNING, FINISHED, FINISHED, INVOICED]",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test update job status, invalid job id")
    void testUpdateStatusInvalidJobId() {
        ProblemDetail result = client.put()
                .uri("/api/jobs/updateStatus")
                .bodyValue(UpdateJobStatusCommand.builder()
                        .jobId(-5)
                        .jobStatus(JobStatus.RUNNING)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"),result.getType());
        assertEquals("Job id must be greater than 0",result.getDetail());
    }

    @Test
    @DisplayName("Test list made by employee")
    void testMadeByEmployee(){
        client.post()
                .uri("/api/machines/addEmployee")
                .bodyValue(AddEmpToMachineCommand.builder()
                        .machineId(savedMachine.getId())
                        .employeeId(savedEmployeeMill1.getId())
                        .build())
                .exchange();
        client.post()
                .uri("/api/machines/addEmployee")
                .bodyValue(AddEmpToMachineCommand.builder()
                        .machineId(savedMachine.getId())
                        .employeeId(savedEmployeeMill2.getId())
                        .build())
                .exchange();

        client.post()
                .uri("/api/jobs/addMachine")
                .bodyValue(AddMachineToJobCommand.builder()
                        .jobId(savedJob.getId())
                        .machineId(savedMachine.getId())
                        .build())
                .exchange();

        List<EmployeeDto> result = client.get()
                .uri("/api/jobs/{jobId}/madeByEmployee",savedJob.getId())
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBodyList(EmployeeDto.class)
                .returnResult().getResponseBody();

        assertThat(result)
                .hasSize(2)
                .extracting(EmployeeDto::getName)
                .containsOnly("Kiss Roland","Bede Róbert");
    }

    @Test
    @DisplayName("Test get all cost between given dates")
    void testGetAllCostBetweenDate(){
        Double result = client.get()
                .uri(b->b.path("/api/jobs/allCost").queryParam("startDate","2024-02-01").queryParam("endDate","2024-12-01").build())
                .exchange()
                .expectBody(Double.class)
                .returnResult().getResponseBody();

        assertEquals(100.0,result);
    }

    @Test
    @DisplayName("Test get all cost given only start date")
    void testGetAllCostGivenStartDate(){
        Double result = client.get()
                .uri(b->b.path("/api/jobs/allCost").queryParam("startDate","2024-01-01").build())
                .exchange()
                .expectBody(Double.class)
                .returnResult().getResponseBody();

        assertEquals(200.0,result);
    }

    @Test
    @DisplayName("Test get all cost given only end date")
    void testGetAllCostGivenEndDate(){
        Double result = client.get()
                .uri(b->b.path("/api/jobs/allCost").queryParam("endDate","2023-12-31").build())
                .exchange()
                .expectBody(Double.class)
                .returnResult().getResponseBody();

        assertEquals(100.0,result);
    }

    @Test
    @DisplayName("Test get all cost between given dates, no result")
    void testGetAllCostBetweenNotFound(){
        Double result = client.get()
                .uri(b->b.path("/api/jobs/allCost").queryParam("startDate","2022-01-01").queryParam("endDate","2023-01-01").build())
                .exchange()
                .expectBody(Double.class)
                .returnResult().getResponseBody();

        assertEquals(0.0,result);
    }

    @Test
    @DisplayName("Test find job by id")
    void testFindJobById(){
        JobWithMachinesAndHoursDto found = client.get()
                .uri("/api/jobs/id/{jobId}",savedJob.getId())
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(JobWithMachinesAndHoursDto.class)
                .returnResult().getResponseBody();

        assertEquals(savedJob.getCustomer().getId(),found.getCustomer().getId());
        assertEquals(savedJob.getStatus(),found.getStatus());
    }

    @Test
    @DisplayName("Exception Test find job by id, not found")
    void testFindJobByIdNotFound(){
        ProblemDetail result = client.get()
                .uri("/api/jobs/id/{jobId}",Long.MAX_VALUE)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("jobs/job-not-found"),result.getType());
        assertEquals("Job not found with id: 9223372036854775807",result.getDetail());
    }

    private void saveEmployees() {
        savedEmployeeMill1 = client.post()
                .uri("/api/employees")
                .bodyValue(CreateEmployeeCommand.builder()
                        .name("Kiss Roland")
                        .qualification(EmployeeQualification.MILLING)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(EmployeeDto.class)
                .returnResult().getResponseBody();

        savedEmployeeMill2 = client.post()
                .uri("/api/employees")
                .bodyValue(CreateEmployeeCommand.builder()
                        .name("Bede Róbert")
                        .qualification(EmployeeQualification.MILLING)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(EmployeeDto.class)
                .returnResult().getResponseBody();

        savedEmployeeTurn = client.post()
                .uri("/api/employees")
                .bodyValue(CreateEmployeeCommand.builder()
                        .name("Uj Péter")
                        .qualification(EmployeeQualification.TURNING)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(EmployeeDto.class)
                .returnResult().getResponseBody();
    }

    private void saveMachines() {
        savedMachine = client.post()
                .uri("/api/machines")
                .bodyValue(CreateMachineCommand.builder()
                        .name("Grobe")
                        .type(MachineType.MILL)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(MachineDto.class)
                .returnResult().getResponseBody();
    }

    private void saveJobs(){
        savedJob = client.post()
                .uri("/api/jobs")
                .bodyValue(CreateJobCommand.builder()
                        .customer("Z-form Szerszámgyártó Kft.")
                        .orderDate(LocalDate.now().minusDays(1))
                        .deadline(LocalDate.now().plusMonths(1))
                        .orderType(OrderType.STANDARD)
                        .estimatedMachiningHours(200)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(JobDto.class)
                .returnResult().getResponseBody();
    }

    private void saveCustomers() {
        savedCustomer = client.post()
                .uri("/api/customers")
                .bodyValue(CreateCustomerCommand.builder()
                        .name("Z-form Szerszámgyártó Kft.")
                        .city("Budapest")
                        .country("Hungary")
                        .street("Gyömrői út")
                        .postalCode("2121")
                        .streetNumber("12")
                        .currency(Currency.EUR)
                        .vatNumber("8464721390")
                        .registrationDate(LocalDate.now().minusYears(5))
                        .build())
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(CustomerDto.class)
                .returnResult().getResponseBody();
    }
}