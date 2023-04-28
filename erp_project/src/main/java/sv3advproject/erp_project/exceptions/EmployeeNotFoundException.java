package sv3advproject.erp_project.exceptions;

public class EmployeeNotFoundException extends RuntimeException{

    public EmployeeNotFoundException(long id) {
        super(String.format("Employee not found with id: %d",id));
    }
}
