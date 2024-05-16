package community.unboxing.profile.adapters.web.dto;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import community.unboxing.profile.core.enums.ApprovalPurpose;
import community.unboxing.profile.core.enums.CommunityUserType;
import community.unboxing.profile.core.enums.UserApprovalStatus;
import community.unboxing.profile.core.enums.VerificationChannel;
import community.unboxing.profile.core.enums.VerificationPurpose;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class UserProfileDetails {

    private String userId;
    private String email;
    private boolean isPasswordSet;

    private UserDetails userDetails;
    private Profile profile;
    private List<Verifications> verifications;
    private List<Approvals> approvals;
    private List<Education> userEducation;
    private List<Grades> userGrades;
    private Preferences userPreference;
    private WorkExperience userWorkExperience;
    private Sessions sessions;

    @Data
    @Builder
    public static class UserDetails {
        private String username;
        private String email;
        private String contactNumber;
        private CommunityUserType userType;
        private UserApprovalStatus approvalStatus;
    }

    @Data
    @Builder
    public static class Verifications {
        private boolean isVerified;
        private VerificationChannel verificationChannel;
        private VerificationPurpose verificationPurpose;
        private String verificationId;

        @JsonFormat(pattern = "dd-mm-yyyy HH:mm:ss", timezone = "UTC")
        private Date validTill;
    }

    @Data
    @Builder
    public static class Approvals {
        private String approvalId;
        private UserApprovalStatus approvalStatus;
        private ApprovalPurpose approvalPurpose;
        private String approvedBy;
        private String approverRoleId;
        private Date approvalDate;
    }

    @Data
    @Builder
    public static class Sessions {
        private Date loginTime;
        private Date logoutTime;
        private String ipAddress;
        private String deviceInfo;
        private boolean isActive;
    }

    @Data
    @Builder
    public static class Education {
        private String institutionName;
        private String educationBackground;
        private String fieldOfStudy;
        private Integer endYear;
        private String status;
        private String achievements;
    }

    @Data
    @Builder
    public static class Profile {
        private String firstname;
        private String lastname;
        private Date dateOfBirth;
        private String gender;
        private String district;
        private String state;
        private String pincode;
        private String profileWebsiteUrl;
        private String linkedInUrl;
    }

    @Data
    @Builder
    public static class EngineeringSkills {
        private String skillName;
        private Integer experience;
        private Integer selfRating;
        private String description;
    }

    @Data
    @Builder
    public static class Grades {
        private Long catalogId;
        private String catalogName;
        private String catalogGrade;
        private Integer catalogGradeStar;
        private Double catalogGradePercentage;
        private Long productId;
        private String productName;
        private Double productGrade;
    }

    @Data
    @Builder
    public static class Preferences {
        private boolean email;
        private boolean sms;
        private boolean newsletterSubscription;
        private boolean profileCompletionRemainders;
        private boolean assignmentNotifications;
        private boolean systemUpdates;
    }

    @Data
    @Builder
    public static class WorkExperience {
        private String companyName;
        private Integer totalYearsOfExperience;
        private String latestRoleDesignation;
        private String skillsStatus;
        private String catalogVariants;
        private List<EngineeringSkills> userEngineeringSkills;
    }
}
