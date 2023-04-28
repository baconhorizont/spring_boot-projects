package sv3advproject.erp_project.dtos.job_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import sv3advproject.erp_project.models.OrderType;
import sv3advproject.erp_project.validators.job.OrderTypeValid;

import java.time.LocalDate;

@Data
@Builder
public class CreateJobCommand {
    @NotBlank(message = "Customer name must not blank")
    @Size(min = 3,message = "Customer name size must be greater than 2")
    @Schema(description = "Name of the customer",example = "Denso Hungary Kft")
    private String customer;
    @PastOrPresent(message = "Order date must be int the past or present")
    @Schema(description = "Date of the order",example = "2023-04-15")
    private LocalDate orderDate;
    @Future(message = "Deadline date must be in the future")
    @Schema(description = "Date of the order deadline",example = "2024-05-15")
    private LocalDate deadline;
    @OrderTypeValid(anyOf = {OrderType.STANDARD,OrderType.PRIORITY,OrderType.DISCOUNTED})
    @Schema(description = "Type of the order",example ="STANDARD")
    private OrderType orderType;
    @Schema(description = "Estimated machining hours of the job",example = "200")
    @Positive(message = "Machining hours must be positive and greater than 0")
    private int estimatedMachiningHours;
}
