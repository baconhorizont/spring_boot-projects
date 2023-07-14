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
    @DisplayName("Exception Test save customer with invalid name blank")
    void testSaveCustomerInvalidNameBlank() {
        ProblemDetail result = client.post()
                .uri("/api/customers")
                .bodyValue(CreateCustomerCommand.builder()
                        .name("     ")
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
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Customer name must not blank",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save customer with invalid name short")
    void testSaveCustomerInvalidNameShort() {
        ProblemDetail result = client.post()
                .uri("/api/customers")
                .bodyValue(CreateCustomerCommand.builder()
                        .name("KF")
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
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Customer name size must be greater than 2",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save customer with invalid vat number")
    void testSaveCustomerInvalidVatNumber() {
        ProblemDetail result = client.post()
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
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Invalid vat number!",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save customer with invalid registration date")
    void testSaveCustomerInvalidRegistrationDate() {
        ProblemDetail result = client.post()
                .uri("/api/customers")
                .bodyValue(CreateCustomerCommand.builder()
                        .name("Euroform Kft")
                        .city("Budapest")
                        .country("Hungary")
                        .street("Gyömrői út")
                        .postalCode("2121")
                        .streetNumber("12")
                        .currency(Currency.EUR)
                        .vatNumber("8464721390")
                        .registrationDate(LocalDate.now().plusDays(1))
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Registration date must be past or present!",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save customer with invalid country, blank")
    void testSaveCustomerInvalidCountryBlank() {
        ProblemDetail result = client.post()
                .uri("/api/customers")
                .bodyValue(CreateCustomerCommand.builder()
                        .name("Euroform Kft")
                        .city("Budapest")
                        .country("    ")
                        .street("Gyömrői út")
                        .postalCode("2121")
                        .streetNumber("12")
                        .currency(Currency.EUR)
                        .vatNumber("8464721390")
                        .registrationDate(LocalDate.now().minusYears(5))
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Customer country must not blank",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save customer with invalid country")
    void testSaveCustomerInvalidCountry() {
        ProblemDetail result = client.post()
                .uri("/api/customers")
                .bodyValue(CreateCustomerCommand.builder()
                        .name("Euroform Kft")
                        .city("Budapest")
                        .country("@&#")
                        .street("Gyömrői út")
                        .postalCode("2121")
                        .streetNumber("12")
                        .currency(Currency.EUR)
                        .vatNumber("8464721390")
                        .registrationDate(LocalDate.now().minusYears(5))
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Invalid country name!",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save customer with invalid postal code")
    void testSaveCustomerInvalidPostal() {
        ProblemDetail result = client.post()
                .uri("/api/customers")
                .bodyValue(CreateCustomerCommand.builder()
                        .name("Euroform Kft")
                        .city("Budapest")
                        .country("Hungary")
                        .street("Gyömrői út")
                        .postalCode("asd")
                        .streetNumber("12")
                        .currency(Currency.EUR)
                        .vatNumber("8464721390")
                        .registrationDate(LocalDate.now().minusYears(5))
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Invalid postal code!",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save customer with invalid city, blank")
    void testSaveCustomerInvalidCityBlank() {
        ProblemDetail result = client.post()
                .uri("/api/customers")
                .bodyValue(CreateCustomerCommand.builder()
                        .name("Euroform Kft")
                        .city("    ")
                        .country("Hungary")
                        .street("Gyömrői út")
                        .postalCode("2121")
                        .streetNumber("12")
                        .currency(Currency.EUR)
                        .vatNumber("8464721390")
                        .registrationDate(LocalDate.now().minusYears(5))
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Customer city must not blank",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save customer with invalid city")
    void testSaveCustomerInvalidCity() {
        ProblemDetail result = client.post()
                .uri("/api/customers")
                .bodyValue(CreateCustomerCommand.builder()
                        .name("Euroform Kft")
                        .city("####")
                        .country("Hungary")
                        .street("Gyömrői út")
                        .postalCode("2121")
                        .streetNumber("12")
                        .currency(Currency.EUR)
                        .vatNumber("8464721390")
                        .registrationDate(LocalDate.now().minusYears(5))
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Invalid city name!",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save customer with invalid street, blank")
    void testSaveCustomerInvalidStreetBlank() {
        ProblemDetail result = client.post()
                .uri("/api/customers")
                .bodyValue(CreateCustomerCommand.builder()
                        .name("Euroform Kft")
                        .city("Budapest")
                        .country("Hungary")
                        .street("    ")
                        .postalCode("2121")
                        .streetNumber("12")
                        .currency(Currency.EUR)
                        .vatNumber("8464721390")
                        .registrationDate(LocalDate.now().minusYears(5))
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Customer street must not blank",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save customer with invalid street")
    void testSaveCustomerInvalidStreet() {
        ProblemDetail result = client.post()
                .uri("/api/customers")
                .bodyValue(CreateCustomerCommand.builder()
                        .name("Euroform Kft")
                        .city("Budapest")
                        .country("Hungary")
                        .street("@@123")
                        .postalCode("2121")
                        .streetNumber("12")
                        .currency(Currency.EUR)
                        .vatNumber("8464721390")
                        .registrationDate(LocalDate.now().minusYears(5))
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Invalid street name!",result.getDetail());
    }

    @Test
    @DisplayName("Exception Test save customer with invalid currency")
    void testSaveCustomerInvalidCurrency() {
        ProblemDetail result = client.post()
                .uri("/api/customers")
                .bodyValue(CreateCustomerCommand.builder()
                        .name("Euroform Kft")
                        .city("Budapest")
                        .country("Hungary")
                        .street("Petőfi utca")
                        .postalCode("2121")
                        .streetNumber("12")
                        .currency(Currency.TEST)
                        .vatNumber("8464721390")
                        .registrationDate(LocalDate.now().minusYears(5))
                        .build())
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertEquals(URI.create("input-data/not-valid"), result.getType());
        assertEquals("Currency must be any of [HUF, EUR]",result.getDetail());
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
                        .vatNumber("8464721390")
                        .registrationDate(LocalDate.now().minusYears(5))
                        .build())
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(CustomerDto.class)
                .returnResult().getResponseBody();
    }

}