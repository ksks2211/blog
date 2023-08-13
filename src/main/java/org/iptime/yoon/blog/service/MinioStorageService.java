package org.iptime.yoon.blog.service;

import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.iptime.yoon.blog.exception.ImageDownloadException;
import org.iptime.yoon.blog.exception.ImageUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author rival
 * @since 2023-08-13
 */

@Service
@Slf4j
public class MinioStorageService implements StorageService{
    private final MinioClient minioClient;
    private final String bucketName;
    private static final int THUMBNAIL_WIDTH = 100;
    private static final int THUMBNAIL_HEIGHT = 100;

    public MinioStorageService(
        @Value("${minio.endpoint}") String endpoint,
        @Value("${minio.accessKey}") String accessKey,
        @Value("${minio.secretKey}") String secretKey,
        @Value("${minio.bucketName}") String bucketName
    ) {
        minioClient = MinioClient.builder()
            .endpoint(endpoint)
            .credentials(accessKey, secretKey)
            .build();
        this.bucketName = bucketName;
    }

    private byte[] createThumbnail(byte[] imageData) throws IOException {
        try (ByteArrayOutputStream thumbnailOutput = new ByteArrayOutputStream()) {
            Thumbnailator.createThumbnail(new ByteArrayInputStream(imageData), thumbnailOutput , THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
            return thumbnailOutput.toByteArray();
        }
    }

    private void uploadToMinio(String objectName, String contentType, byte[] data) throws Exception {
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .contentType(contentType)
                .stream(new ByteArrayInputStream(data), data.length, -1)
                .build()
        );
    }
    @Override
    public void uploadFile(String filename, MultipartFile multipartFile) throws ImageUploadException {
        try {
            byte[] imageData = multipartFile.getBytes();
            byte[] thumbData = createThumbnail(imageData);

            uploadToMinio(filename, multipartFile.getContentType(), imageData);
            uploadToMinio(filename + ".thumb", multipartFile.getContentType(), thumbData);
        } catch(Exception e) {
             log.error("Failed to upload file: " + filename, e);
            throw new ImageUploadException(filename, e);
        }
    }
    @Override
    public byte[] downloadFile(String filename) throws ImageDownloadException {
        try(InputStream stream = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .build())) {
            return stream.readAllBytes();
        } catch (Exception e) {
            log.error("Failed to download file: " + filename, e);
            throw new ImageDownloadException(filename,e);
        }
    }

    @Override
    public void deleteFile(String filename) throws Exception {
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .build()
        );
    }
}
