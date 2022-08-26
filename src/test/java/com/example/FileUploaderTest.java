package com.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class FileUploaderTest {

    static DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:1.0.4");

    @Container
    static LocalStackContainer localStack = new LocalStackContainer(localstackImage)
            .withServices(LocalStackContainer.Service.S3);

    static S3Client s3Client;

    static String bucketName = "sample-bucket";

    static FileUploader fileUploader;

    @BeforeAll
    public static void beforeAll() {
        // コンテナ内LocalStackのS3にアクセスするS3Clientを作成
        s3Client = S3Client.builder()
                .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.S3))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                localStack.getAccessKey(),
                                localStack.getSecretKey())))
                .region(Region.of(localStack.getRegion()))
                .build();

        // S3バケットを事前に作成
        CreateBucketResponse createBucketResponse = s3Client.createBucket(builder -> builder.bucket(bucketName));
        S3Waiter s3Waiter = s3Client.waiter();
        HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                .bucket(bucketName)
                .build();
        WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);

        // テスト対象クラスのインスタンスを生成
        fileUploader = new FileUploader(s3Client);
    }

    @Test
    @DisplayName("S3にファイルをアップロードできる")
    public void test01() {
        // アップロード実行
        fileUploader.upload("てすと");

        // ファイルが1件アップロードできたかをチェック
        assertEquals(1, s3Client.listObjectsV2(builder -> builder.bucket(bucketName)).keyCount());
    }
}
