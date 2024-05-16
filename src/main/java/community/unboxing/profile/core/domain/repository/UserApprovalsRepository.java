package community.unboxing.profile.core.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import community.unboxing.profile.core.domain.entity.User;
import community.unboxing.profile.core.domain.entity.UserApprovals;
import java.util.List;
import community.unboxing.profile.core.enums.UserApprovalStatus;


@Repository
public interface UserApprovalsRepository extends JpaRepository<UserApprovals, UUID> {
    
    Optional<UserApprovals> findByUser(User user);

    Optional<List<UserApprovals>> findByApprovalStatus(UserApprovalStatus approvalStatus);

}
