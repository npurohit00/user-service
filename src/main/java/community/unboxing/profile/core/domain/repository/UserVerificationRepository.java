package community.unboxing.profile.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import community.unboxing.profile.core.domain.entity.UserVerification;
import community.unboxing.profile.core.enums.VerificationChannel;
import community.unboxing.profile.core.enums.VerificationPurpose;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification, UUID> {

    @Query("SELECT uv FROM UserVerification uv WHERE uv.user.email = :email AND uv.verificationCode = :otp")
    Optional<UserVerification> findByEmailAndVerificationCode(String email, String otp);

    @Query("SELECT uv FROM UserVerification uv WHERE uv.user.email = ?1")
    Optional<UserVerification> findByEmail(String email);

    Optional<UserVerification> findByVerificationCode(String verificationId);

    @Query("SELECT uv FROM UserVerification uv WHERE uv.user.id in (?1)")
    List<UserVerification> findAllByUserId(List<UUID> pendingUserIdList);

    Optional<UserVerification> findByIdAndVerificationCodeAndVerificationPurpose(UUID verificationId, String otp,
            VerificationPurpose purpose);

    Optional<UserVerification> findByUserIdAndVerificationChannelAndVerificationPurpose(UUID userId, VerificationChannel channel,
            VerificationPurpose purpose);

    @Query("SELECT DISTINCT uv FROM UserVerification uv WHERE uv.user.id in (?1)")
    Optional<UserVerification> findByUser(UUID userId);

}
