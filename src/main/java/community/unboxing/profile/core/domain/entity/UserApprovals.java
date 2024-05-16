package community.unboxing.profile.core.domain.entity;

import java.sql.Timestamp;

import community.unboxing.profile.core.enums.UserApprovalStatus;
import community.unboxing.profile.core.enums.ApprovalPurpose;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_approvals")
public class UserApprovals extends BaseEntity {

    @Column
    private Integer submittedByUser;

    @Column
    @Enumerated(EnumType.STRING)
    private UserApprovalStatus approvalStatus;

    @Column
    private String approvedBy;

    @Column
    private String approverRoleId;

    @Column
    private Timestamp approvalDate;

    @Column
    @Enumerated(EnumType.STRING)
    private ApprovalPurpose approvalPurpose;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
