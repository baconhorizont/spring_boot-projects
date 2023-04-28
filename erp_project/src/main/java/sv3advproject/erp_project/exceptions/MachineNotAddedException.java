package sv3advproject.erp_project.exceptions;

public class MachineNotAddedException extends RuntimeException{

    public MachineNotAddedException(String name) {
        super(String.format("Machine not added to this job: %s",name));
    }
}
