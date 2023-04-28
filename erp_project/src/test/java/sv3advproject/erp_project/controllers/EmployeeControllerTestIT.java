package sv3advproject.erp_project.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import sv3advproject.erp_project.dtos.employe_dto.CreateEmployeeCommand;
import sv3advproject.erp_project.dtos.employe_dto.EmployeeDto;
import sv3advproject.erp_project.dtos.employe_dto.UpdateEmployeeCommand;
import sv3advproject.erp_project.dtos.machine_dto.AddEmpToMachineCommand;
import sv3advproject.erp_project.dtos.machine_dto.CreateMachineCommand;
import sv3advproject.erp_project.dtos.machine_dto.MachineDto;
import sv3advproject.erp_project.models.EmployeeQualification;
import sv3advproject.erp_project.models.MachineType;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/create-data.sql")
@Sql(scripts = "/cleanup-data.sql", executionPhase = AFTER_TEST_METHOD)
class EmployeeControllerTestIT {

    @Autowired
    WebTestClient client;

    EmployeeDto savedEmployee;
    MachineDto savedMachine;

    @BeforeEach
    void initDb(){
        saveEmployees();
        saveMachines();
    }

    @Test
    @DisplayName("Test save employee")
    void testSaveEmployee() {
        assertNotNull(savedEmployee.getId());
        assertEquals("Kiss Roland",savedEmployee.getName());
        assertEquals(EmployeeQualification.MILLING,savedEmployee.getQualification());
    }

