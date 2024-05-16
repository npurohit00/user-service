package community.unboxing.profile.adapters.web.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import community.unboxing.profile.adapters.web.dto.UbcApiResponse;
import community.unboxing.profile.adapters.web.dto.OTPRequest;
import community.unboxing.profile.adapters.web.dto.PasswordRequest;
import community.unboxing.profile.adapters.web.dto.SignUpRequest;
import community.unboxing.profile.adapters.web.dto.UbcApiResponse.ErrorMessages;
import community.unboxing.profile.core.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/user/api/v1")
@RestController
@Slf4j
public class UserController {

        private UserService userService;

        UserController(UserService userService) {
                this.userService = userService;
        }

        @PostMapping("/check-user-exists")
        @Operation(summary = "Checks if a user exists based on email", responses = {
                        @ApiResponse(responseCode = "200", description = "User exists information", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UbcApiResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UbcApiResponse.class)))
        })
        public ResponseEntity<UbcApiResponse<?>> isUserRegistered(@RequestBody SignUpRequest request) {
                try {
                        Optional<Map<String, Object>> existsOptional = userService.checkUserExists(request.getEmail());
                        if (!existsOptional.isPresent()) {
                                log.info("checkUserExists() has empty respsonse");
                                return ResponseEntity.internalServerError().body(
                                                UbcApiResponse.builder()
                                                                .success(false)
                                                                .errorMessages(List.of(ErrorMessages.builder()
                                                                                .errorMessage("Error checking user existence")
                                                                                .errorCode("UBCUS0001")
                                                                                .build()))
                                                                .build());
                        }
                        return ResponseEntity.ok(UbcApiResponse.builder()
                                        .success(existsOptional.isPresent())
                                        .result(existsOptional.get())
                                        .build());
                } catch (Exception e) {
                        log.error("Error checking user existence for email: " + request.getEmail(), e);
                        return ResponseEntity.internalServerError().body(
                                        UbcApiResponse.builder()
                                                        .success(false)
                                                        .errorMessages(List.of(ErrorMessages.builder()
                                                                        .errorMessage("Error checking user existence")
                                                                        .errorCode("UBCUS0001")
                                                                        .build()))
                                                        .build());
                }
        }

        @PostMapping("/send-otp")
        @Operation(summary = "Sends OTP to the user email", responses = {
                        @ApiResponse(responseCode = "200", description = "Generates/resend OTP to the User email", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UbcApiResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UbcApiResponse.class)))
        })
        public ResponseEntity<UbcApiResponse<?>> resendOTP(@RequestBody OTPRequest request) {
                try {
                        Optional<Map<String, Object>> otpResult = userService.sendOTP(request);
                        if (!otpResult.isPresent()) {
                                log.info("sendOTP() has empty respsonse");
                                return ResponseEntity.internalServerError().body(
                                                UbcApiResponse.builder()
                                                                .success(false)
                                                                .errorMessages(List.of(ErrorMessages.builder()
                                                                                .errorMessage("Error sending OTP")
                                                                                .errorCode("UBCUS0002")
                                                                                .build()))
                                                                .build());
                        }
                        return ResponseEntity.ok(UbcApiResponse.builder()
                                        .success(otpResult.isPresent())
                                        .result(otpResult.get())
                                        .build());
                } catch (Exception e) {
                        log.error("Error sending OTP for verification id: " + request.getVerificationId(), e);
                        return ResponseEntity.internalServerError().body(
                                        UbcApiResponse.builder()
                                                        .success(false)
                                                        .errorMessages(List.of(ErrorMessages.builder()
                                                                        .errorMessage("Error sending OTP")
                                                                        .errorCode("UBCUS0002")
                                                                        .build()))
                                                        .build());
                }
        }

        @PostMapping("/verify-otp")
        @Operation(summary = "Verify OTP entered by the user", responses = {
                        @ApiResponse(responseCode = "200", description = "Validates user provided OTP", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UbcApiResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UbcApiResponse.class)))
        })
        public ResponseEntity<UbcApiResponse<?>> verifyOTP(@RequestBody OTPRequest request) {
                try {
                        // String id = request.getVerificationId();
                        // String otp = request.getOtp();
                        // VerificationPurpose purpose = request.getPurpose();
                        // if (id == null || id.isEmpty() || otp == null || otp.isEmpty() || purpose ==
                        // null) {
                        // return ResponseEntity.badRequest().body(
                        // UbcApiResponse.builder()
                        // .success(false)
                        // .errorMessages(List.of(ErrorMessages.builder()
                        // .errorMessage("Mandatory fields must not be null or empty")
                        // .errorCode("UBCUS0003")
                        // .build()))
                        // .build());
                        // }
                        Optional<Map<String, Object>> verified = userService.verifyOTP(request);
                        if (!verified.isPresent()) {
                                return ResponseEntity.internalServerError().body(
                                                UbcApiResponse.builder()
                                                                .success(false)
                                                                .errorMessages(List.of(ErrorMessages.builder()
                                                                                .errorMessage("Error verifying OTP")
                                                                                .errorCode("UBCUS0004")
                                                                                .build()))
                                                                .build());
                        }
                        return ResponseEntity.ok(UbcApiResponse.builder()
                                        .success(verified.isPresent())
                                        .result(verified.get())
                                        .build());
                } catch (Exception e) {
                        log.error("Error verifying OTP for verification id: " + request.getVerificationId(), e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                        UbcApiResponse.builder()
                                                        .success(false)
                                                        .errorMessages(List.of(ErrorMessages.builder()
                                                                        .errorMessage("Error verifying OTP")
                                                                        .errorCode("UBCUS0004")
                                                                        .build()))
                                                        .build());
                }
        }

        @PostMapping("/signup")
        @Operation(summary = "Onboarding new user, this API will store the basic user information and triggers OTP to email for validation", responses = {
                        @ApiResponse(responseCode = "200", description = "Registers user if user not exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UbcApiResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UbcApiResponse.class)))
        })
        public ResponseEntity<UbcApiResponse<?>> createUser(@RequestBody SignUpRequest request) {
                try {
                        Optional<Map<String, Object>> signupResult = userService.createUser(request);
                        if (!signupResult.isPresent()) {
                                return ResponseEntity.internalServerError().body(
                                                UbcApiResponse.builder()
                                                                .success(false)
                                                                .errorMessages(List.of(ErrorMessages.builder()
                                                                                .errorMessage("Error creating user")
                                                                                .errorCode("UBCUS0005")
                                                                                .build()))
                                                                .build());
                        }
                        return ResponseEntity.ok(UbcApiResponse.builder()
                                        .success(signupResult.isPresent())
                                        .result(signupResult.get())
                                        .build());
                } catch (Exception e) {
                        log.error("Error creating user: ", e);
                        return ResponseEntity.internalServerError().body(
                                        UbcApiResponse.builder()
                                                        .success(false)
                                                        .errorMessages(List.of(ErrorMessages.builder()
                                                                        .errorMessage("Error creating user")
                                                                        .errorCode("UBCUS0005")
                                                                        .build()))
                                                        .build());
                }
        }

        @PostMapping("/set-password")
        @Operation(summary = "This API allows new user to set new password, this takes OTP verification id from the Signup API", responses = {
                        @ApiResponse(responseCode = "200", description = "Set new user provided password", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UbcApiResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UbcApiResponse.class)))
        })
        public ResponseEntity<UbcApiResponse<?>> setPassword(@RequestBody PasswordRequest request) {
                try {
                        Optional<Map<String, Object>> passwordSetResult = userService.createPassword(request);
                        if (!passwordSetResult.isPresent()) {
                                return ResponseEntity.internalServerError().body(
                                                UbcApiResponse.builder()
                                                                .success(false)
                                                                .errorMessages(List.of(ErrorMessages.builder()
                                                                                .errorMessage("Error setting password")
                                                                                .errorCode("UBCUS0005")
                                                                                .build()))
                                                                .build());
                        }

                        return ResponseEntity.ok(UbcApiResponse.builder()
                                        .success(passwordSetResult.isPresent())
                                        .result(passwordSetResult.get())
                                        .build());
                } catch (Exception e) {
                        log.error("Error resetting password for user: " + request.getUserId(), e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                        UbcApiResponse.builder()
                                                        .success(false)
                                                        .errorMessages(List.of(ErrorMessages.builder()
                                                                        .errorMessage("Error setting password")
                                                                        .errorCode("UBCUS0005")
                                                                        .build()))
                                                        .build());
                }
        }

}
