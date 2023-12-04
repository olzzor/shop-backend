package com.bridgeshop.module.contact.service;

import com.bridgeshop.module.contact.dto.ContactDto;
import com.bridgeshop.module.contact.dto.ContactListResponse;
import com.bridgeshop.module.contact.dto.ContactListSearchRequest;
import com.bridgeshop.module.contact.entity.Contact;
import com.bridgeshop.module.contact.entity.ContactStatus;
import com.bridgeshop.module.contact.mapper.ContactMapper;
import com.bridgeshop.module.contact.repository.ContactRepository;
import com.bridgeshop.module.user.entity.User;
import com.bridgeshop.common.exception.NotFoundException;
import com.bridgeshop.common.exception.UnauthorizedException;
import com.bridgeshop.common.exception.ValidationException;
import com.bridgeshop.module.user.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ContactMapper contactMapper;

    private static final int MAX_NAME_LENGTH = 20;
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_CONTENT_LENGTH = 5000;
    private static final String EMAIL_PATTERN = "^\\S+@\\S+\\.\\S+$";

    public ContactDto convertToDto(Contact contact) {
        return contactMapper.mapToDto(contact);
    }

    public List<ContactDto> convertToDtoList(List<Contact> contactList) {
        return contactMapper.mapToDtoList(contactList);
    }

