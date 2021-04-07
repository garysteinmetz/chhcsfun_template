package com.example.demo.clients.cms;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AwsCmsService implements CmsService {

    @Value("${tf.var.aws.s3.bucket.name.content}") String contentBucket;
    private AmazonS3 s3client;

    @PostConstruct
    public void init() {
        s3client = AmazonS3ClientBuilder.standard().build();
    }

    @Override
    public CmsContent getContent(String key) {
        try {
            S3Object fileFromBucket = s3client.getObject(contentBucket, "content" + key);
            ObjectMetadata objectMetadata = fileFromBucket.getObjectMetadata();
            return new CmsContent(
                    fileFromBucket.getObjectContent(),
                    objectMetadata.getContentType(),
                    objectMetadata.getContentLength());
        } catch (AmazonServiceException ase) {
            return null;
        }
    }
}