    @Test
    @DisplayName("Exception Test save employee with invalid name, special char")
    void testSaveEmployeeInvalidName() {
        ProblemDetail result = client.post()
                .uri("/api/employees")
                .bodyValue(CreateEmployeeCommand.builder()
                        .name("Nagy#Roland")
                        .qualification(EmployeeQualification.MILLING)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"),result.getType());
        assertEquals("Invalid employee name",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save employee with invalid name, blank")
    void testSaveEmployeeInvalidNameBlank() {
        ProblemDetail result = client.post()
                .uri("/api/employees")
                .bodyValue(CreateEmployeeCommand.builder()
                        .name("     ")
                        .qualification(EmployeeQualification.MILLING)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"),result.getType());
        assertEquals("Invalid employee name",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save employee with invalid qualification")
    void testSaveEmployeeInvalidQualification() {
        ProblemDetail result = client.post()
                .uri("/api/employees")
                .bodyValue(CreateEmployeeCommand.builder()
                        .name("Nagy Roland")
                        .qualification(EmployeeQualification.TEST_QUAL)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"),result.getType());
        assertEquals("Employee qualification must be any of [MILLING, TURNING, GRINDING]",result.getDetail());
    }

    @Test
    @DisplayName("Test list all employee")
    void testListAllEmployee() {
        List<EmployeeDto> result = client.get()
                .uri("/api/employees/allEmployees")
                .exchange()
                .expectBodyList(EmployeeDto.class)
                .returnResult().getResponseBody();

        assertThat(result)
                .hasSize(6)
                .extracting(EmployeeDto::getName)
                .containsOnly("Kiss Roland","Nagy Roland","Iglói Péter","Kovács István","Takács Vanda","Szalai Zorka");
    }

    @Test
    @DisplayName("Test list employees by name")
    void testListAllEmployeeByName() {
        List<EmployeeDto> result = client.get()
                .uri(b->b.path("/api/employees/allEmployees").queryParam("nameFragment","lan").build())
                .exchange()
                .expectBodyList(EmployeeDto.class)
                .returnResult().getResponseBody();

        assertThat(result)
                .hasSize(2)
                .extracting(EmployeeDto::getName)
                .containsOnly("Kiss Roland","Nagy Roland");
    }

    @Test
    @DisplayName("Test list employees by qualification")
    void testListAllEmployeeByQualification() {
        List<EmployeeDto> result = client.get()
                .uri(b->b.path("/api/employees/allEmployees").queryParam("qualification","TURNING").build())
                .exchange()
                .expectBodyList(EmployeeDto.class)
                .returnResult().getResponseBody();

        assertThat(result)
                .hasSize(2)
                .extracting(EmployeeDto::getName)
                .containsOnly("Szalai Zorka","Iglói Péter");
    }

    @Test
    @DisplayName("Test list employees by name and qualification")
    void testListAllEmployeeByNameAndQualification() {
        List<EmployeeDto> result = client.get()
                .uri(b->b.path("/api/employees/allEmployees").queryParam("nameFragment","or").queryParam("qualification","TURNING").build())
                .exchange()
                .expectBodyList(EmployeeDto.class)
                .returnResult().getResponseBody();

        assertThat(result)
                .hasSize(1)
                .extracting(EmployeeDto::getName)
                .containsExactly("Szalai Zorka");
    }

    @Test
    @DisplayName("Test find employee by id")
    void testFindEmployeeById(){
        EmployeeDto found = client.get()
                .uri("/api/employees/id/{employeeId}",savedEmployee.getId())
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(EmployeeDto.class)
                .returnResult().getResponseBody();

        assertEquals(savedEmployee.getName(),found.getName());
        assertEquals(savedEmployee.getQualification(),found.getQualification());
    }

    @Test
    @DisplayName("Exception Test find employee by id, not exist")
    void testFindEmployeeByIdNotFound(){
        ProblemDetail result = client.get()
                .uri("/api/employees/id/{employeeId}",Long.MAX_VALUE)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("employees/employee-not-found"),result.getType());
        assertEquals("Employee not found with id: 9223372036854775807",result.getDetail());
    }

    @Test
    @DisplayName("Test find list employees by name and qualification, not found")
    void testListAllEmployeeByNameAndQualificationNotFound(){
        List<EmployeeDto> result = client.get()
                .uri(b->b.path("/api/employees/allEmployees").queryParam("nameFragment","roland").queryParam("qualification","TURNING").build())
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBodyList(EmployeeDto.class)
                .returnResult().getResponseBody();

        assertEquals(0,result.size());
    }

    @Test
    @DisplayName("Test find list employees by name, not found")
    void testListAllEmployeeByNameNotFound(){
        List<EmployeeDto> result = client.get()
                .uri(b->b.path("/api/employees/allEmployees").queryParam("nameFragment","bence").build())
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBodyList(EmployeeDto.class)
                .returnResult().getResponseBody();

        assertEquals(0,result.size());
    }

    @Test
    @DisplayName("Test find list employees by qualification, not found")
    void testListAllEmployeeByQualificationNotFound(){
        List<EmployeeDto> result = client.get()
                .uri(b->b.path("/api/employees/allEmployees").queryParam("qualification","TEST_QUAL").build())
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBodyList(EmployeeDto.class)
                .returnResult().getResponseBody();

        assertEquals(0,result.size());
    }

    @Test
    @DisplayName("Remove employee with machines")
    void testRemoveEmployeeWithMachines(){
        client.post()
                .uri("/api/machines/addEmployee")
                .bodyValue(AddEmpToMachineCommand.builder()
                        .machineId(savedMachine.getId())
                        .employeeId(savedEmployee.getId())
                        .build())
                .exchange();

        List<EmployeeDto> resultBeforeDelete = client.get()
                .uri("/api/employees/allEmployees")
                .exchange()
                .expectBodyList(EmployeeDto.class)
                .returnResult().getResponseBody();

        assertThat(resultBeforeDelete)
                .hasSize(6)
                .extracting(EmployeeDto::getName)
                .contains("Kiss Roland","Nagy Roland","Iglói Péter","Kovács István","Takács Vanda","Szalai Zorka");

        client.delete()
                .uri("/api/employees/remove/{employeeId}",savedEmployee.getId())
                .exchange()
                .expectStatus().isEqualTo(204);

        List<EmployeeDto> resultAfterDelete = client.get()
                .uri("/api/employees/allEmployees")
                .exchange()
                .expectBodyList(EmployeeDto.class)
                .returnResult().getResponseBody();

        assertThat(resultAfterDelete)
                .hasSize(5)
                .extracting(EmployeeDto::getName)
                .contains("Nagy Roland","Iglói Péter","Kovács István","Takács Vanda","Szalai Zorka");
    }

    @Test
    @DisplayName("Test update employee")
    void testUpdateEmployee() {
        EmployeeDto saved =  client.post()
                .uri("/api/employees")
                .bodyValue(CreateEmployeeCommand.builder()
                        .name("Nagy Roland")
                        .qualification(EmployeeQualification.MILLING)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(EmployeeDto.class)
                .returnResult().getResponseBody();

      EmployeeDto result =  client.put()
                .uri("/api/employees/update")
                .bodyValue(UpdateEmployeeCommand.builder()
                        .id(saved.getId())
                        .name("Iglói Péter")
                        .qualification(EmployeeQualification.TURNING)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(EmployeeDto.class)
                .returnResult().getResponseBody();

      assertEquals("Iglói Péter",result.getName());
      assertEquals(EmployeeQualification.TURNING,result.getQualification());
    }

    @Test
    @DisplayName("Exception Test update employee with invalid name, to short")
    void testUpdateEmployeeInvalidNameToShort() {
        ProblemDetail result = client.put()
                .uri("/api/employees/update")
                .bodyValue(UpdateEmployeeCommand.builder()
                        .id(1L)
                        .name("ab")
                        .qualification(EmployeeQualification.MILLING)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"),result.getType());
        assertEquals("Invalid employee name",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test update employee with invalid name, null")
    void testUpdateEmployeeInvalidNameNull() {
        ProblemDetail result = client.put()
                .uri("/api/employees/update")
                .bodyValue(UpdateEmployeeCommand.builder()
                        .id(1L)
                        .name(null)
                        .qualification(EmployeeQualification.MILLING)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"),result.getType());
        assertEquals("Invalid employee name",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test update employee with invalid qualification")
    void testUpdateEmployeeInvalidQualification() {
        ProblemDetail result = client.put()
                .uri("/api/employees/update")
                .bodyValue(UpdateEmployeeCommand.builder()
                        .id(1L)
                        .name("Nagy Roland")
                        .qualification(EmployeeQualification.TEST_QUAL)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"),result.getType());
        assertEquals("Employee qualification must be any of [MILLING, TURNING, GRINDING]",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test update employee with invalid id")
    void testUpdateEmployeeInvalidId() {
        ProblemDetail result = client.put()
                .uri("/api/employees/update")
                .bodyValue(UpdateEmployeeCommand.builder()
                        .id(-5L)
                        .name("Nagy Roland")
                        .qualification(EmployeeQualification.MILLING)
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"),result.getType());
        assertEquals("Employee id must be greater than 0",result.getDetail());
    }

    private void saveEmployees() {
        savedEmployee = client.post()
                .uri("/api/employees")
                .bodyValue(CreateEmployeeCommand.builder()
                        .name("Kiss Roland")
                        .qualification(EmployeeQualification.MILLING)
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
}