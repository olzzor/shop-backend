package com.bridgeshop.module.user.service;

import com.bridgeshop.integration.feign.common.SocialAuthResponse;
import com.bridgeshop.integration.feign.common.SocialLoginRequest;
import com.bridgeshop.integration.feign.common.SocialUserResponse;
import com.bridgeshop.module.user.entity.AuthProvider;
import com.bridgeshop.common.exception.ExistsException;
import com.bridgeshop.common.exception.NotFoundException;
import com.bridgeshop.common.exception.UnauthorizedException;
import com.bridgeshop.common.exception.ValidationException;
import com.bridgeshop.module.payload.LoginRequest;
import com.bridgeshop.module.user.dto.*;
import com.bridgeshop.module.user.mapper.UserMapper;
import com.bridgeshop.module.user.entity.User;
import com.bridgeshop.module.user.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;

    private final List<SocialLoginService> loginServices;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final int MAX_NAME_LENGTH = 20;
    private static final String EMAIL_PATTERN = "^\\S+@\\S+\\.\\S+$";
    private static final String PHONE_NUMBER_PATTERN = "^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    public User retrieveById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("userNotFound", "유저 정보를 찾을 수 없습니다."));
    }

    public UserDto convertToDto(User user) {
        return userMapper.mapToDto(user);
    }

    public List<UserDto> convertToDtoList(List<User> userList) {
        return userMapper.mapToDtoList(userList);
    }

    public boolean isAdminRole(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::isAdminFlag).orElse(false);
    }

    public LoginResponse doLogin(LoginRequest request) {

        User user = userRepository.findByEmailAndAuthProvider(request.getEmail(), AuthProvider.LOCAL)
                .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return LoginResponse.builder()
                    .id(user.getId())
                    .role(user.isAdminFlag() ? "admin" : "user") // 추가
                    .build();
        } else {
            throw new UnauthorizedException("passwordMismatch", "비밀번호가 일치하지 않습니다.");
        }
    }

    public LoginResponse doSocialLogin(SocialLoginRequest request) {
        SocialLoginService loginService = this.getLoginService(request.getAuthProvider());

        SocialAuthResponse socialAuthResponse = loginService.getAccessToken(request.getCode());

        SocialUserResponse socialUserResponse = loginService.getUserInfo(socialAuthResponse.getAccess_token());
        log.info("socialUserResponse {} ", socialUserResponse.toString());

        if (userRepository.findBySocialId(socialUserResponse.getId()).isEmpty()) {
            this.joinUser(
                    UserJoinRequest.builder()
                            .name(socialUserResponse.getName())
                            .email(socialUserResponse.getEmail())
                            .authProvider(request.getAuthProvider())
                            .socialId(socialUserResponse.getId())
                            .build()
            );
        }

        User user = userRepository.findBySocialId(socialUserResponse.getId())
                .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));

        return LoginResponse.builder()
                .id(user.getId())
                .role(user.isAdminFlag() ? "admin" : "user") // 추가
                .build();
    }

    private UserJoinResponse joinUser(UserJoinRequest userJoinRequest) {
        User user = userRepository.save(
                User.builder()
                        .name(userJoinRequest.getName())
                        .email(userJoinRequest.getEmail())
                        .password(userJoinRequest.getPassword())
                        .authProvider(userJoinRequest.getAuthProvider())
                        .socialId(userJoinRequest.getSocialId()) // 현재 미사용중이나, 추후 가능성을 위해 작성
                        .adminFlag(false)
                        .activateFlag(true)
                        .build()
        );

        return UserJoinResponse.builder()
                .id(user.getId())
                .role(user.isAdminFlag() ? "admin" : "user") // 추가
                .build();
    }

    private SocialLoginService getLoginService(AuthProvider authProvider) {
        for (SocialLoginService loginService : loginServices) {
            if (authProvider.equals(loginService.getServiceName())) {
                log.info("login service name: {}", loginService.getServiceName());
                return loginService;
            }
        }
        return new LoginServiceImpl();
    }

    public UserResponse getUserResponse(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    /**
     * 아래부터 작성
     **/
    public boolean existUser(Long id) {
        return userRepository.existsById(id);
    }

    public boolean existUserByEmail(String email) {
        return userRepository.existsByEmailAndAuthProvider(email, AuthProvider.LOCAL);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmailAndAuthProvider(email, AuthProvider.LOCAL)
                .orElseThrow(() -> new NotFoundException("userNotFound", "존재하지 않는 계정입니다."));
    }

    public Page<User> retrieveAllPaginated(Pageable pageable) {
        return userRepository.findAllByOrderByIdDesc(pageable);
    }

    public Page<User> searchUserList(UserListSearchRequest userListSearchRequest, Pageable pageable) {
        return userRepository.findByCondition(userListSearchRequest, pageable);
    }

    private boolean isNullOrEmpty(String str) {
        return Optional.ofNullable(str).map(String::isEmpty).orElse(true);
    }

    @Transactional
    public Long insertUser(UserJoinRequest userJoinRequest) {

        if (userRepository.existsByEmailAndAuthProvider(userJoinRequest.getEmail(), AuthProvider.LOCAL)) {
            throw new ExistsException("emailAlreadyExists", "계정이 이미 존재합니다.");

        } else {
            UserJoinRequest updatedRequest = UserJoinRequest.builder()
                    .name(userJoinRequest.getName().trim())
                    .email(userJoinRequest.getEmail().trim())
                    .password(passwordEncoder.encode(userJoinRequest.getPassword().trim()))
                    .authProvider(AuthProvider.LOCAL)
                    .build();

            return joinUser(updatedRequest).getId();
        }
    }

    public Long createAccount(UserJoinRequest userJoinRequest) {

        String name = userJoinRequest.getName();
        String email = userJoinRequest.getEmail();
        String password = userJoinRequest.getPassword();
        String passwordConfirm = userJoinRequest.getPasswordConfirm();

        if (StringUtils.isBlank(name)) {
            throw new ValidationException("nameMissing", "이름을 입력해주세요.");
        } else if (name.trim().length() > MAX_NAME_LENGTH) {
            throw new ValidationException("nameTooLong", "이름은 20자 이하로 입력해주세요.");
        }

        if (StringUtils.isBlank(email)) {
            throw new ValidationException("emailMissing", "이메일를 입력해주세요.");
        } else if (!email.trim().matches(EMAIL_PATTERN)) {
            throw new ValidationException("emailInvalidFormat", "이메일이 유효하지 않습니다.");
        }

        if (StringUtils.isBlank(password)) {
            throw new ValidationException("passwordMissing", "비밀번호를 입력해주세요.");
        } else if (!password.trim().matches(PASSWORD_PATTERN)) {
            throw new ValidationException("passwordInvalidFormat", "비밀번호는 8자 이상이며, 대문자, 소문자, 숫자, 특수 문자를 포함해야 합니다.");
        }

        if (StringUtils.isBlank(passwordConfirm)) {
            throw new ValidationException("passwordConfirmMissing", "비밀번호를 재입력해주세요.");
        } else if (!password.trim().equals(passwordConfirm.trim())) {
            throw new ValidationException("passwordConfirmMismatch", "비밀번호가 일치하지 않습니다.");
        }

        return insertUser(userJoinRequest);
    }

    @Transactional
    public void deleteUser(Long id) {
        Optional<User> opUser = userRepository.findById(id);

        if (opUser.isPresent()) {
            userRepository.delete(opUser.get());
        }
    }


    /**
     * 주어진 유저 정보 목록에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param userDtoList 업데이트할 유저 정보의 DTO 목록
     */
    @Transactional
    public void updateUsers(List<UserDto> userDtoList) {

        for (UserDto userDto : userDtoList) {
            User user = userRepository.findById(userDto.getId())
                    .orElseThrow(() -> new NotFoundException("userNotFound", "유저 정보를 찾을 수 없습니다."));

            // 변경 사항을 감지하여 엔티티를 저장
            if (updateUserDetails(user, userDto)) {
                userRepository.save(user);
            }
        }
    }

    /**
     * 개별 유저 정보 엔티티에 대한 상세 업데이트를 수행
     * 변경된 필드가 있을 경우에만 업데이트를 진행
     *
     * @param user    업데이트할 유저 정보 엔티티
     * @param userDto 업데이트에 사용될 DTO
     * @return 변경 사항이 있었으면 true를, 아니면 false를 반환
     */
    private boolean updateUserDetails(User user, UserDto userDto) {
        boolean isModified = false;

        // 이름의 변경 사항을 검사하고 업데이트
        isModified |= updateIfDifferent(user.getName(), userDto.getName(), user::setName);

        // 관리자 권한 변경이 있을 경우 업데이트
        if (user.isAdminFlag() != userDto.isAdminFlag()) {
            user.setAdminFlag(userDto.isAdminFlag());
            isModified = true; // 상태가 변경되었다면 수정됨으로 표시
        }

        // 활성 상태 변경이 있을 경우 업데이트
        if (user.isActivateFlag() != userDto.isActivateFlag()) {
            user.setActivateFlag(userDto.isActivateFlag());
            isModified = true; // 상태가 변경되었다면 수정됨으로 표시
        }

        // 변경된 사항이 있으면 true, 아니면 false를 반환
        return isModified;
    }

    /**
     * 현재 값과 새로운 값을 비교하여 다를 경우, 제공된 setter 함수를 사용해 값을 업데이트
     *
     * @param currentValue 현재 객체의 필드 값
     * @param newValue     업데이트 할 새로운 값
     * @param setter       현재 값을 업데이트할 setter 메소드 참조
     * @return 값이 변경되었으면 true를, 그렇지 않으면 false를 반환
     */
    private boolean updateIfDifferent(String currentValue, String newValue, Consumer<String> setter) {
        if (StringUtils.isNotBlank(newValue) && !newValue.trim().equals(currentValue)) {
            // 새로운 값이 다르다면 setter 메소드를 사용하여 현재 객체의 값을 업데이트
            setter.accept(newValue.trim());
            return true; // 변경이 있었으므로 true를 반환
        }
        return false; // 값이 변경되지 않았으므로 false를 반환
    }

    @Transactional
    public void updateProfile(Long userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));

        String name = userUpdateRequest.getName();
        String phoneNumber = userUpdateRequest.getPhoneNumber();

        if (StringUtils.isBlank(name)) {
            throw new ValidationException("nameMissing", "이름을 입력해주세요.");
        } else if (name.trim().length() > MAX_NAME_LENGTH) {
            throw new ValidationException("nameTooLong", "이름은 20자 이하로 입력해주세요.");
        }

        if (StringUtils.isNotBlank(phoneNumber)) {
            if (!phoneNumber.trim().matches(PHONE_NUMBER_PATTERN)) {
                throw new ValidationException("phoneNumberInvalidFormat", "유효한 전화번호를 입력해주세요.");
            }
            user.setPhoneNumber(phoneNumber.trim());
        } else {
            user.setPhoneNumber(null);
        }

        user.setName(name.trim());
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(Long userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));

        String currentPassword = userUpdateRequest.getCurrentPassword();
        String newPassword = userUpdateRequest.getNewPassword();
        String newPasswordConfirm = userUpdateRequest.getNewPasswordConfirm();

        if (StringUtils.isBlank(currentPassword)) {
            throw new ValidationException("currentPasswordMissing", "현재 비밀번호를 입력해주세요.");
        } else if (!passwordEncoder.matches(currentPassword.trim(), user.getPassword())) {
            throw new ValidationException("currentPasswordMismatch", "현재 비밀번호가 일치하지 않습니다.");
        }

        if (StringUtils.isBlank(newPassword)) {
            throw new ValidationException("newPasswordMissing", "새 비밀번호를 입력해주세요.");
        } else if (!newPassword.trim().matches(PASSWORD_PATTERN)) {
            throw new ValidationException("newPasswordInvalidFormat", "새 비밀번호는 8자 이상이며, 대문자, 소문자, 숫자, 특수 문자를 포함해야 합니다.");
        }

        if (StringUtils.isBlank(newPasswordConfirm)) {
            throw new ValidationException("newPasswordConfirmMissing", "새 비밀번호를 재입력해주세요.");
        } else if (!newPassword.trim().equals(newPasswordConfirm.trim())) {
            throw new ValidationException("newPasswordConfirmMismatch", "새 비밀번호가 일치하지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(newPassword.trim()));
        userRepository.save(user);
    }
}
