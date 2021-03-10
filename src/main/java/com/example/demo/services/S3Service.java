package com.example.demo.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3Service {
    //@Value("${aws.user.access.key}") String accessKey;
    //@Value("${aws.user.secret.key}") String secretKey;
    //@Value("${aws.region}") String awsRegion;

    AmazonS3 s3client;

    @PostConstruct
    public void init() {
        //
        //AWSCredentials credentials = new BasicAWSCredentials(
        //        accessKey,
        //        secretKey
        //);
        //
        s3client = AmazonS3ClientBuilder
                .standard()
                //.withCredentials(new AWSStaticCredentialsProvider(credentials))
                //.withRegion(Regions.fromName(awsRegion))
                .build();
    }
    //
    public void uploadFileIntoBucket(String bucketName, String key, InputStream is, String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        s3client.putObject(bucketName, key, is, objectMetadata);
    }
    //
    public S3Object getFileFromBucket(String bucketName, String key) {
        return s3client.getObject(bucketName, key);
    }
    //
    public List<String> listFilesInFolder(String bucketName, String prefix) {
        List<String> outValue = new ArrayList<>();
        ObjectListing objectListing = s3client.listObjects(bucketName, prefix);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        for (S3ObjectSummary s3ObjectSummary : objectSummaries) {
            if (!prefix.equals(s3ObjectSummary.getKey())) {
                //
                outValue.add(s3ObjectSummary.getKey());
            }
        }
        ListVersionsRequest listVersionsRequest = new ListVersionsRequest();
        listVersionsRequest.setBucketName(bucketName);
        listVersionsRequest.setPrefix(prefix);
        VersionListing versionListing = s3client.listVersions(listVersionsRequest);
        List<S3VersionSummary> versionSummaries = versionListing.getVersionSummaries();
        for (S3VersionSummary nextVersion : versionSummaries) {
            System.out.println("ZZZ nextVersion key - " + nextVersion.getKey());
            System.out.println("ZZZ nextVersion version - " + nextVersion.getVersionId());
            System.out.println("ZZZ nextVersion latest - " + nextVersion.isLatest());
            System.out.println("ZZZ nextVersion storageClass - " + nextVersion.getStorageClass());
            System.out.println("ZZZ nextVersion lastModified - " + nextVersion.getLastModified());
            System.out.println("ZZZ nextVersion deleteMarker - " + nextVersion.isDeleteMarker());
            System.out.println("ZZZ nextVersion size - " + nextVersion.getSize());
            System.out.println("ZZZ nextVersion owner - " + nextVersion.getOwner());
        }
        return outValue;
    }
    //
    public byte[] readContent(S3Object s3Object) throws IOException {

        ByteArrayOutputStream outValue = new ByteArrayOutputStream();
        S3ObjectInputStream objectContent = s3Object.getObjectContent();
        int nextByte;
        while ((nextByte = objectContent.read()) != -1) {
            outValue.write(nextByte);
        }
        return outValue.toByteArray();
    }
    //
    //public ObjectListing getAllObjectsAuthoredByUser(String bucketName, )
}
