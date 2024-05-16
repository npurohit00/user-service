package community.unboxing.profile.adapters.web.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import community.unboxing.profile.adapters.web.dto.UbcApiResponse;
import community.unboxing.profile.adapters.web.dto.UbcApiResponse.ErrorMessages;
import community.unboxing.profile.core.service.UserService;
import community.unboxing.profile.adapters.web.dto.PasswordRequest;
import community.unboxing.profile.adapters.web.dto.UserProfileDetails;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/profile/api/v1")
@RestController
@Slf4j
public class UserProfileController {

    private UserService userService;

    UserProfileController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/change-password")
        public ResponseEntity<UbcApiResponse<?>> changePassword(@RequestBody PasswordRequest request) {
                try {
                        Optional<Map<String, Object>> changePasswordResult = userService.resetPassword(request);
                        if (!changePasswordResult.isPresent()) {
                                return ResponseEntity.internalServerError().body(
                                                UbcApiResponse.builder()
                                                                .success(false)
                                                                .errorMessages(List.of(ErrorMessages.builder()
                                                                                .errorMessage("Error changing password")
                                                                                .errorCode("UBCUS0005")
                                                                                .build()))
                                                                .build());
                        }

                        return ResponseEntity.ok(UbcApiResponse.builder()
                                        .success(changePasswordResult.isPresent())
                                        .result(changePasswordResult.get())
                                        .build());
                } catch (Exception e) {
                        log.error("Error resetting password for user: " + request.getUserId(), e);
                        return ResponseEntity.internalServerError().body(
                                        UbcApiResponse.builder()
                                                        .success(false)
                                                        .errorMessages(List.of(ErrorMessages.builder()
                                                                        .errorMessage("Error changing password")
                                                                        .errorCode("UBCUS0005")
                                                                        .build()))
                                                        .build());
                }
        }

        @PutMapping("/update-user-profile")
        public ResponseEntity<UbcApiResponse<?>> createOrUpdateUserProfile(
                        @RequestBody UserProfileDetails request) {

                try {
                        Optional<Map<String, Object>> profileUpdateOptional = userService.updateUserProfile(request);

                        if (!profileUpdateOptional.isPresent()) {
                                return ResponseEntity.internalServerError().body(
                                                UbcApiResponse.builder()
                                                                .success(false)
                                                                .errorMessages(List.of(ErrorMessages.builder()
                                                                                .errorMessage("Error updating user profile")
                                                                                .errorCode("UBCUS0005")
                                                                                .build()))
                                                                .build());
                        }

                        return ResponseEntity.ok(UbcApiResponse.builder()
                                        .success(profileUpdateOptional.isPresent())
                                        .result(profileUpdateOptional.get())
                                        .build());
                } catch (Exception e) {
                        log.error("Error updating user profile", e);
                        return ResponseEntity.internalServerError().body(
                                        UbcApiResponse.builder()
                                                        .success(false)
                                                        .errorMessages(List.of(ErrorMessages.builder()
                                                                        .errorMessage("Error updating user profile")
                                                                        .errorCode("UBCUS0005")
                                                                        .build()))
                                                        .build());
                }
        }

        @GetMapping("/preferences/{userId}")
        public ResponseEntity<UbcApiResponse<?>> userNotifications(@PathVariable String userId) {
                try {
                        // List<Notification> notifications = getUserNotifications(user);
                        List<String> notifications = Collections.emptyList();
                        return ResponseEntity.ok(UbcApiResponse.builder()
                                        .success(true)
                                        .result(notifications)
                                        .build());
                } catch (Exception e) {
                        log.error("Error getting user notifications for user: " + userId, e);
                        return ResponseEntity.internalServerError().body(
                                        UbcApiResponse.builder()
                                                        .success(false)
                                                        .errorMessages(List.of(ErrorMessages.builder()
                                                                        .errorMessage("Error fetching user preferences")
                                                                        .errorCode("UBCUS0005")
                                                                        .build()))
                                                        .build());
                }
        }

        @DeleteMapping("/{user}")
        public ResponseEntity<UbcApiResponse<?>> deleteUser(@PathVariable String user) {
                try {
                        Optional<Map<String, Object>> deleted = userService.deleteUser(user);
                        return ResponseEntity.ok(UbcApiResponse.builder()
                                        .success(deleted.isPresent())
                                        .result(deleted.get())
                                        .build());
                } catch (Exception e) {
                        log.error("Error deleting user: " + user, e);
                        return ResponseEntity.internalServerError().body(
                                        UbcApiResponse.builder()
                                                        .success(false)
                                                        .errorMessages(List.of(ErrorMessages.builder()
                                                                        .errorMessage("Error fetching user preferences")
                                                                        .errorCode("UBCUS0005")
                                                                        .build()))
                                                        .build());
                }
        }

}
