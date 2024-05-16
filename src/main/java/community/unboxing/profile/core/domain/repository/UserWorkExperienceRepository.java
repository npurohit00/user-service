package community.unboxing.profile.core.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import community.unboxing.profile.core.domain.entity.UserProfile;
import community.unboxing.profile.core.domain.entity.UserWorkExperience;

@Repository
public interface UserWorkExperienceRepository extends JpaRepository<UserWorkExperience, UUID> {

    Optional<UserWorkExperience> findByProfile(UserProfile profile);
    
}
