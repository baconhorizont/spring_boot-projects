package sv3advproject.erp_project.exceptions;

public class JobNotRunningException extends RuntimeException{

    public JobNotRunningException(long id) {
        super(String.format("Job not running id: %d",id));
    }
}
