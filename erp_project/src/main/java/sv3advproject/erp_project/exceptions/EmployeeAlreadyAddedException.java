package sv3advproject.erp_project.exceptions;

public class EmployeeAlreadyAddedException extends RuntimeException {

    public EmployeeAlreadyAddedException(long employeeId,long machineId) {
        super(String.format("Employee already added id: %d to this machine id: %d",employeeId,machineId));
    }
}
