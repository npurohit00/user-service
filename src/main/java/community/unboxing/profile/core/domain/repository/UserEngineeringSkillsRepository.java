package community.unboxing.profile.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import community.unboxing.profile.core.domain.entity.UserEngineeringSkills;
import community.unboxing.profile.core.domain.entity.UserWorkExperience;

@Repository
public interface UserEngineeringSkillsRepository extends JpaRepository<UserEngineeringSkills, UUID> {

    Optional<List<UserEngineeringSkills>> findByWorkExperience(UserWorkExperience experience);

    Optional<UserEngineeringSkills> findByWorkExperienceAndSkillName(UserWorkExperience experience, String skillName);
    
}
