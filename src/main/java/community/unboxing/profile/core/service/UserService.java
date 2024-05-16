package community.unboxing.profile.core.service;

import java.util.Map;
import java.util.Optional;

import community.unboxing.profile.adapters.web.dto.LoginRequest;
import community.unboxing.profile.adapters.web.dto.OTPRequest;
import community.unboxing.profile.adapters.web.dto.PasswordRequest;
import community.unboxing.profile.adapters.web.dto.SignUpRequest;
import community.unboxing.profile.adapters.web.dto.UserProfileDetails;
import community.unboxing.profile.core.domain.entity.UserApprovals;

public interface UserService {
    
    Optional<Map<String, Object>> checkUserExists(String email); //boolean

    Optional<Map<String, Object>> createUser(SignUpRequest request); //string

    Optional<Map<String, Object>> sendOTP(OTPRequest request); //string

    Optional<Map<String, Object>> verifyOTP(OTPRequest request); //boolean

    Optional<Map<String, Object>> createPassword(PasswordRequest request); //boolean

    Optional<Map<String, Object>> updateUserProfile(UserProfileDetails request); //string

    Optional<Map<String, Object>> resetPassword(PasswordRequest request); //boolean

    Optional<Map<String, Object>> deleteUser(String userId); //boolean

    Optional<Map<String, Object>> login(LoginRequest request); //string

    Optional<Map<String, Object>> logout(LoginRequest request); //string

    Optional<Map<String, Object>> findUserForApproval(String email); //userapprovals

    Optional<Map<String, Object>> updateUserApproval(UserApprovals userApprovals); //void

    Optional<Map<String, Object>> getAllUsersForSignUpApproval(); //list<userProfileDetails>

}
