package sv3advproject.erp_project.exceptions;

public class MachineAlreadyAddedException extends RuntimeException{

    public MachineAlreadyAddedException(long machineId,long jobId) {
        super(String.format("Machine already added id: %d to this job id: %d",machineId,jobId));
    }
}
