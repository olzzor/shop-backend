package com.bridgeshop.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.bridgeshop.common.exception.FileUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 파일 업로드
     * S3에 파일을 업로드하고, 업로드된 파일의 URL을 반환
     */
    public String saveFile(MultipartFile multipartFile, String fileKey) {

        try {
            ObjectMetadata metadata = new ObjectMetadata(); // 파일 메타데이터 설정
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            amazonS3.putObject(bucket, fileKey, multipartFile.getInputStream(), metadata); // S3에 파일 업로드
            return amazonS3.getUrl(bucket, fileKey).toString(); // 업로드된 파일의 URL 반환

        } catch (IOException e) {
            // S3에 이미 파일이 업로드 되었다면 삭제
            if (amazonS3.doesObjectExist(bucket, fileKey)) {
                deleteImage(fileKey);
            }

            throw new FileUploadException("fileUploadFailed", "파일 업로드에 실패하였습니다.");
        }
    }

    /**
     * 파일 다운로드
     */
    public ResponseEntity<UrlResource> downloadImage(String fileKey) {
        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, fileKey));

        String contentDisposition = "attachment; filename=\"" + fileKey + "\"";

        // header에 CONTENT_DISPOSITION 설정을 통해 클릭 시 다운로드 진행
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

    /**
     * 파일 삭제
     */
    public void deleteImage(String fileKey) {
        amazonS3.deleteObject(bucket, fileKey);
    }
}