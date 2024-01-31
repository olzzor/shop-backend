package com.bridgeshop.common.controller;

import com.bridgeshop.common.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/toast-ui")
public class ToastUIController {

    private final FileUploadService fileUploadService;

    @PostMapping("/image-upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file,
                                         @RequestHeader("upload-type") String uploadType) {
        try {
            // 헤더 값에 따른 저장 경로 취득
            String uploadDir = fileUploadService.getUploadDir(uploadType);

            // 파일을 저장하는 로직
            String savedFileName = fileUploadService.saveFile(file, uploadDir);

            // 응답 형식
            Map<String, String> response = new HashMap<>();
            String savedFilePath = "http://localhost.test:8080/upload/" + uploadType + "/" + savedFileName;
            response.put("url", savedFilePath); // 저장된 파일의 URL

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed");
        }
    }
}