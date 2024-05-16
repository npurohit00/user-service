package community.unboxing.profile.core.domain.entity;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "user_profiles")
public class UserProfile extends BaseEntity {

    @Column
    private String firstname;
    
    @Column
    private String lastname;
    
    @Column
    private Date dateOfBirth;
    
    @Column
    private String gender;
    
    @Column
    private String district;
    
    @Column
    private String state;

    @Column
    private String country;

    @Column
    private String pincode;
    
    @Column
    private String profileWebsiteUrl;
    
    @Column
    private String linkedInUrl;
    
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
    private List<UserAddress> userAddress;

    @OneToOne
    @JoinColumn(name = "education_id", referencedColumnName = "id")
    private UserEducation userEducation;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
    private List<UserGrades> userGrades;

    @OneToOne
    @JoinColumn(name = "work_experience_id", referencedColumnName = "id")
    private UserWorkExperience userWorkExperience;

    @OneToOne
    @JoinColumn(name = "preference_id", referencedColumnName = "id")
    private UserPreference userPreference;

    
}
