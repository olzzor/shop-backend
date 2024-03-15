package com.shop.module.notice.service;

import com.shop.common.exception.NotFoundException;
import com.shop.common.exception.ValidationException;
import com.shop.module.notice.dto.NoticeListSearchRequest;
import com.shop.module.notice.entity.*;
import com.shop.module.notice.repository.NoticeRepository;
import com.shop.module.notice.dto.NoticeDto;
import com.shop.module.notice.dto.NoticeImageDto;
import com.shop.module.notice.mapper.NoticeImageMapper;
import com.shop.module.notice.mapper.NoticeMapper;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeImageService noticeImageService;
    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;
    private final NoticeImageMapper noticeImageMapper;
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_CONTENT_LENGTH = 10000;

    public NoticeDto convertToDto(Notice notice) {
        return noticeMapper.mapToDto(notice);
    }

    public List<NoticeDto> convertToDtoList(List<Notice> noticeList) {
        return noticeMapper.mapToDtoList(noticeList);
    }

    public Notice retrieveById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("noticeNotFound", "공지 정보를 찾을 수 없습니다."));
    }

    public Page<Notice> retrieveAllPaginated(Pageable pageable) {
        return noticeRepository.findAllByNoticeImages_Type(NoticeImageType.MAIN, pageable);
    }

    public List<Notice> retrieveActiveNoticeListWithMainImageByType(NoticeType type) {
        return noticeRepository.findAllByStatusAndTypeAndNoticeImages_Type(NoticeStatus.ACTIVE, type, NoticeImageType.MAIN, Sort.by(Sort.Order.desc("regDate")));
    }

    public List<Notice> retrieveActiveNoticeListWithMainImage() {
        return noticeRepository.findAllByStatusAndNoticeImages_Type(NoticeStatus.ACTIVE, NoticeImageType.MAIN, Sort.by(Sort.Order.desc("regDate")));
    }

    public List<Notice> retrieveActiveNoticeListWithSliderImage() {
        List<Notice> notices = noticeRepository.findActiveNoticesWithSliderImage(NoticeStatus.ACTIVE, NoticeImageType.SLIDER);
        return notices.stream()
                .sorted(Comparator.comparing(Notice::getRegDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Notice> retrieveActiveNoticeListWithModalImage() {
        return noticeRepository.findAllByStatusAndNoticeImages_Type(NoticeStatus.ACTIVE, NoticeImageType.MODAL, Sort.by(Sort.Order.desc("regDate")));
    }

    public Page<Notice> searchAllPaginated(NoticeListSearchRequest noticeListSearchRequest, Pageable pageable) {
        return noticeRepository.findByCondition(noticeListSearchRequest, pageable);
    }

    public NoticeDto getDto(Notice notice) {
        List<NoticeImageDto> noticeImageDtoList = noticeImageService.convertToDtoList(notice.getNoticeImages());
        NoticeDto noticeDto = noticeMapper.mapToDto(notice);
        noticeDto.setNoticeImages(noticeImageDtoList);

        return noticeDto;
    }

    public List<NoticeDto> getDtoListWithImages(List<Notice> noticeList) {
        return noticeList.stream().map(this::getDtoWithImages).collect(Collectors.toList());
    }

    public NoticeDto getDtoWithImages(Notice notice) {
        NoticeDto noticeDto = noticeMapper.mapToDto(notice);

        // 각 이미지 타입에 대한 이미지를 설정
        for (NoticeImage noticeImage : notice.getNoticeImages()) {
            NoticeImageType imageType = noticeImage.getType();

            if (imageType == NoticeImageType.MAIN) {
                noticeDto.setMainImage(noticeImageMapper.mapToDto(noticeImage));
            } else if (imageType == NoticeImageType.SLIDER) {
                noticeDto.setSliderImage(noticeImageMapper.mapToDto(noticeImage));
            } else if (imageType == NoticeImageType.MODAL) {
                noticeDto.setModalImage(noticeImageMapper.mapToDto(noticeImage));
            }
        }

        return noticeDto;
    }

    public List<NoticeDto> getDtoListWithMainImage(List<Notice> noticeList) {
        return noticeList.stream().map(this::getDtoWithMainImage).collect(Collectors.toList());
    }

    public NoticeDto getDtoWithMainImage(Notice notice) {
        NoticeDto noticeDto = noticeMapper.mapToDto(notice);

        // 메인 이미지 타입에 대한 이미지를 설정
        for (NoticeImage noticeImage : notice.getNoticeImages()) {
            NoticeImageType imageType = noticeImage.getType();

            if (imageType == NoticeImageType.MAIN) {
                noticeDto.setMainImage(noticeImageMapper.mapToDto(noticeImage));
            }
        }

        return noticeDto;
    }

    public List<NoticeDto> getDtoListWithSliderImage(List<Notice> noticeList) {
        return noticeList.stream().map(this::getDtoWithSliderImage).collect(Collectors.toList());
    }

    public NoticeDto getDtoWithSliderImage(Notice notice) {
        NoticeDto noticeDto = noticeMapper.mapToDto(notice);

        // 슬라이더 이미지 타입에 대한 이미지를 설정
        for (NoticeImage noticeImage : notice.getNoticeImages()) {
            NoticeImageType imageType = noticeImage.getType();

            if (imageType == NoticeImageType.SLIDER) {
                noticeDto.setSliderImage(noticeImageMapper.mapToDto(noticeImage));
            }
        }

        return noticeDto;
    }

    public List<NoticeDto> getDtoListWithModalImage(List<Notice> noticeList) {
        return noticeList.stream().map(this::getDtoWithModalImage).collect(Collectors.toList());
    }

    public NoticeDto getDtoWithModalImage(Notice notice) {
        NoticeDto noticeDto = noticeMapper.mapToDto(notice);

        // 모달 이미지 타입에 대한 이미지를 설정
        for (NoticeImage noticeImage : notice.getNoticeImages()) {
            NoticeImageType imageType = noticeImage.getType();

            if (imageType == NoticeImageType.MODAL) {
                noticeDto.setModalImage(noticeImageMapper.mapToDto(noticeImage));
            }
        }

        return noticeDto;
    }

    public void checkInput(NoticeDto noticeDto) {

        // type 체크
        if (noticeDto.getType() == null) {
            throw new ValidationException("typeMissing", "유형을 선택해주세요.");
        }

        // title 체크
        if (StringUtils.isBlank(noticeDto.getTitle())) {
            throw new ValidationException("titleMissing", "제목을 입력해주세요.");
        } else if (noticeDto.getTitle().trim().length() > MAX_TITLE_LENGTH) {
            throw new ValidationException("titleTooLong", "제목은 50자 이하로 입력해주세요.");
        }

        // content 체크
        if (StringUtils.isBlank(noticeDto.getContent())) {
            throw new ValidationException("contentMissing", "내용을 입력해주세요.");
        } else if (noticeDto.getContent().trim().length() > MAX_CONTENT_LENGTH) {
            throw new ValidationException("contentTooLong", "내용은 1000자 이하로 입력해주세요.");
        }

        // status 체크
        if (noticeDto.getStatus() == null) {
            throw new ValidationException("statusMissing", "공지 상태를 선택해주세요.");
        }
    }

    @Transactional
    public Notice insertNotice(NoticeDto noticeDto, boolean isSliderImage, boolean isModalImage) {

        Notice notice = Notice.builder()
                .type(noticeDto.getType())
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .status(noticeDto.getStatus())
                .isSliderImage(isSliderImage)   // 추가
                .isModalImage(isModalImage)     // 추가
                .build();

        noticeRepository.save(notice);

        return notice;
    }

    /**
     * 주어진 공지 정보에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param notice    업데이트할 공지 정보 엔티티
     * @param noticeDto 업데이트에 사용될 DTO
     */
    @Transactional
    public void updateNoticeContent(Notice notice, NoticeDto noticeDto) {
        boolean isModified = false;

        isModified |= updateIfDifferent(notice.getContent(), noticeDto.getContent(), notice::setContent);

        // 변경 사항을 감지하여 엔티티를 저장
        if (isModified) {
            noticeRepository.save(notice);
        }
    }

    /**
     * 주어진 공지 정보에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param notice    업데이트할 공지 정보 엔티티
     * @param noticeDto 업데이트에 사용될 DTO
     */
    @Transactional
    public void updateNotice(Notice notice, NoticeDto noticeDto) {

        // 변경 사항을 감지하여 엔티티를 저장
        if (updateNoticeDetails(notice, noticeDto)) {
            noticeRepository.save(notice);
        }
    }

    /**
     * 주어진 공지 정보 목록에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param noticeDtoList 업데이트할 공지 정보의 DTO 목록
     */
    @Transactional
    public void updateNotices(List<NoticeDto> noticeDtoList) {

        for (NoticeDto noticeDto : noticeDtoList) {
            // ID를 이용해 배송 정보 엔티티를 조회하여, 없을 경우 NotFoundException을 발생
            Notice notice = noticeRepository.findById(noticeDto.getId())
                    .orElseThrow(() -> new NotFoundException("noticeNotFound", "공지 정보를 찾을 수 없습니다."));

            // 변경 사항을 감지하여 엔티티를 저장
            if (updateNoticeDetails(notice, noticeDto)) {
                noticeRepository.save(notice);
            }
        }
    }

    /**
     * 개별 공지 정보 엔티티에 대한 상세 업데이트를 수행
     * 변경된 필드가 있을 경우에만 업데이트를 진행
     *
     * @param notice    업데이트할 공지 정보 엔티티
     * @param noticeDto 업데이트에 사용될 DTO
     * @return 변경 사항이 있었으면 true를, 아니면 false를 반환
     */
    private boolean updateNoticeDetails(Notice notice, NoticeDto noticeDto) {
        boolean isModified = false;

        // 공지 제목, 내용의 변경 사항을 검사하고 업데이트
        isModified |= updateIfDifferent(notice.getTitle(), noticeDto.getTitle(), notice::setTitle);

        // 공지 구분 변경 확인 및 업데이트
        if (notice.getType() != noticeDto.getType()) {
            notice.setType(noticeDto.getType());
            isModified = true;
        }

        // 공지 상태 변경 확인 및 업데이트
        if (notice.getStatus() != noticeDto.getStatus()) {
            notice.setStatus(noticeDto.getStatus());
            isModified = true;
        }

        // 슬라이더 이미지 표시 여부 변경 확인 및 업데이트
        if (noticeDto.getIsSliderImage() != null && notice.isSliderImage() != noticeDto.getIsSliderImage()) {
            notice.setSliderImage(noticeDto.getIsSliderImage());
            isModified = true;
        }

        // 모달 이미지 표시 여부 변경 확인 및 업데이트
        if (noticeDto.getIsModalImage() != null && notice.isModalImage() != noticeDto.getIsModalImage()) {
            notice.setModalImage(noticeDto.getIsModalImage());
            isModified = true;
        }

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