package sv3advproject.erp_project.exceptions;

public class EmployeeNotQualifiedException extends RuntimeException{

    public EmployeeNotQualifiedException(long id) {
        super(String.format("Employee not qualified id: %d",id));
    }
}
