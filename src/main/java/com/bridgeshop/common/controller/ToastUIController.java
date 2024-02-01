package com.bridgeshop.common.controller;

import com.bridgeshop.common.service.FileUploadService;
import com.bridgeshop.common.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/toast-ui")
public class ToastUIController {

    private final FileUploadService fileUploadService;
    private final S3UploadService s3UploadService;

    @PostMapping("/image-upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file,
                                         @RequestHeader("upload-type") String uploadType) {
        try {
            // 원본 파일 이름에서 파일 확장자 추출
            String originalFileName = file.getOriginalFilename();
            int lastDot = originalFileName.lastIndexOf('.');
            String extension = (lastDot > 0) ? originalFileName.substring(lastDot) : "";

            // 저장할 파일 이름 생성
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // 날짜 포맷 설정
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 시간대 설정
            String formattedDate = sdf.format(new Date()); // 현재 날짜와 시간을 위의 포맷으로 변환

            String fileName = formattedDate + extension; // 현재 날짜와 시간, 파일 확장자를 결합하여 파일 이름 생성
            String fileKey = "notices/contents/" + fileName; // S3에 저장될 파일의 키를 'notices/contents/' 디렉토리와 생성된 파일 이름으로 결합

            Map<String, String> response = new HashMap<>();
            String s3Url = s3UploadService.saveFile(file, fileKey); // S3에 파일 업로드 및 URL 반환
            response.put("url", s3Url); // 저장된 파일의 URL
            return ResponseEntity.ok(s3Url);

//            // 헤더 값에 따른 저장 경로 취득
//            String uploadDir = fileUploadService.getUploadDir(uploadType);
//
//            // 파일을 저장하는 로직
//            String savedFileName = fileUploadService.saveFile(file, uploadDir);
//
//            // 응답 형식
//            Map<String, String> response = new HashMap<>();
//            String savedFilePath = "http://localhost.test:8080/upload/" + uploadType + "/" + savedFileName;
//            response.put("url", savedFilePath); // 저장된 파일의 URL
//
//            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed");
        }
    }
}