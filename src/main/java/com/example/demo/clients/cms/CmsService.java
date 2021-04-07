package com.example.demo.clients.cms;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;

public interface CmsService {
    public CmsContent getContent(String key);
}
