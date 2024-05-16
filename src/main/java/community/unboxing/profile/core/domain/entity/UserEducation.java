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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_education")
public class UserEducation extends BaseEntity {
    
    @PrimaryKeyJoinColumn
    @OneToOne
    private UserProfile profile;

    @Column
    private String institutionName;

    @Column
    private String educationBackground;

    @Column
    private String fieldOfStudy;

    @Column
    private Integer endYear;

    @Column
    private String status;

    @Column
    private String achievements;

}
