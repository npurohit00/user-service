package community.unboxing.profile.core.domain.entity;

import java.sql.Timestamp;

import community.unboxing.profile.core.enums.VerificationChannel;
import community.unboxing.profile.core.enums.VerificationPurpose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_verification")
public class UserVerification extends BaseEntity {
    
    @Column
    private String verificationCode;
    
    @Column
    private boolean isVerified;

    @Column
    private boolean isExpired;
    
    @Column
    @Enumerated(EnumType.STRING)
    private VerificationChannel verificationChannel;
    
    @Column
    @Enumerated(EnumType.STRING)
    private VerificationPurpose verificationPurpose;
    
    @Column
    private Timestamp validTill;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
