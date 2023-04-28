package sv3advproject.erp_project.exceptions;

public class JobNotFoundException extends RuntimeException{

    public JobNotFoundException(long id) {
        super(String.format("Job not found with id: %d", id));
    }
}
