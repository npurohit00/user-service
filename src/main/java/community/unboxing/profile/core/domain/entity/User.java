package community.unboxing.profile.core.domain.entity;

import community.unboxing.profile.core.enums.CommunityUserType;
import community.unboxing.profile.core.enums.UserApprovalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "users")
public class User extends BaseEntity {
    
    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String email;

    @Column
    private String contactNumber;

    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private CommunityUserType userType;

    @Enumerated(EnumType.STRING)
    private UserApprovalStatus approvalStatus;

    // @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    // private UserProfile userProfile;

    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    // private List<UserSessions> userSessions;

    // @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    // private UserVerification userVerification;

    // @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    // private UserApprovals userApprovals;

}
