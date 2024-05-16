package community.unboxing.profile.core.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import community.unboxing.profile.core.domain.entity.UserPreference;
import community.unboxing.profile.core.domain.entity.UserProfile;

@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreference, UUID> {

    Optional<UserPreference> findByProfile(UserProfile profile);
    
}
