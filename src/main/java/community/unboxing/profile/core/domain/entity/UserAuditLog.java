package community.unboxing.profile.core.domain.entity;

import java.sql.Timestamp;

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
@Table(name = "audit_log")
public class UserAuditLog extends BaseEntity {
    
    @Column
    private String tableName;

    @Column
    private String operationType;

    @Column
    private String primaryKeyValue;

    @Column
    private String changedColumn;

    @Column
    private String oldValue;

    @Column
    private String newValue;

    @Column
    private Timestamp changTimestamp;

    @PrimaryKeyJoinColumn
    @OneToOne
    private User user;

    @OneToOne(mappedBy = "userAuditLog")
    private UserSessions userSessions;

}