//    /** 문의 이력 취득 */
//    public ContactUserResponse getContactUser(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));
//        List<String> lstOrderNumber = orderRepository.findOrderNumberByUserId(id);
//
//        return ContactUserResponse.builder()
//                .userName(user.getName())
//                .userEmail(user.getEmail())
//                .orderNumbers(lstOrderNumber)
//                .build();
//    }

    /**
     * 문의 이력 취득
     */
    public List<Contact> retrieveAllByUserId(Long userId) {
        return contactRepository.findAllByUser_IdAndStepOrderByIdDesc(userId, 0);
    }

    /**
     * 문의 목록 취득
     */
    public ContactListResponse getContactList(Pageable pageable) {
        List<ContactDto> contactDtoList = new ArrayList<>();
        Page<Contact> contacts = contactRepository.findAllByStepOrderByRefDesc(0, pageable);

        for (Contact contact : contacts) {
            ContactDto contactDto = contactMapper.mapToDto(contact);
            contactDto.setCountAnswer(contactRepository.countByRef(contact.getRef()) - 1);

            contactDtoList.add(contactDto);
        }

        ContactListResponse contactListResponse = ContactListResponse.builder()
                .contacts(contactDtoList)
                .totalPages(contacts.getTotalPages())
                .build();

        return contactListResponse;
    }

    /**
     * 문의 내역 취득
     */
    public List<Contact> getContactDetail(Long userId, Long contactId) {
        Long ref = contactRepository.findRefById(contactId);
        List<Contact> contactList = contactRepository.findAllByRefOrderByStep(ref);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));

        // 해당 Contact의 User ID와 현재 요청한 유저의 ID가 일치하는지 확인 (무결성 체크) ※ 관리자의 경우, 무조건 접근 가능
        if (!user.isAdminFlag() && !contactList.get(0).getUser().getId().equals(userId)) {
            throw new UnauthorizedException("contactUnauthorizedAccess", "이 문의 내역에 대한 접근 권한이 없습니다.");
        }

        return contactList;
    }

    /**
     * 문의 목록 검색
     */
    public ContactListResponse searchContactList(ContactListSearchRequest contactListSearchRequest, Pageable pageable) {
        List<ContactDto> contactDtoList = new ArrayList<>();
        Page<Contact> contactPage = contactRepository.findByCondition(contactListSearchRequest, pageable);

        for (Contact contact : contactPage.getContent()) {
            ContactDto contactDto = contactMapper.mapToDto(contact);
            contactDto.setCountAnswer(contactRepository.countByRef(contact.getRef()) - 1);
            contactDtoList.add(contactDto);
        }

        ContactListResponse contactListResponse = ContactListResponse.builder()
                .contacts(contactDtoList)
                .totalPages(contactPage.getTotalPages())
                .build();

        return contactListResponse;
    }

    /**
     * 문의 글 등록
     */
    public void createInquiry(Long userId, ContactDto contactDto) {

        if (!StringUtils.isNotBlank(contactDto.getInquirerName())) {
            throw new ValidationException("nameMissing", "이름을 입력해주세요.");
        } else if (contactDto.getInquirerName().trim().length() > MAX_NAME_LENGTH) {
            throw new ValidationException("nameTooLong", "이름은 20자 이하로 입력해주세요.");
        }

        if (!StringUtils.isNotBlank(contactDto.getInquirerEmail())) {
            throw new ValidationException("emailMissing", "이메일를 입력해주세요.");
        } else if (!contactDto.getInquirerEmail().trim().matches(EMAIL_PATTERN)) {
            throw new ValidationException("emailInvalidFormat", "이메일이 유효하지 않습니다.");
        }

        if (contactDto.getType() == null) {
            throw new ValidationException("typeMissing", "문의 사항을 선택해주세요.");
        }

        if (!StringUtils.isNotBlank(contactDto.getTitle())) {
            throw new ValidationException("titleMissing", "제목을 입력해주세요.");
        } else if (contactDto.getTitle().trim().length() > MAX_TITLE_LENGTH) {
            throw new ValidationException("titleTooLong", "제목은 100자 이하로 입력해주세요.");
        }

        if (!StringUtils.isNotBlank(contactDto.getContent())) {
            throw new ValidationException("contentMissing", "내용을 입력해주세요.");
        } else if (contactDto.getContent().trim().length() > MAX_CONTENT_LENGTH) {
            throw new ValidationException("contentTooLong", "내용은 5,000자 이하로 입력해주세요.");
        }

        insertInquiry(userId, contactDto);
    }

    /**
     * 문의 글 등록 (DB)
     */
    @Transactional
    public void insertInquiry(Long userId, ContactDto contactDto) {

        // 작성자 정보 취득
        Optional<User> userOptional = userRepository.findById(userId);

        // 문의 글 내용 작성
        Contact contact = new Contact();

        contact.setInquirerName(contactDto.getInquirerName().trim());
        contact.setInquirerEmail(contactDto.getInquirerEmail().trim());
        contact.setOrderNumber(contactDto.getOrderNumber().trim());
        contact.setType(contactDto.getType());
        contact.setTitle(contactDto.getTitle().trim());
        contact.setContent(contactDto.getContent().trim());
        contact.setStatus(ContactStatus.UNANSWERED);
        contact.setRef(contactRepository.findMaxRef() + 1);
        contact.setStep(0);

        if (userOptional.isPresent()) {
            contact.setUser(userOptional.get());
        }

        // DB 등록
        contactRepository.save(contact);
    }

    /**
     * 문의 글 답변 등록
     */
    public void createAnswer(Long userId, ContactDto contactDto) {
        String content = contactDto.getContent();

        if (!StringUtils.isNotBlank(content)) {
            throw new ValidationException("contentMissing", "내용을 입력해주세요.");
        } else if (content.trim().length() > MAX_CONTENT_LENGTH) {
            throw new ValidationException("contentTooLong", "내용은 5,000자 이하로 입력해주세요.");
        }

        insertAnswer(userId, contactDto);
    }

    /**
     * 문의 글 답변 등록 (DB)
     */
    @Transactional
    public void insertAnswer(Long userId, ContactDto contactDto) {
        // 작성자 정보 취득
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));

        // 문의 글 상태 변경
        Contact originContact = contactRepository.findById(contactDto.getId())
                .orElseThrow(() -> new NotFoundException("contactNotFound", "문의 정보를 찾을 수 없습니다."));
        originContact.setStatus(ContactStatus.ANSWERED);

        // 문의 글 답변 내용 작성
        Contact contact = new Contact();

        contact.setUser(user);
        contact.setInquirerName(user.getName().trim());
        contact.setInquirerEmail(user.getEmail().trim());
        contact.setOrderNumber(originContact.getOrderNumber().trim());
        contact.setType(originContact.getType());
        contact.setTitle("RE : " + originContact.getTitle().trim());
        contact.setContent(contactDto.getContent().trim());
        contact.setStatus(ContactStatus.UNANSWERED);
        contact.setRef(originContact.getRef());
        contact.setStep(contactRepository.findMaxStepByRef(originContact.getRef()) + 1);

        // DB 등록
        contactRepository.save(contact);
        contactRepository.save(originContact);
    }

    /**
     * 주어진 문의 정보 목록에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param contactDtoList 업데이트할 문의 정보의 DTO 목록
     */
    @Transactional
    public void updateContacts(List<ContactDto> contactDtoList) {

        for (ContactDto contactDto : contactDtoList) {
            // ID를 이용해 배송 정보 엔티티를 조회하여, 없을 경우 NotFoundException을 발생
            Contact contact = contactRepository.findById(contactDto.getId())
                    .orElseThrow(() -> new NotFoundException("contactNotFound", "문의 정보를 찾을 수 없습니다."));

            // 변경 사항을 감지하여 엔티티를 저장
            if (updateContactDetails(contact, contactDto)) {
                contactRepository.save(contact);
            }
        }
    }

    /**
     * 개별 문의 정보 엔티티에 대한 상세 업데이트를 수행
     * 변경된 필드가 있을 경우에만 업데이트를 진행
     *
     * @param contact    업데이트할 문의 정보 엔티티
     * @param contactDto 업데이트에 사용될 DTO
     * @return 변경 사항이 있었으면 true를, 아니면 false를 반환
     */
    private boolean updateContactDetails(Contact contact, ContactDto contactDto) {
        boolean isModified = false;

        // 문의자 이름, 이메일의 변경 사항을 검사하고 업데이트
        isModified |= updateIfDifferent(contact.getInquirerName(), contactDto.getInquirerName(), contact::setInquirerName);
        isModified |= updateIfDifferent(contact.getInquirerEmail(), contactDto.getInquirerEmail(), contact::setInquirerEmail);

        // 문의 구분 변경이 있을 경우 업데이트
        if (contactDto.getType() != null && contact.getType() != contactDto.getType()) {
            contact.setType(contactDto.getType());
            isModified = true; // 상태가 변경되었다면 수정됨으로 표시
        }

        // 문의 상태 변경이 있을 경우 업데이트
        if (contactDto.getStatus() != null && contact.getStatus() != contactDto.getStatus()) {
            contact.setStatus(contactDto.getStatus());
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
}