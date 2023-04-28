package sv3advproject.erp_project.dtos.job_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddMachiningHoursCommand {

    @Positive(message = "Job id must be greater than 0")
    @Schema(description = "Id of job",example = "1")
    private long jobId;
    @Positive(message = "Machine id must be greater than 0")
    @Schema(description = "Id of the machine",example = "1")
    private long machineId;
    @Positive(message = "Machining hour must be greater than 0")
    @Schema(description = "Hours of machining",example = "10")
    private int hours;
}
