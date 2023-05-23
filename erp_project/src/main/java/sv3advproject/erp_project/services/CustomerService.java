package sv3advproject.erp_project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sv3advproject.erp_project.dtos.customer_dto.CreateCustomerCommand;
import sv3advproject.erp_project.dtos.customer_dto.CustomerDto;
import sv3advproject.erp_project.dtos.customer_dto.CustomerWithJobsDto;
import sv3advproject.erp_project.dtos.employe_dto.EmployeeWithMachinesDto;
import sv3advproject.erp_project.exceptions.CustomerNotFoundException;
import sv3advproject.erp_project.exceptions.EmployeeNotFoundException;
import sv3advproject.erp_project.mappers.CustomerMapper;
import sv3advproject.erp_project.models.Address;
import sv3advproject.erp_project.models.Customer;
import sv3advproject.erp_project.repository.CustomerRepository;

import java.time.LocalDate;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class CustomerService {

    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;

    public CustomerDto saveCustomer(CreateCustomerCommand command) {
        Customer customer = buildCustomer(command);
        customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    public CustomerWithJobsDto findCustomerById(long customerId) {
        return customerMapper.toDtoWithJobs(customerRepository
                .findByIdWithJobs(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId))
        );
    }

    private Customer buildCustomer(CreateCustomerCommand command) {
        Address address = buildAddress(command);
        return Customer.builder()
                .name(command.getName())
                .address(address)
                .vatNumber(command.getVatNumber())
                .currency(command.getCurrency())
                .registrationDate(LocalDate.now())
                .jobs(new HashSet<>())
                .build();
    }

    private Address buildAddress(CreateCustomerCommand command) {
        return Address.builder()
                .country(command.getCountry())
                .city(command.getCity())
                .street(command.getStreet())
                .streetNumber(command.getStreetNumber())
                .postalCode(command.getPostalCode())
                .build();
    }

}
