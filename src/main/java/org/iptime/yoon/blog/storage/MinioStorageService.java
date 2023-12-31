package org.iptime.yoon.blog.storage;

import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.image.exception.ImageDownloadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
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
    @Override
    public void upload(String objectName, String contentType, byte[] data) throws Exception {
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
    public byte[] download(String filename) throws ImageDownloadException {
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
    public void delete(String filename) throws Exception {
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .build()
        );
    }

    @Override
    public String getUrl(String objectName) {
        try {
            GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(24 * 60 * 60)
                .build();
            return minioClient.getPresignedObjectUrl(args);
        }catch(Exception e){
            log.error("Failed To download File : " + objectName, e);
            throw new RuntimeException(e);
        }
    }
}
