package com.bridgeshop.module.notice.service;

import com.bridgeshop.module.notice.repository.NoticeImageRepository;
import com.bridgeshop.module.notice.dto.NoticeImageDto;
import com.bridgeshop.module.notice.entity.Notice;
import com.bridgeshop.module.notice.entity.NoticeImage;
import com.bridgeshop.module.notice.entity.NoticeImageType;
import com.bridgeshop.module.notice.mapper.NoticeImageMapper;
import com.bridgeshop.common.service.FileUploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    private final FileUploadService fileUploadService;
    private final NoticeImageRepository noticeImageRepository;
    private final NoticeImageMapper noticeImageMapper;

    @Value("${upload.directory.notice}")
    private String uploadDir;

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

//    /**
//     * 공지 이미지 파일 리스트 취득 (메인 표시 이미지만)
//     */
//    public List<NoticeImageFileDto> getNoticeImageFileDtoListForMain(List<NoticeImageFile> noticeImageFileList) {
//
//        return noticeImageFileList.stream()
//                .filter(noticeImageFile -> noticeImageFile.getType() == NoticeImageType.MAIN)
//                .map(this::getNoticeImageFileDto)
//                .collect(Collectors.toList());
//    }

    @Transactional
    public void updateNoticeImage(Long userId, Notice notice, NoticeImageType noticeImageType, MultipartFile file) {
        Optional<NoticeImage> noticeImageOptional = noticeImageRepository.findByTypeAndNotice_Id(noticeImageType, notice.getId());

        // 변경 전 이미지가 존재하는 경우, 해당 이미지를 파일 서버, DB로부터 삭제
        if (noticeImageOptional.isPresent()) {
            NoticeImage noticeImage = noticeImageOptional.get();

            fileUploadService.deleteFile(uploadDir + noticeImage.getFileName());
            noticeImageRepository.delete(noticeImage);
        }

        // 변경 후 이미지를 저장
        saveNoticeImage(userId, notice, noticeImageType, file);
    }

    @Transactional
    public void saveNoticeImage(Long userId, Notice notice, NoticeImageType noticeImageType, MultipartFile file) {

        // 원본 파일 이름에서 파일 확장자 추출
        String extension = extractFileExtension(file.getOriginalFilename());
        // 저장할 파일 이름 생성
        String fileName = createFileName(noticeImageType, userId, extension);

        NoticeImage noticeImage = new NoticeImage();

        noticeImage.setType(noticeImageType);
        noticeImage.setNotice(notice);
        noticeImage.setFilePath("/img/upload/notices/");
        noticeImage.setFileName(fileName);

        fileUploadService.uploadFile(file, fileName, "notices");

        noticeImageRepository.save(noticeImage);
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
     */
    private String createFileName(NoticeImageType noticeImageType, Long userId, String extension) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // 년월일시분초 밀리초 포맷
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 시간대 설정
        String formattedDate = sdf.format(new Date()); // 현재 날짜와 시간을 위의 포맷으로 변환

        return "notice_" + noticeImageType + "_" + userId + "_" + formattedDate + extension;
    }
}