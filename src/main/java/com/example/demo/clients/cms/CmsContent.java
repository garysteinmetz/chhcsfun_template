package com.example.demo.clients.cms;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;

import java.io.InputStream;

public class CmsContent {
    private InputStream content;
    private String mimeType;
    private long length;
    protected CmsContent(InputStream content, String mimeType, long length) {
        this.content = content;
        this.mimeType = mimeType;
        this.length = length;
    }
    public InputStream getContent() {
        return content;
    }
    public String getMimeType() {
        return mimeType;
    }
    public long getLength() {
        return length;
    }
    public MediaType getMediaType() {
        MediaType outValue = MediaType.APPLICATION_OCTET_STREAM;
        try {
            //
            outValue = MediaType.parseMediaType(mimeType);
        } catch (InvalidMediaTypeException imte) {
            //
        }
        return outValue;
    }
}
