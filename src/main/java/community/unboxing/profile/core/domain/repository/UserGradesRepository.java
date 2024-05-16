package community.unboxing.profile.core.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import community.unboxing.profile.core.domain.entity.UserGrades;
import community.unboxing.profile.core.domain.entity.UserProfile;

@Repository
public interface UserGradesRepository extends JpaRepository<UserGrades, UUID> {

    Optional<UserGrades> findByUserProfile(UserProfile profile);

    Optional<UserGrades> findByUserProfileAndCatalogIdAndProductId(UserProfile profile, Long catalogId, Long productId);
    
}
