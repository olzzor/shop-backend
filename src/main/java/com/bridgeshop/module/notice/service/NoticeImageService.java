package com.bridgeshop.module.notice.service;

import com.bridgeshop.common.service.S3UploadService;
import com.bridgeshop.module.notice.dto.NoticeImageDto;
import com.bridgeshop.module.notice.entity.Notice;
import com.bridgeshop.module.notice.entity.NoticeImage;
import com.bridgeshop.module.notice.entity.NoticeImageType;
import com.bridgeshop.module.notice.mapper.NoticeImageMapper;
import com.bridgeshop.module.notice.repository.NoticeImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeImageService {

    private final S3UploadService s3UploadService;
    private final NoticeImageRepository noticeImageRepository;
    private final NoticeImageMapper noticeImageMapper;

    /**
     * 상품 파일 취득
     */
    public NoticeImageDto convertToDto(NoticeImage noticeImage) {
        return noticeImageMapper.mapToDto(noticeImage);
    }

    /**
     * 상품 파일 리스트 취득
     */
    public List<NoticeImageDto> convertToDtoList(List<NoticeImage> noticeImageList) {
        return noticeImageMapper.mapToDtoList(noticeImageList);
    }

    @Transactional
    public void saveNoticeImage(Long userId, Notice notice, NoticeImageType noticeImageType, MultipartFile file) {

        String extension = extractFileExtension(file.getOriginalFilename()); // 원본 파일 이름에서 파일 확장자 추출
        String fileName = createFileName(noticeImageType, userId, extension); // 저장할 파일 이름 생성
        String fileKey = "notices/" + fileName; // S3에 저장될 파일의 키를 'notices/' 디렉토리와 생성된 파일 이름으로 결합
        String s3Url = s3UploadService.saveFile(file, fileKey); // S3에 파일 업로드 및 URL 반환

        NoticeImage noticeImage = NoticeImage.builder()
                .type(noticeImageType)
                .notice(notice)
                .fileUrl(s3Url)
                .fileKey(fileKey)
                .build();

        noticeImageRepository.save(noticeImage); // 생성된 NoticeImage 를 DB에 저장
    }

    @Transactional
    public void updateNoticeImage(Long userId, Notice notice, NoticeImageType noticeImageType, MultipartFile file) {
        Optional<NoticeImage> noticeImageOptional = noticeImageRepository.findByTypeAndNotice_Id(noticeImageType, notice.getId());

        // 기존 NoticeImage 가 존재하는 경우
        if (noticeImageOptional.isPresent()) {
            NoticeImage noticeImage = noticeImageOptional.get();
            s3UploadService.deleteImage(noticeImage.getFileKey()); // S3에 업로드 된 변경 전 이미지 파일 삭제
            noticeImageRepository.delete(noticeImage); // 변경 전 NoticeImage 를 DB에서 삭제
        }

        saveNoticeImage(userId, notice, noticeImageType, file); // S3에 파일 업로드 및 생성된 NoticeImage 를 DB에 저장
    }

    /**
     * 파일 확장자 추출 메소드
     */
    private String extractFileExtension(String originalFileName) {
        int lastDot = originalFileName.lastIndexOf('.');
        return (lastDot > 0) ? originalFileName.substring(lastDot) : "";
    }

    /**
     * 파일 이름 생성 메소드
     * 공지 유형, 사용자 ID, 현재 날짜와 시간을 조합하여 파일 이름 생성
     */
    private String createFileName(NoticeImageType noticeImageType, Long userId, String extension) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // 날짜 포맷 설정
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 시간대 설정
        String formattedDate = sdf.format(new Date()); // 현재 날짜와 시간을 위의 포맷으로 변환

        // 공지 유형, 사용자 ID, 현재 날짜와 시간, 파일 확장자를 결합하여 파일 이름 생성
        return noticeImageType + "_" + userId + "_" + formattedDate + extension;
    }
}