package community.unboxing.profile.adapters.web.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import community.unboxing.profile.core.enums.CommunityUserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private CommunityUserType userType;

    private String contactNumber;

    @NotBlank
    @JsonFormat(pattern = "ddMMyyyy", timezone = "UTC")
    private Date dateOfBirth;
    
    private String district;
    private String state;
    private String country;
}
