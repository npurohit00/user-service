package community.unboxing.profile.core.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_engineering_skills")
public class UserEngineeringSkills extends BaseEntity {
    
    @Column
    private String skillName;
    
    @Column
    private Integer experience;
    
    @Column(name = "self_rating", nullable = true, columnDefinition = "integer check (self_rating <= 10)")
    private Integer selfRating;
    
    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "work_experience_id")
    private UserWorkExperience workExperience;

}