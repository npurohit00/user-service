package community.unboxing.profile.core.service;

import org.springframework.stereotype.Service;

import community.unboxing.profile.core.domain.entity.User;
import community.unboxing.profile.core.domain.entity.UserVerification;
import community.unboxing.profile.core.domain.repository.UserRepository;
import community.unboxing.profile.core.domain.repository.UserVerificationRepository;
import community.unboxing.profile.core.enums.VerificationChannel;
import community.unboxing.profile.core.enums.VerificationPurpose;
import community.unboxing.profile.utils.OTPUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserVerificationService {

    private static final long OTP_EXPIRATION_MINUTES = 15;

    private UserVerificationRepository verificationRepository;
    private UserRepository userRepository;

    public UserVerificationService(UserVerificationRepository verificationRepository, UserRepository userRepository) {
        this.verificationRepository = verificationRepository;
        this.userRepository = userRepository;
    }

    public Optional<UserVerification> generateOTP(String userId, VerificationChannel channel,
            VerificationPurpose purpose) {

        Optional<UserVerification> verification = verificationRepository
                .findByUserIdAndVerificationChannelAndVerificationPurpose(UUID.fromString(userId), channel, purpose);
        if (verification.isPresent()) {
            UserVerification verificationOtp = verification.get();
            if (verificationOtp.getValidTill().after(new Timestamp(System.currentTimeMillis()))) {
                log.info("Existing OTP is still valid. ID: {}", verificationOtp.getId());
            } else {
                verificationOtp.setExpired(true);
                verificationRepository.save(verificationOtp);
                String newOTP = OTPUtil.generateOTP();
                verification = saveOTP(verificationOtp.getUser().getEmail(), newOTP);
                log.info("Existing OTP Expired ID: {} generating new OTP with ID: {}", verificationOtp.getId(),
                        verification.get().getId());
            }
        } else {
            Optional<UserVerification> user = verificationRepository.findByUser(UUID.fromString(userId));
            String newOTP = OTPUtil.generateOTP();
            verification = saveOTP(user.get().getUser().getEmail(), newOTP);
            log.info("No OTP found for ID: " + verification.get().getId() + " generating new OTP");
        }

        return verification;
    }

    public Optional<UserVerification> validateOTP(String verificationId, String otp, VerificationPurpose purpose) {
        Optional<UserVerification> otpVerificationOptional = verificationRepository
                .findByIdAndVerificationCodeAndVerificationPurpose(UUID.fromString(verificationId), otp, purpose);
        if (otpVerificationOptional.isPresent()) {
            UserVerification otpVerification = otpVerificationOptional.get();
            if (otpVerification.getValidTill().after(new Timestamp(System.currentTimeMillis()))) {
                otpVerification.setVerified(true);
                otpVerification.setExpired(true);
                otpVerification = verificationRepository.save(otpVerification);
                return Optional.of(otpVerification);
            }
        }
        return otpVerificationOptional;
    }

    // public Optional<UserVerification> verifyEmailWithOTP(String email, String otp) {
    //     Optional<UserVerification> verificationOptional = verificationRepository.findByEmailAndVerificationCode(email,
    //             otp);
    //     if (verificationOptional.isPresent()) {
    //         UserVerification verification = verificationOptional.get();
    //         if (verification.getValidTill().after(new Timestamp(System.currentTimeMillis()))) {
    //             verification.setVerified(true);
    //             verificationRepository.save(verification);
    //             return Optional.of(verification);
    //         } else {
    //             return Optional.empty();
    //         }
    //     } else {
    //         return Optional.empty();
    //     }
    // }

    public Optional<UserVerification> saveOTP(String email, String otp) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserVerification newVerification = new UserVerification();
            newVerification.setUser(user);
            newVerification.setVerificationCode(otp);
            newVerification.setValidTill(
                    new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(OTP_EXPIRATION_MINUTES)));
            newVerification.setVerificationChannel(VerificationChannel.EMAIL);
            newVerification.setVerificationPurpose(VerificationPurpose.SIGNUP_OTP);
            newVerification = verificationRepository.save(newVerification);
            return Optional.of(newVerification);
        } else {
            return Optional.empty();
        }
    }

    public Optional<UserVerification> findByEmail(String email) {
        return verificationRepository.findByEmail(email);
    }

    public Optional<UserVerification> findByVerificationId(String verificationId) {
        return verificationRepository.findById(UUID.fromString(verificationId));
    }

    public List<UserVerification> findAllByUserId(List<UUID> pendingUserIdList) {
        return verificationRepository.findAllByUserId(pendingUserIdList);
    }

}
