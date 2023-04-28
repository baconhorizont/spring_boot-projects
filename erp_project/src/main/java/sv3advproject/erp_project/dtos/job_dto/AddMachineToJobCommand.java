package sv3advproject.erp_project.dtos.job_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddMachineToJobCommand {

    @Positive(message = "Job id must be greater than 0")
    @Schema(description = "Id of job",example = "1")
    private long jobId;
    @Positive(message = "Machine id must be greater than 0")
    @Schema(description = "Id of the machine",example = "1")
    private long machineId;
}
