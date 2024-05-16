package community.unboxing.profile.core.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
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
@Table(name = "user_work_experience")
public class UserWorkExperience extends BaseEntity {
    
    @Column
    private String companyName;
    
    @Column
    private Integer totalYearsOfExperience;
    
    @Column
    private String latestRoleDesignation;
    
    @Column
    private String skillsStatus;
    
    @Column
    private String catalogVariants;
    
    @PrimaryKeyJoinColumn
    @OneToOne
    private UserProfile profile;

}
