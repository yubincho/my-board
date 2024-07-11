package com.example.demo.controller;


import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostMapping("/uploadAjax")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile[] uploadFiles) {
        // 파일이 null인지 확인
        if (uploadFiles == null || uploadFiles.length == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No files to upload");
        }

        StringBuilder fileUrls = new StringBuilder();

        for (MultipartFile uploadFile : uploadFiles) {
            // 업로드 된 파일의 이름
            String originalName = uploadFile.getOriginalFilename();

            // 업로드 된 파일의 확장자
            assert originalName != null;
            String fileExtension = originalName.substring(originalName.lastIndexOf("."));

            // 업로드 될 파일의 이름 재설정 (중복 방지를 위해 UUID 사용)
            String uuidFileName = UUID.randomUUID().toString() + fileExtension;

            try {
                // 위에서 설정한 서버 경로에 이미지 저장
                File saveFile = new File(uploadPath, uuidFileName);
                uploadFile.transferTo(saveFile);

                // 파일 접근 경로 추가
                fileUrls.append("/files/").append(uuidFileName).append(" ");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload files");
            }
        }

        // JSON 형태로 반환
        return ResponseEntity.ok("{\"url\":\"" + fileUrls.toString().trim() + "\"}");
    }


    @PostMapping("/uploadThumbnail")
    public ResponseEntity<?> uploadThumbnail(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded");
        }

        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String savedName = uuid + extension;

        try {
            // 원본 이미지 저장
            File saveFile = new File(uploadPath, savedName);
            file.transferTo(saveFile);

            // 썸네일 이미지 생성 및 저장
            String thumbnailName = "s_" + savedName;
            File thumbnailFile = new File(uploadPath, thumbnailName);
            Thumbnails.of(saveFile)
                    .size(200, 200) // 썸네일 크기 조정
                    .toFile(thumbnailFile);

            String thumbnailUrl = "/files/" + thumbnailName;

            // JSON 형태로 반환
            return ResponseEntity.ok("{\"url\":\"" + thumbnailUrl + "\"}");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload thumbnail");
        }
    }

}







