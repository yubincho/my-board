package com.example.demo.service;


import com.example.demo.entity.Image;
import com.example.demo.entity.Question;
import com.example.demo.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

// 썸네일 이미지 저장
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @Transactional
    public Image saveImage(MultipartFile file, Question question, boolean isThumbnail) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String savedName = uuid + extension;
        String filePath = uploadPath + File.separator + savedName;

        File saveFile = new File(filePath);
        file.transferTo(saveFile);

        if (isThumbnail) {
            String thumbnailName = "s_" + savedName;
            File thumbnailFile = new File(uploadPath + File.separator + thumbnailName);
            Thumbnails.of(saveFile)
                    .size(100, 100)
                    .toFile(thumbnailFile);

            return imageRepository.save(Image.createImage(uuid, thumbnailName, uploadPath, question, true));
        }

        return imageRepository.save(Image.createImage(uuid, savedName, uploadPath, question, false));
    }
}
