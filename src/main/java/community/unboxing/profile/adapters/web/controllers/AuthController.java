package community.unboxing.profile.adapters.web.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import community.unboxing.profile.adapters.web.dto.UbcApiResponse;
import community.unboxing.profile.adapters.web.dto.UbcApiResponse.ErrorMessages;
import community.unboxing.profile.adapters.web.dto.LoginRequest;
import community.unboxing.profile.core.service.UserService;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/auth/api/v1")
@RestController
@Slf4j
public class AuthController {

    private UserService userService;

    AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UbcApiResponse<?>> login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<Map<String, Object>> jwtToken = userService.login(loginRequest);
            return ResponseEntity.ok(UbcApiResponse.builder()
                    .success(jwtToken.isPresent())
                    .result(jwtToken.get())
                    .build());
        } catch (AuthenticationException e) {
            log.error("Invalid login credentials", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    UbcApiResponse.builder()
                            .success(false)
                            .errorMessages(List.of(ErrorMessages.builder()
                                    .errorMessage("Invalid login credentials")
                                    .errorCode("UBCUS0005")
                                    .build()))
                            .build());
        } catch (Exception e) {
            log.error("Error during login", e);
            return ResponseEntity.internalServerError().body(
                    UbcApiResponse.builder()
                            .success(false)
                            .errorMessages(List.of(ErrorMessages.builder()
                                    .errorMessage("Error logging in user")
                                    .errorCode("UBCUS0005")
                                    .build()))
                            .build());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<UbcApiResponse<?>> logout(@RequestBody LoginRequest request) {
        SecurityContextHolder.clearContext();
        Optional<Map<String, Object>> loguOptional = userService.logout(request);
        return ResponseEntity.ok(UbcApiResponse.builder()
                .success(loguOptional.isPresent())
                .result(loguOptional.get())
                .build());
    }

}
