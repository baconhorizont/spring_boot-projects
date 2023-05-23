package sv3advproject.erp_project.exceptions;

public class CustomerNotFoundException extends RuntimeException{

    public CustomerNotFoundException(long id) {
        super(String.format("Customer not found with id: %d",id));
    }

    public CustomerNotFoundException(String name) {
        super(String.format("Customer not found with name: %s",name));
    }
}
