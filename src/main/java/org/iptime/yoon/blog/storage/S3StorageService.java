package org.iptime.yoon.blog.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.ByteBuffer;

/**
 * @author rival
 * @since 2024-03-06
 */

@Service
public class S3StorageService implements StorageService{

    private final S3Client s3Client;
    private final String bucketName;


    private final String baseUrl;

    public S3StorageService(
        @Value("${cloud.aws.region.static}") String region,
        @Value("${cloud.aws.s3.bucket}") String bucketName,
        @Value("${cdn.baseUrl}") String baseUrl
        ) {

        this.s3Client = S3Client.builder()
            .region(Region.of(region))
            .build();
        this.bucketName = bucketName;
        this.baseUrl = baseUrl;
    }

    @Override
    public byte[] download(String objectName) {
        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(GetObjectRequest.builder()
            .bucket(bucketName)
            .key(objectName)
            .build());
        return objectBytes.asByteArray();
    }

    @Override
    public void upload(String objectName, String contentType, byte[] data) {
        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucketName)
                .contentType(contentType)
                .key(objectName)
                .build(),
            RequestBody.fromByteBuffer(ByteBuffer.wrap(data))
        );
    }

    @Override
    public void delete(String objectName) throws Exception {
        s3Client.deleteObject(DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(objectName)
            .build());

    }

    @Override
    public Resource downloadAsStream(String objectName)  {
        ResponseInputStream<GetObjectResponse> object = s3Client.getObject(GetObjectRequest.builder()
            .bucket(bucketName)
            .key(objectName)
            .build());
        return new InputStreamResource(object);
    }

    @Override
    public String getUrl(String objectName) {
        return baseUrl+"/"+objectName;
    }
}
