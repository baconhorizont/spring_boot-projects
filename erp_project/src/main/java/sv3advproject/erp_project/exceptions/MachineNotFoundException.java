package sv3advproject.erp_project.exceptions;

public class MachineNotFoundException extends RuntimeException{

    public MachineNotFoundException(long id) {
        super(String.format("Machine not found with id: %d",id));
    }
}
