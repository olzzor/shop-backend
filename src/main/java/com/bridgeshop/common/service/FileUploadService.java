package com.bridgeshop.common.service;

import com.bridgeshop.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

    @Value("${upload.directory.product}")
    private String productUploadDir;

    @Value("${upload.directory.notice}")
    private String noticeUploadDir;

    @Value("${upload.directory.review}")
    private String reviewUploadDir;


    public void checkInputFiles(List<MultipartFile> files) {
        if (files.isEmpty()) {
            throw new ValidationException("filesEmpty", "하나 이상의 이미지 파일을 업로드해주세요.");
        }
        files.forEach(this::checkInputFile);
    }

    public void checkInputFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ValidationException("fileEmpty", "이미지가 존재하지 않습니다.");
        }
    }

    public String saveFile(MultipartFile file, String uploadDir) throws IOException {

        // 저장할 디렉토리의 절대 경로
        Path uploadPath = Paths.get(uploadDir);
        // 디렉토리가 존재하지 않는다면 생성
        Files.createDirectories(uploadPath);

        // 원본 파일 이름
        String originalFilename = file.getOriginalFilename();
        // 파일을 저장할 절대 경로
        Path filePath = uploadPath.resolve(originalFilename);

        // 파일 저장
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 저장된 파일의 경로 반환
        return originalFilename;
    }

    public void uploadFile(MultipartFile file, String fileName, String uploadType) {

        try {
            String uploadDir = getUploadDir(uploadType);

            // 디렉토리 확인 및 생성
            Path dirPath = Paths.get(uploadDir);
            Files.createDirectories(dirPath);

            // 확장자 유효성 검사
//            if (!extension.equalsIgnoreCase(".jpg") &&
//                    !extension.equalsIgnoreCase(".png") &&
//                    !extension.equalsIgnoreCase(".gif")) {
//                throw new RuntimeException("Invalid file extension: " + extension);
//            }

            // 파일을 저장할 절대 경로
            Path filePath = dirPath.resolve(fileName);

            File dest = filePath.toFile();
            file.transferTo(dest);

            // 로그 기록
            log.info("File uploaded successfully to " + filePath.toString());

        } catch (IOException e) {
            log.error("File upload failed", e);
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String fileName) {

        try {
            Path filePath = Paths.get(fileName);
            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            log.error("Failed to delete file: {}", fileName, e);
            throw new RuntimeException("File delete failed", e);
        }
    }

    public String getUploadDir(String uploadType) {

        // 헤더 값에 따라 저장 경로 변경
        switch (uploadType) {
            case "products":
                return productUploadDir;
            case "notices":
                return noticeUploadDir;
            case "reviews":
                return reviewUploadDir;
            default:
                throw new IllegalArgumentException("Invalid upload type: " + uploadType);
        }
    }
}