package community.unboxing.profile.core.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import community.unboxing.profile.adapters.web.dto.LoginRequest;
import community.unboxing.profile.adapters.web.dto.OTPRequest;
import community.unboxing.profile.adapters.web.dto.PasswordRequest;
import community.unboxing.profile.adapters.web.dto.SignUpRequest;
import community.unboxing.profile.adapters.web.dto.UserProfileDetails;
import community.unboxing.profile.adapters.web.dto.UserProfileDetails.Approvals;
import community.unboxing.profile.adapters.web.dto.UserProfileDetails.UserDetails;
import community.unboxing.profile.adapters.web.dto.UserProfileDetails.Verifications;
import community.unboxing.profile.converters.UserMapper;
import community.unboxing.profile.core.domain.entity.User;
import community.unboxing.profile.core.domain.entity.UserApprovals;
import community.unboxing.profile.core.domain.entity.UserEducation;
import community.unboxing.profile.core.domain.entity.UserEngineeringSkills;
import community.unboxing.profile.core.domain.entity.UserGrades;
import community.unboxing.profile.core.domain.entity.UserPreference;
import community.unboxing.profile.core.domain.entity.UserProfile;
import community.unboxing.profile.core.domain.entity.UserSessions;
import community.unboxing.profile.core.domain.entity.UserVerification;
import community.unboxing.profile.core.domain.entity.UserWorkExperience;
import community.unboxing.profile.core.domain.repository.UserApprovalsRepository;
import community.unboxing.profile.core.domain.repository.UserEducationRepository;
import community.unboxing.profile.core.domain.repository.UserEngineeringSkillsRepository;
import community.unboxing.profile.core.domain.repository.UserGradesRepository;
import community.unboxing.profile.core.domain.repository.UserPreferencesRepository;
import community.unboxing.profile.core.domain.repository.UserProfileRepository;
import community.unboxing.profile.core.domain.repository.UserRepository;
import community.unboxing.profile.core.domain.repository.UserSessionsRepository;
import community.unboxing.profile.core.domain.repository.UserWorkExperienceRepository;
import community.unboxing.profile.core.enums.ApprovalPurpose;
import community.unboxing.profile.core.enums.UserApprovalStatus;
import community.unboxing.profile.core.enums.VerificationChannel;
import community.unboxing.profile.core.enums.VerificationPurpose;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.lang.Objects;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommunityUserService implements UserService {

    private final UserRepository userRepository;
    private final UserVerificationService verificationService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService mailService;
    private final AuthenticationManager authenticationManager;

    private final UserProfileRepository profileRepository;
    private final UserEducationRepository educationRepository;
    private final UserEngineeringSkillsRepository engineeringSkillsRepository;
    private final UserGradesRepository gradesRepository;
    private final UserPreferencesRepository preferencesRepository;
    private final UserWorkExperienceRepository workExperienceRepository;
    private final UserApprovalsRepository approvalsRepository;
    private final UserSessionsRepository sessionsRepository;

    CommunityUserService(UserRepository userRepository,
            EmailService mailService,
            UserVerificationService verificationService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            UserProfileRepository profileRepository,
            UserEducationRepository educationRepository,
            UserEngineeringSkillsRepository engineeringSkillsRepository,
            UserGradesRepository gradesRepository,
            UserPreferencesRepository preferencesRepository,
            UserWorkExperienceRepository workExperienceRepository,
            UserApprovalsRepository approvalsRepository,
            UserSessionsRepository sessionsRepository) {

        this.verificationService = verificationService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.mailService = mailService;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.educationRepository = educationRepository;
        this.engineeringSkillsRepository = engineeringSkillsRepository;
        this.gradesRepository = gradesRepository;
        this.preferencesRepository = preferencesRepository;
        this.workExperienceRepository = workExperienceRepository;
        this.approvalsRepository = approvalsRepository;
        this.sessionsRepository = sessionsRepository;

    }

    @Override
    public Optional<Map<String, Object>> checkUserExists(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email)
                .map(user -> Map.of("isPresent", true, "userDetails",
                        UserProfileDetails.builder().userId(user.getId().toString()).build()))
                .orElseGet(() -> Map.of("isPresent", false)));
    }

    @Override
    public Optional<Map<String, Object>> createUser(SignUpRequest request) {

        // validator.validate(request);

        Optional<Map<String, Object>> isRegisteredUser = checkUserExists(request.getEmail());
        if (isRegisteredUser.isPresent()) {
            Map<String, Object> registeredUserMap = isRegisteredUser.get();
            if (registeredUserMap.containsKey("isPresent") &&
                    Boolean.TRUE.equals(registeredUserMap.get("isPresent"))) {
                return isRegisteredUser;
            }
        }

        User savedUser = userRepository.save(UserMapper.dtoToEntity(request));

        UserProfile profile = UserProfile.builder()
                .dateOfBirth(new Date(request.getDateOfBirth().getTime()))
                .district(request.getDistrict())
                .state(request.getState())
                .country(request.getCountry())
                .user(savedUser)
                .build();
        profileRepository.save(profile);

        UserApprovals approval = UserApprovals.builder()
                .approvalStatus(UserApprovalStatus.PENDING_FOR_ADMIN_APPROVAL)
                .approvalPurpose(ApprovalPurpose.COMMUNITY_USER_REGISTRATION)
                .user(savedUser)
                .build();
        approvalsRepository.save(approval);

        UserProfileDetails userDetails = UserProfileDetails.builder()
                .userId(savedUser.getId().toString())
                .email(savedUser.getEmail())
                .isPasswordSet(!Objects.isEmpty(savedUser.getPassword()))
                .approvals(List.of(Approvals.builder()
                        .approvalId(approval.getId().toString())
                        .approvalPurpose(approval.getApprovalPurpose())
                        .approvalStatus(approval.getApprovalStatus())
                        .build()))
                .build();

        return verificationService.generateOTP(savedUser.getId().toString(), VerificationChannel.EMAIL, VerificationPurpose.SIGNUP_OTP)
                .flatMap(otp -> {
                    userDetails.setVerifications(List.of(Verifications.builder()
                            .isVerified(otp.isVerified())
                            .validTill(new Date(otp.getValidTill().getTime()))
                            .verificationChannel(otp.getVerificationChannel())
                            .verificationPurpose(otp.getVerificationPurpose())
                            .verificationId(otp.getId().toString())
                            .build()));
                    try {
                        mailService.sendEmailSignUpVerification(request.getEmail(), otp.getVerificationCode());
                        return Optional.of(Map.of(
                                "message",
                                "User creation successful, Verification OTP was sent to the registered email.",
                                "userDetails", userDetails,
                                "isOtpSent", true));
                    } catch (MailSendException | MailAuthenticationException mse) {
                        log.error("Error while sending signup email otp: ", mse);
                        return Optional.of(Map.of(
                                "message",
                                "User creation successful, Verification OTP not sent. Try resend OTP.",
                                "userDetails", userDetails,
                                "isOtpSent", false));
                    }
                });
    }

    @Override
    public Optional<Map<String, Object>> sendOTP(OTPRequest request) {

        return verificationService.generateOTP(request.getUserId(), request.getChannel(), request.getPurpose())
                .flatMap(otp -> {
                    try {
                        mailService.sendEmailSignUpVerification(otp.getUser().getEmail(), otp.getVerificationCode());
                        return Optional.of(Map.of(
                                "message",
                                "Verification OTP was sent to the registered email.",
                                "verificationId", otp.getId().toString()));
                    } catch (MailSendException | MailAuthenticationException mse) {
                        log.error("Error while sending signup email otp: ", mse);
                        return Optional.of(Map.of(
                                "message",
                                "Verification OTP not sent. Try resend OTP.",
                                "verificationId", otp.getId().toString()));
                    }
                });

    }

    @Override
    public Optional<Map<String, Object>> verifyOTP(OTPRequest request) {

        Optional<UserVerification> otpVerifyOptional = verificationService.validateOTP(request.getVerificationId(), request.getOtp(), request.getPurpose());
        log.info("Verification Service result: {}", otpVerifyOptional);
        if (otpVerifyOptional.isPresent()) {

            UserVerification otpVerify = otpVerifyOptional.get();
            Verifications otpVerification = Verifications.builder()
            .isVerified(otpVerify.isVerified())
            .verificationId(otpVerify.getId().toString())
            .verificationChannel(otpVerify.getVerificationChannel())
            .verificationPurpose(otpVerify.getVerificationPurpose())
            .build();

            // if (otpVerify.isVerified()) {
            //     return Optional.of(Map.of(
            //             "message",
            //             "OTP Verification Already Completed",
            //             "userId", otpVerify.getUser().getId().toString(),
            //             "verifications", otpVerification));
            // }

            try {
                mailService.sendEmailVerificationConfirmation(otpVerify.getUser().getEmail(),
                        otpVerify.isVerified() ? "OTP Verified Successfully" : "OTP Verification Failed");
                return Optional.of(Map.of(
                        "message",
                        "OTP Verified Successfully",
                        "userId", otpVerify.getUser().getId().toString(),
                        "verifications", otpVerification));
            } catch (MailSendException | MailAuthenticationException mse) {
                log.error("Error while sending signup email otp: ", mse);
                return Optional.of(Map.of(
                        "message",
                        "OTP Verified Successfully. Email Notification send failure.",
                        "userId", otpVerify.getUser().getId().toString(),
                        "verifications", otpVerification));
            }
        } else {
            return Optional.of(Map.of("message","OTP Verification Failure."));
        }

    }

    @Override
    public Optional<Map<String, Object>> createPassword(PasswordRequest request) {

        if (!request.getPassword().equals(request.getConfirmationPassword())) {
            log.info("Password and confirm password mismatch");
            return Optional.of(Map.of(
                    "message",
                    "Password and confirm password mismatch",
                    "isPasswordSet", false));
        }

        Optional<User> existingUserOptional = userRepository.findById(UUID.fromString(request.getUserId()));

        if (!existingUserOptional.isPresent()) {
            log.info("User not found");
            return Optional.of(Map.of(
                    "message",
                    "User not found for userId " + request.getUserId(),
                    "isPasswordSet", false));
        } else {
            User user = existingUserOptional.get();
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            user.setPassword(hashedPassword);
            userRepository.save(user);
            return Optional.of(Map.of(
                    "message",
                    "Password Set Successful",
                    "isPasswordSet", true));
        }

    }

    @Override
    public Optional<Map<String, Object>> resetPassword(PasswordRequest request) {

        // validator.validate(request);

        if (!request.getPassword().equals(request.getConfirmationPassword())) {
            return Optional.of(Map.of(
                    "message",
                    "Password and confirm password mismatch",
                    "isPasswordSet", false));
        }

        Optional<User> userOptional = userRepository.findById(UUID.fromString(request.getUserId()));

        if (!userOptional.isPresent()) {
            return Optional.of(Map.of(
                    "message",
                    "User not found",
                    "isPasswordSet", false));
        }

        User user = userOptional.get();

        if (request.isOtpVerified() && request.getVerificationId() != null) {

            Optional<UserVerification> verification = verificationService
                    .findByVerificationId(request.getVerificationId());

            if (verification.isPresent()
                    && verification.get().getVerificationPurpose().equals(VerificationPurpose.PASSWORD_RESET_OTP)
                    && verification.get().isVerified()) {
                String hashedPassword = passwordEncoder.encode(request.getNewPassword());
                user.setPassword(hashedPassword);
                userRepository.save(user);
            } else {
                log.info("Password reset OTP not present / not verified / expired");
                return Optional.of(Map.of(
                        "message",
                        "Password reset OTP not present / not verified / expired without verification completion",
                        "isPasswordSet", false));
            }

        } else {
            log.info("Password reset OTP not present / not verified / expired");
            return Optional.of(Map.of(
                    "message",
                    "Mandatory data missing in request [isOtpVerified, verificationId]",
                    "isPasswordSet", false));
        }

        return Optional.of(Map.of(
                "message",
                "Password reset Successful",
                "isPasswordSet", true));
    }

    public Optional<Map<String, Object>> updateUserProfile(UserProfileDetails request) {

        Optional<User> existingUser = userRepository.findById(UUID.fromString(request.getUserId()));

        if (!existingUser.isPresent()) {
            return Optional.of(Map.of(
                    "message",
                    "User not found",
                    "isProfileUpdated", false));
        }

        UserProfile profile = UserProfile.builder().build();
        profile.setFirstname(request.getProfile().getFirstname());
        profile.setLastname(request.getProfile().getLastname());
        profile.setDateOfBirth(new Date(request.getProfile().getDateOfBirth().getTime()));
        profile.setGender(request.getProfile().getGender());
        profile.setDistrict(request.getProfile().getDistrict());
        profile.setState(request.getProfile().getState());
        profile.setPincode(request.getProfile().getPincode());
        profile.setProfileWebsiteUrl(request.getProfile().getProfileWebsiteUrl());
        profile.setLinkedInUrl(request.getProfile().getLinkedInUrl());
        profile = profileRepository.save(profile);

        updateEducation(request.getUserEducation(), profile);
        updateGrades(request.getUserGrades(), profile);
        updatePreference(request.getUserPreference(), profile);
        updateWorkExperience(request.getUserWorkExperience(), profile);

        return Optional.of(Map.of(
                "message",
                "Profile Update Successful",
                "isProfileUpdated", true));
    }

    private void updateEducation(List<UserProfileDetails.Education> educationList,
            UserProfile profile) {
        List<UserEducation> educationListModel = educationList.stream()
                .map(education -> UserEducation.builder()
                        .institutionName(education.getInstitutionName())
                        .educationBackground(education.getEducationBackground())
                        .fieldOfStudy(education.getFieldOfStudy())
                        .endYear(education.getEndYear())
                        .status(education.getStatus())
                        .achievements(education.getAchievements())
                        .profile(profile)
                        .build())
                .collect(Collectors.toList());
        educationRepository.saveAll(educationListModel);
    }

    private void updateGrades(List<UserProfileDetails.Grades> grades, UserProfile profile) {

        if (grades == null || grades.isEmpty()) {
            return;
        }

        for (UserProfileDetails.Grades grade : grades) {
            Optional<UserGrades> existingGrades = gradesRepository.findByUserProfileAndCatalogIdAndProductId(profile,
                    grade.getCatalogId(), grade.getProductId());

            UserGrades userGrades;

            if (existingGrades.isPresent()) {
                userGrades = existingGrades.get();
                userGrades.setCatalogId(grade.getCatalogId());
                userGrades.setCatalogName(grade.getCatalogName());
                userGrades.setCatalogGrade(grade.getCatalogGrade());
                userGrades.setCatalogGradeStar(grade.getCatalogGradeStar());
                userGrades.setCatalogGradePercentage(grade.getCatalogGradePercentage());
                userGrades.setProductId(grade.getProductId());
                userGrades.setProductName(grade.getProductName());
                userGrades.setProductGrade(grade.getProductGrade());
            } else {
                userGrades = UserGrades.builder()
                        .catalogId(grade.getCatalogId())
                        .catalogName(grade.getCatalogName())
                        .catalogGrade(grade.getCatalogGrade())
                        .catalogGradeStar(grade.getCatalogGradeStar())
                        .catalogGradePercentage(grade.getCatalogGradePercentage())
                        .productId(grade.getProductId())
                        .productName(grade.getProductName())
                        .productGrade(grade.getProductGrade())
                        .build();
            }

            gradesRepository.save(userGrades);
        }

    }

    private void updatePreference(UserProfileDetails.Preferences preference, UserProfile profile) {

        if (preference == null) {
            return;
        }

        Optional<UserPreference> existingPreference = preferencesRepository.findByProfile(profile);

        UserPreference userPreference;
        if (existingPreference.isPresent()) {
            userPreference = existingPreference.get();
        } else {
            userPreference = new UserPreference();
        }

        userPreference.setEmail(preference.isEmail());
        userPreference.setSms(preference.isSms());
        userPreference.setNewsletterSubscription(preference.isNewsletterSubscription());
        userPreference.setProfileCompletionRemainders(preference.isProfileCompletionRemainders());
        userPreference.setAssignmentNotifications(preference.isAssignmentNotifications());
        userPreference.setSystemUpdates(preference.isSystemUpdates());

        userPreference.setProfile(profile);

        preferencesRepository.save(userPreference);
    }

    private void updateWorkExperience(UserProfileDetails.WorkExperience workExperience, UserProfile profile) {

        if (workExperience == null) {
            return;
        }

        Optional<UserWorkExperience> existingWorkExperience = workExperienceRepository.findByProfile(profile);

        UserWorkExperience userWorkExperience;
        if (existingWorkExperience.isPresent()) {
            userWorkExperience = existingWorkExperience.get();
        } else {
            userWorkExperience = new UserWorkExperience();
        }

        userWorkExperience.setCompanyName(workExperience.getCompanyName());
        userWorkExperience.setTotalYearsOfExperience(workExperience.getTotalYearsOfExperience());
        userWorkExperience.setLatestRoleDesignation(workExperience.getLatestRoleDesignation());
        userWorkExperience.setSkillsStatus(workExperience.getSkillsStatus());
        userWorkExperience.setCatalogVariants(workExperience.getCatalogVariants());

        UserWorkExperience experienceModel = workExperienceRepository.save(userWorkExperience);

        List<UserProfileDetails.EngineeringSkills> skills = workExperience.getUserEngineeringSkills();

        if (skills == null || skills.isEmpty()) {
            return;
        }

        List<UserEngineeringSkills> updatedSkills = new ArrayList<>();

        for (UserProfileDetails.EngineeringSkills requestSkill : skills) {

            if (requestSkill == null) {
                continue;
            }

            String skillName = requestSkill.getSkillName();

            if (skillName == null || skillName.isBlank()) {
                continue;
            }

            Optional<UserEngineeringSkills> existingSkills = engineeringSkillsRepository
                    .findByWorkExperienceAndSkillName(experienceModel, skillName);

            UserEngineeringSkills engineeringSkills;
            if (existingSkills.isPresent()) {
                engineeringSkills = existingSkills.get();
            } else {
                engineeringSkills = UserEngineeringSkills.builder().build();
                engineeringSkills.setWorkExperience(userWorkExperience);
            }

            engineeringSkills.setSkillName(requestSkill.getSkillName());
            engineeringSkills.setExperience(requestSkill.getExperience());
            engineeringSkills.setSelfRating(requestSkill.getSelfRating());
            engineeringSkills.setDescription(requestSkill.getDescription());

            updatedSkills.add(engineeringSkills);

        }

        engineeringSkillsRepository.saveAll(updatedSkills);

    }

    @Override
    public Optional<Map<String, Object>> login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = generateJwtToken(authentication);

        Optional<User> user = userRepository.findByEmail(request.getUsername());

        UserSessions sessions = UserSessions.builder()
                .deviceInfo(request.getDeviceInfo())
                .browserInfo(request.getBrowserInfo())
                .ipAddress(request.getIpAddress())
                .isActive(true)
                .loginTime(Timestamp.valueOf(LocalDateTime.now()))
                .user(user.get())
                .build();

        sessionsRepository.save(sessions);

        return Optional.of(Map.of("token", jwtToken, "userId", user.get().getId(), "sessionId", sessions.getId()));
    }

    private String generateJwtToken(Authentication authentication) {
        org.springframework.security.core.userdetails.UserDetails userDetails = (org.springframework.security.core.userdetails.UserDetails) authentication
                .getPrincipal();

        String secretKey = "n6rxkZOj0DPydXck0qqaVBBMqYqnldlCfDo0Ab9LPfcDDomdnFHYesqmBwcYFR8l1UiF8OQIKNn/y5VYbUJnLw==";

        long expirationTime = 900000; // 15 minutes

        Map<String, Object> claims = new HashMap<>();
        claims.put(Claims.SUBJECT, userDetails.getUsername());
        claims.put("authorities", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .claims().empty().add(claims).and()
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    @Override
    public Optional<Map<String, Object>> logout(LoginRequest request) {
        Optional<UserSessions> sessions = sessionsRepository.findByIdAndIsActive(UUID.fromString(request.getSessionId()), true);
        if (sessions.isPresent()) {
            sessions.get().setLogoutTime(Timestamp.valueOf(LocalDateTime.now()));
            sessions.get().setActive(false);
            sessionsRepository.save(sessions.get());
            return Optional.of(Map.of("isLoggedOut", true, "userId", sessions.get().getUser().getId()));
        }
        return Optional.of(Map.of("isLoggeOut", false));
    }

    @Override
    public Optional<Map<String, Object>> deleteUser(String userId) {
        return Optional.of(Map.of("userId", userId));
    }

    @Override
    public Optional<Map<String, Object>> findUserForApproval(String userId) {
        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        Optional<UserApprovals> approvals = approvalsRepository.findByUser(user.get());
        return Optional.of(Map.of("user", user.get(), "approvals", approvals.get()));
    }

    @Override
    public Optional<Map<String, Object>> updateUserApproval(UserApprovals userApprovals) {
        userApprovals.getUser().setApprovalStatus(userApprovals.getApprovalStatus());
        userApprovals = approvalsRepository.save(userApprovals);
        return Optional.of(Map.of("userApprovals", userApprovals));
    }

    @Override
    public Optional<Map<String, Object>> getAllUsersForSignUpApproval() {

        Optional<List<UserApprovals>> pendingUserApprovals = approvalsRepository
                .findByApprovalStatus(UserApprovalStatus.PENDING_FOR_ADMIN_APPROVAL);

        if (!pendingUserApprovals.isPresent()) {
            Optional.of(Collections.emptyList());
        }

        List<UUID> pendingUserIdList = new ArrayList<>();

        for (UserApprovals approvals : pendingUserApprovals.get()) {
            pendingUserIdList.add(approvals.getUser().getId());
        }

        List<User> users = userRepository.findAllById(pendingUserIdList);
        List<UserVerification> verifications = verificationService.findAllByUserId(pendingUserIdList);

        List<UserProfileDetails> userProfileList = new ArrayList<>();

        for (User user : users) {

            UserProfileDetails userDetails = UserProfileDetails.builder().build();

            for (UserApprovals approvals : pendingUserApprovals.get()) {

                if (approvals.getApprovalPurpose().equals(ApprovalPurpose.COMMUNITY_USER_REGISTRATION)) {
                    Approvals signuApprovals = Approvals.builder()
                            .approvalStatus(approvals.getApprovalStatus())
                            .approvalPurpose(approvals.getApprovalPurpose())
                            .build();

                    userDetails.setApprovals(List.of(signuApprovals));
                }

                if (user.getId().equals(approvals.getUser().getId())) {
                    log.info("Adding User data for admin approval");
                    userDetails.setUserId(user.getId().toString());
                    UserDetails details = UserDetails.builder()
                            .approvalStatus(user.getApprovalStatus())
                            .email(user.getEmail())
                            .contactNumber(user.getContactNumber())
                            .userType(user.getUserType())
                            .username(user.getUsername())
                            .build();
                    userDetails.setUserDetails(details);
                }

                for (UserVerification verification : verifications) {
                    log.info("Adding Verification data for admin approval");
                    if (verification.getUser().getId().equals(user.getId())
                            && verification.getVerificationPurpose().equals(VerificationPurpose.SIGNUP_OTP)) {
                        Verifications userVerification = Verifications.builder()
                                .isVerified(verification.isVerified())
                                .validTill(new Date(verification.getValidTill().getTime()))
                                .verificationChannel(verification.getVerificationChannel())
                                .verificationPurpose(verification.getVerificationPurpose())
                                .build();
                        userDetails.setVerifications(List.of(userVerification));
                    }
                }

            }

            userProfileList.add(userDetails);

        }

        return Optional.of(Map.of("userProfile", userProfileList));
    }

}
