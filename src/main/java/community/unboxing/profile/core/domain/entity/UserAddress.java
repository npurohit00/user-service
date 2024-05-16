package community.unboxing.profile.core.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "user_addresses")
public class UserAddress extends BaseEntity {
    
    @Column
    private String addressLine1;
    
    @Column
    private String addressLine2;
    
    @Column
    private String city;
    
    @Column
    private String state;
    
    @Column
    private String country;
    
    @Column
    private String pincode;
    
    @Column
    private String atlPhoneNumber;
    
    @Column
    private String addressType;

    @PrimaryKeyJoinColumn
    @OneToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;
}
