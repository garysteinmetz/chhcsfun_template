package com.example.demo.clients.cms;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class LocalCmsService implements CmsService {
    @Value("${local.cms.path}") String localCmsPath;

    @PostConstruct
    public void init() {
        File baseDir = new File(localCmsPath);
        if (!baseDir.isDirectory()) {
            throw new IllegalStateException("'" + localCmsPath + "' isn't a directory");
        }
    }
    @Override
    public CmsContent getContent(String key) {
        CmsContent outValue = null;
        try {
            File baseDir = new File(localCmsPath);
            File targetFile = new File(baseDir, key);
            if (targetFile.isFile()) {
                return new CmsContent(
                        new FileInputStream(targetFile),
                        Files.probeContentType(targetFile.toPath()),
                        targetFile.length());
            }
        } catch (IOException ioe) {
        }
        return outValue;
    }
}
