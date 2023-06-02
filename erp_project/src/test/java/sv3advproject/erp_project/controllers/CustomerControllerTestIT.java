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
import sv3advproject.erp_project.dtos.customer_dto.CustomerWithJobsDto;
import sv3advproject.erp_project.models.Currency;

import java.net.URI;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/create-data.sql")
@Sql(scripts = "/cleanup-data.sql", executionPhase = AFTER_TEST_METHOD)
class CustomerControllerTestIT {

    @Autowired
    WebTestClient client;

    CustomerDto savedCustomer;

    @BeforeEach
    void initDb(){
        saveCustomers();
    }

    @Test
    @DisplayName("Test save customer")
    void testSaveCustomer() {
        assertNotNull(savedCustomer.getId());
        assertEquals("Euroform Kft",savedCustomer.getName());
        assertEquals(Currency.EUR,savedCustomer.getCurrency());
        assertEquals("Budapest",savedCustomer.getAddress().getCity());
    }

    @Test
    @DisplayName("Test find customer by id")
    void testFindCustomerById(){
        CustomerWithJobsDto found = client.get()
                .uri("/api/customers/id/{customerId}",savedCustomer.getId())
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(CustomerWithJobsDto.class)
                .returnResult().getResponseBody();

        assertEquals(savedCustomer.getName(),found.getName());
        assertEquals(savedCustomer.getAddress().getCity(),found.getAddress().getCity());
        assertThat(found.getJobs()).isEmpty();
    }

    @Test
    @DisplayName("Exception Test find customer by id, not exist")
    void testFindCustomerByIdNotFound(){
        ProblemDetail result = client.get()
                .uri("/api/customers/id/{customerId}",Long.MAX_VALUE)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("customers/customer-not-found"),result.getType());
        assertEquals("Customer not found with id: 9223372036854775807",result.getDetail());
    }

    private void saveCustomers() {
        savedCustomer = client.post()
                .uri("/api/customers")
                .bodyValue(CreateCustomerCommand.builder()
                        .name("Euroform Kft")
                        .city("Budapest")
                        .country("Hungary")
                        .street("Gyömrői út")
                        .postalCode("2121")
                        .streetNumber("12")
                        .currency(Currency.EUR)
                        .vatNumber("123456789")
                        .registrationDate(LocalDate.now().minusYears(5))
                        .build())
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(CustomerDto.class)
                .returnResult().getResponseBody();
    }

}