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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_grades")
public class UserGrades extends BaseEntity {

    @Column
    private Long catalogId;
    
    @Column
    private String catalogName;
    
    @Column
    private String catalogGrade;
    
    @Column
    private Integer catalogGradeStar;
    
    @Column
    private Double catalogGradePercentage; 
    
    @Column
    private Long productId;
    
    @Column
    private String productName; 
    
    @Column
    private Double productGrade;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

}
