package com.bridgeshop.module.address.service;

import com.bridgeshop.common.exception.NotFoundException;
import com.bridgeshop.common.exception.UnauthorizedException;
import com.bridgeshop.common.exception.ValidationException;
import com.bridgeshop.module.address.dto.AddressDto;
import com.bridgeshop.module.address.dto.AddressUpdateRequest;
import com.bridgeshop.module.address.entity.Address;
import com.bridgeshop.module.address.mapper.AddressMapper;
import com.bridgeshop.module.address.repository.AddressRepository;
import com.bridgeshop.module.user.entity.User;
import com.bridgeshop.module.user.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    private static final int MAX_NAME_LENGTH = 20;
    private static final int MAX_ADDRESS2_LENGTH = 100;
    private static final String PHONE_NUMBER_PATTERN = "^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$";

    public AddressDto convertToDto(Address address) {
        return addressMapper.mapToDto(address);
    }

    public List<AddressDto> convertToDtoList(List<Address> addressList) {
        return addressMapper.mapToDtoList(addressList);
    }

    public Address retrieveById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("addressNotFound", "주소 정보를 찾을 수 없습니다."));
    }

    public List<Address> retrieveAllByUserId(Long userId) {
        return addressRepository.findAllByUser_IdOrderByIsDefaultDescIdDesc(userId);
    }

    public Optional<Address> getDefaultAddressByUserId(Long userId) {
        return addressRepository.findByUser_IdAndIsDefaultTrue(userId);
    }

    public void checkInput(AddressUpdateRequest auReq) {

        // name 체크
        if (StringUtils.isBlank(auReq.getName())) {
            throw new ValidationException("nameMissing", "이름을 입력해주세요.");
        } else if (auReq.getName().trim().length() > MAX_NAME_LENGTH) {
            throw new ValidationException("nameTooLong", "이름은 20자 이하로 입력해주세요.");
        }

        // phoneNumber 체크
        if (StringUtils.isBlank(auReq.getPhoneNumber())) {
            throw new ValidationException("phoneNumberMissing", "전화번호를 입력해주세요.");
        } else if (!auReq.getPhoneNumber().trim().matches(PHONE_NUMBER_PATTERN)) {
            throw new ValidationException("phoneNumberInvalidFormat", "전화번호가 유효하지 않습니다.");
        }

        // address2 체크 (공동주택의 경우)
        if (auReq.isApartment()) {
            if (!StringUtils.isNotBlank(auReq.getAddress2())) {
                throw new ValidationException("address2Missing", "공동주택의 경우, 아파트, 동/호수 정보를 입력해주세요.");
            } else if (auReq.getAddress2().trim().length() > MAX_ADDRESS2_LENGTH) {
                throw new ValidationException("address2TooLong", "주소는 100자 이하로 입력해주세요.");
            }
        }
    }

    @Transactional
    public void insertAddress(Long userId, AddressUpdateRequest auReq) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));

        Address address = Address.builder()
                .user(user)
                .name(auReq.getName().trim())
                .phoneNumber(auReq.getPhoneNumber().trim())
                .zipCode(auReq.getZipCode().trim())
                .province(auReq.getProvince().trim())
                .city(auReq.getCity().trim())
                .address1(auReq.getAddress1().trim())
                .address2(auReq.getAddress2().trim())
                .isApartment(auReq.isApartment())
                .isDefault(auReq.isDefault())
                .build();

        addressRepository.save(address);
    }

    @Transactional
    public void updateAddress(Long userId, AddressUpdateRequest auReq) {
        Address address = addressRepository.findById(auReq.getId())
                .orElseThrow(() -> new NotFoundException("addressNotFound", "주소 정보를 찾을 수 없습니다."));

        // 해당 Address의 User ID와 현재 요청한 유저의 ID가 일치하는지 확인 (무결성 체크)
        if (!address.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("addressUnauthorizedAccess", "이 주소에 대한 접근 권한이 없습니다.");
        }

        address.setName(auReq.getName().trim());
        address.setPhoneNumber(auReq.getPhoneNumber().trim());
        address.setZipCode(auReq.getZipCode().trim());
        address.setProvince(auReq.getProvince().trim());
        address.setCity(auReq.getCity().trim());
        address.setAddress1(auReq.getAddress1().trim());
        address.setAddress2(auReq.getAddress2().trim());
        address.setIsApartment(auReq.isApartment());
        address.setIsDefault(auReq.isDefault());

        addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("addressNotFound", "주소 정보를 찾을 수 없습니다."));

        // 해당 Address의 User ID와 현재 요청한 유저의 ID가 일치하는지 확인 (무결성 체크)
        if (!address.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("addressUnauthorizedAccess", "이 주소에 대한 접근 권한이 없습니다.");
        }

        addressRepository.delete(address);
    }

    @Transactional
    public void updateDefaultAddress(Long userId) {
        Optional<Address> addressOptional = addressRepository.findByUser_IdAndIsDefaultTrue(userId);

        // 기본 주소로 등록된 주소가 존재하는 경우
        if (addressOptional.isPresent()) {
            // 해당 주소를 기본 주소에서 해제
            Address defaultAddress = addressOptional.get();
            defaultAddress.setIsDefault(false);
        }
    }
}
