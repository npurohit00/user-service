package community.unboxing.profile.core.service;

import java.util.Map;
import java.util.Optional;

import community.unboxing.profile.adapters.web.dto.AdminApprovalRequest;

public interface AdminService {
    
    Optional<Map<String, Object>> approveUserProfile(AdminApprovalRequest request);

    Optional<Map<String, Object>> getUsersForApproval();

}
