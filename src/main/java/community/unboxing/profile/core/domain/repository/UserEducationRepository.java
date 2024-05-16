package community.unboxing.profile.core.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import community.unboxing.profile.core.domain.entity.UserEducation;

@Repository
public interface UserEducationRepository extends JpaRepository<UserEducation, UUID> {
    
}
