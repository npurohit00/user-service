package community.unboxing.profile.converters;

import community.unboxing.profile.adapters.web.dto.SignUpRequest;
import community.unboxing.profile.core.domain.entity.User;
import community.unboxing.profile.core.enums.UserApprovalStatus;

public class UserMapper {
    public static User dtoToEntity(SignUpRequest userDTO) {
        User userEntity = new User();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setUserType(userDTO.getUserType());
        userEntity.setContactNumber(userDTO.getContactNumber());
        userEntity.setApprovalStatus(UserApprovalStatus.PENDING_FOR_ADMIN_APPROVAL);
        return userEntity;
    }
}

