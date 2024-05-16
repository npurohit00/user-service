package community.unboxing.profile.core.service;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import community.unboxing.profile.adapters.web.dto.AdminApprovalRequest;
import community.unboxing.profile.core.domain.entity.UserApprovals;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommunityAdminService implements AdminService {

    private UserService userService;
    private EmailService emailService;

    CommunityAdminService(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public Optional<Map<String, Object>> approveUserProfile(AdminApprovalRequest request) {

        Optional<Map<String, Object>> userApprovalOptional = userService.findUserForApproval(request.getUserId());

        log.info("UserService response: {}", userApprovalOptional);

        if (!userApprovalOptional.isPresent()) {
            log.info("User not found");
            return Optional.of(Map.of(
                    "message",
                    "User not found",
                    "isProfileApproved", false));
        }

        UserApprovals user = (UserApprovals) userApprovalOptional.get().get("approvals");
        user.setApprovalStatus(request.getApprovalStatus());
        user.setApprovedBy(request.getApprovedBy());
        user.setApproverRoleId(request.getApproverRoleId());
        user.setApprovalDate(new Timestamp(request.getApprovalDate().getTime()));
        userService.updateUserApproval(user);

        emailService.sendEmailSignUpConfirmation(user.getUser().getEmail(), user.getUser().getUsername());

        return Optional.of(Map.of(
                "message",
                "User Profile Approved Successfully",
                "isProfileApproved", true,
                "userId", user.getUser().getId()));
    }

    @Override
    public Optional<Map<String, Object>> getUsersForApproval() {
        return userService.getAllUsersForSignUpApproval();
    }

}
