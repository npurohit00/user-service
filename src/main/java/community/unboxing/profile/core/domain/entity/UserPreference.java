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
@Table(name = "user_preferences")
public class UserPreference extends BaseEntity {
    
    @Column
    @Builder.Default()
    private boolean email = true;

    @Column
    private boolean sms;

    @Column
    private boolean newsletterSubscription;

    @Column
    @Builder.Default()
    private boolean profileCompletionRemainders = true;

    @Column
    private boolean assignmentNotifications;

    @Column
    private boolean systemUpdates;

    @PrimaryKeyJoinColumn
    @OneToOne
    private UserProfile profile;

}
