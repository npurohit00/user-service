package community.unboxing.profile.adapters.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordRequest {

    @NotBlank(message = "User ID cannot be blank")
    private String userId;

    @NotBlank(message = "Current password cannot be blank")
    private String password;

    @NotBlank(message = "Confirmation password cannot be blank")
    @AssertTrue(message = "Confirmation password doesn't match password")
    private String confirmationPassword;

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 8, message = "New password must be at least 8 characters long")
    private String newPassword;

    private boolean isOtpVerified;

    @NotBlank(message = "Verification ID cannot be blank")
    private String verificationId;

}
