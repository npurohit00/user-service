package community.unboxing.profile.core.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import community.unboxing.profile.core.domain.entity.UserSessions;

@Repository
public interface UserSessionsRepository extends JpaRepository<UserSessions, UUID> {

    @Query("SELECT us FROM UserSessions us WHERE us.user.email = :email AND us.isActive = true")
    Optional<UserSessions> findByIsActiveAndEmail(String email);

    Optional<UserSessions> findByIdAndIsActive(UUID sessionId, boolean isActive);
    
}
