package com.lec.spring.global.common.util.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.lec.spring.global.common.util.BucketDirectory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Override
    public String uploadFile(MultipartFile file, BucketDirectory bucketDirectory) {
        try {
            String fileName = addTimestampToFilename(file.getOriginalFilename());
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            String fileUrl = "https://" + bucket + ".s3." + region +".amazonaws.com/"+ bucketDirectory +"/" + encodedFileName;

            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, bucketDirectory + "/" + fileName, file.getInputStream(), metadata);

            return fileUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String uploadImgFile(MultipartFile file, BucketDirectory bucketDirectory) {
        if (Objects.requireNonNull(file.getContentType()).startsWith("image")) {
            return uploadFile(file, bucketDirectory);
        }

        throw new IllegalArgumentException("이미지 파일이 아닙니다.");
    }

    private String addTimestampToFilename(String filename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return filename + "_" + timestamp;
        } else {
            return filename.substring(0, lastDotIndex) + "_" + timestamp + filename.substring(lastDotIndex);
        }
    }

    @Override
    public void deleteFile(String url) {
        String fileName = url.substring(54);
        String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        amazonS3Client.deleteObject(bucket, decodedFileName);
    }
}
