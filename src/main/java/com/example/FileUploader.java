package com.example;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

/**
 * AWS S3にファイルをアップロードするクラスです。
 */
public class FileUploader {

    private final S3Client s3Client;

    public FileUploader(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * AWS S3にファイルをアップロードします。
     * バケット名はsample-bucket、キーはUUIDです。
     *
     * @param content S3にアップロードするテキストファイルの内容
     */
    public void upload(String content) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket("sample-bucket")
                .key(UUID.randomUUID().toString())
                .build();
        s3Client.putObject(request, RequestBody.fromString(content));
    }
}
