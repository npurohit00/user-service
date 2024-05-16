package community.unboxing.profile.adapters.web.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import community.unboxing.profile.core.enums.UserApprovalStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminApprovalRequest {

    @NotNull(message = "User ID cannot be null")
    private String userId;

    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Approval status cannot be null")
    private UserApprovalStatus approvalStatus;

    @NotBlank(message = "Approved by cannot be blank")
    private String approvedBy;

    @NotBlank(message = "Approver role ID cannot be blank")
    private String approverRoleId;

    @JsonFormat(pattern = "ddMMyyyyHHmmss", timezone = "UTC")
    private Date approvalDate;

}
