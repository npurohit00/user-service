package community.unboxing.profile.core.domain.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
@Table(name = "user_sessions")
public class UserSessions extends BaseEntity {
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrimaryKeyJoinColumn
    @OneToOne
    private UserAuditLog userAuditLog;

    @Column
    private Timestamp loginTime;
    
    @Column
    private Timestamp logoutTime;
    
    @Column
    private String ipAddress;
    
    @Column
    private String deviceInfo;

    @Column
    private String browserInfo;
    
    @Column
    private boolean isActive;

}
