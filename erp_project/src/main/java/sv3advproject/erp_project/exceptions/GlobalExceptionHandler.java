package sv3advproject.erp_project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JobNotRunningException.class)
    public ProblemDetail handleJobNotRunningException(JobNotRunningException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        detail.setType(URI.create("jobs/job-not-running"));
        return detail;
    }

    @ExceptionHandler(EmployeeNotQualifiedException.class)
    public ProblemDetail handleEmployeeNotQualifiedException(EmployeeNotQualifiedException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        detail.setType(URI.create("machines/employee-not-qualified"));
        return detail;
    }

    @ExceptionHandler(MachineNotFoundException.class)
    public ProblemDetail handleMachineNotFoundException(MachineNotFoundException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("machines/machine-not-found"));
        return detail;
    }

    @ExceptionHandler(MachineNotAddedException.class)
    public ProblemDetail handleMachineNotAddedException(MachineNotAddedException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        detail.setType(URI.create("jobs/machine-not-added"));
        return detail;
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ProblemDetail handleEmployeeNotFoundException(EmployeeNotFoundException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("employees/employee-not-found"));
        return detail;
    }

    @ExceptionHandler(JobNotFoundException.class)
    public ProblemDetail handleJobNotFoundException(JobNotFoundException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("jobs/job-not-found"));
        return detail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE,e.getFieldError().getDefaultMessage());
        detail.setType(URI.create("input-data/not-valid"));
        return detail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleJsonParseException(HttpMessageNotReadableException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE,e.getMessage());
        detail.setType(URI.create("input-data/not-valid"));
        return detail;
    }
}


