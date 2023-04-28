package sv3advproject.erp_project.dtos.job_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import sv3advproject.erp_project.models.JobStatus;
import sv3advproject.erp_project.validators.job.JobStatusValid;

@Data
@Builder
public class UpdateJobStatusCommand {
    @Positive(message = "Job id must be greater than 0")
    @Schema(description = "Id of job",example = "1")
    private long jobId;
    @JobStatusValid(anyOf = {JobStatus.NEW,JobStatus.RUNNING,JobStatus.FINISHED,JobStatus.FINISHED,JobStatus.INVOICED})
    @Schema(description = "Status of the job",example = "RUNNING")
    private JobStatus jobStatus;
}
