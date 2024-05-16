package community.unboxing.profile.adapters.web.dto;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import community.unboxing.profile.core.enums.VerificationChannel;
import community.unboxing.profile.core.enums.VerificationPurpose;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OTPRequest {

    @NotBlank(message = "OTP code cannot be blank")
    private String otp;

    @NotNull(message = "Verification purpose cannot be null")
    @AssertTrue(message = "Invalid verification purpose")
    private VerificationPurpose purpose;

    @NotNull(message = "Verification channel cannot be null")
    @AssertTrue(message = "Invalid verification channel")
    private VerificationChannel channel;

    @NotBlank(message = "Verification ID cannot be blank")
    private String verificationId;

    @NotBlank(message = "User ID cannot be blank")
    private String userId;

    public boolean isValidPurpose() {
        return Arrays.asList(VerificationPurpose.values()).contains(purpose);
    }

    public boolean isValidChannel() {
        return Arrays.asList(VerificationChannel.values()).contains(channel);
    }

}
