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

import community.unboxing.profile.adapters.web.dto.AdminApprovalRequest;
import community.unboxing.profile.adapters.web.dto.UbcApiResponse;
import community.unboxing.profile.adapters.web.dto.UbcApiResponse.ErrorMessages;
import community.unboxing.profile.core.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

@RequestMapping("/admin/api/v1")
@RestController
@Slf4j
public class AdminController {

    private AdminService adminService;

    AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/approve-profile")
    @Operation(summary = "Admin API to approve/reject users requested for signup, once approved user gets activated", responses = {
            @ApiResponse(responseCode = "200", description = "Update and notify users as per the approval", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UbcApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UbcApiResponse.class)))
    })
    public ResponseEntity<UbcApiResponse<?>> approveUserProfile(@RequestBody AdminApprovalRequest request) {
        try {
            Optional<Map<String, Object>> approved = adminService.approveUserProfile(request);
            return ResponseEntity.ok(UbcApiResponse.builder()
                    .success(approved.isPresent())
                    .result(approved.get())
                    .build());
        } catch (Exception e) {
            log.error("Error approving user profile for userId: " + request.getUserId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    UbcApiResponse.builder()
                            .success(false)
                            .errorMessages(List.of(ErrorMessages.builder()
                                    .errorMessage("Error approving users registration")
                                    .errorCode("UBCAD0001")
                                    .build()))
                            .build());
        }
    }

    @GetMapping("/pending-profiles")
    @Operation(summary = "API to fetch all user signup requests to be approved by the admin", responses = {
            @ApiResponse(responseCode = "200", description = "List of pending user details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UbcApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UbcApiResponse.class)))
    })
    public ResponseEntity<UbcApiResponse<?>> getMethodName() {
        try {
            Optional<Map<String, Object>> pendingUsers = adminService.getUsersForApproval();
            return ResponseEntity.ok(UbcApiResponse.builder()
                    .success(pendingUsers.isPresent())
                    .result(pendingUsers.get())
                    .build());
        } catch (Exception e) {
            log.error("Error while fetching approval pending user profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    UbcApiResponse.builder()
                            .success(false)
                            .errorMessages(List.of(ErrorMessages.builder()
                                    .errorMessage("Error fetching user profiles")
                                    .errorCode("UBCAD0002")
                                    .build()))
                            .build());
        }
    }

}
