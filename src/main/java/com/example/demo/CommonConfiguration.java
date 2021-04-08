package com.example.demo;

import com.example.demo.clients.cms.AwsCmsService;
import com.example.demo.clients.cms.CmsService;
import com.example.demo.clients.cms.LocalCmsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CommonConfiguration {
    @Bean
    @ConditionalOnProperty(value = "local.cms.path")
    public CmsService localCmsService() {
        System.out.println("ZZZ LOCAL");
        return new LocalCmsService();
    }
    @Bean
    @ConditionalOnMissingBean(CmsService.class)
    public CmsService awsCmsService() {
        System.out.println("ZZZ AWS");
        return new AwsCmsService();
    }
}
