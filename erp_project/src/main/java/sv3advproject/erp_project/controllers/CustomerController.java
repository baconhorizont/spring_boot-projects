package sv3advproject.erp_project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sv3advproject.erp_project.dtos.customer_dto.CreateCustomerCommand;
import sv3advproject.erp_project.dtos.customer_dto.CustomerDto;
import sv3advproject.erp_project.dtos.customer_dto.CustomerWithJobsDto;
import sv3advproject.erp_project.services.CustomerService;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
public class CustomerController {

    private CustomerService  customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto saveCustomer(@RequestBody CreateCustomerCommand command){
        return customerService.saveCustomer(command);
    }

    @GetMapping("/id/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerWithJobsDto findCustomerById(@PathVariable("employeeId") long customerId) {
        return customerService.findCustomerById(customerId);
    }

}
