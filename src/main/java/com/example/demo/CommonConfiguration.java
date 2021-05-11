package com.example.demo;

import com.example.demo.clients.cms.AwsCmsService;
import com.example.demo.clients.cms.CmsService;
import com.example.demo.clients.cms.LocalCmsService;
import com.example.demo.clients.iam.AwsIamService;
import com.example.demo.clients.iam.IamService;
import com.example.demo.clients.iam.LocalIamService;
import com.example.demo.clients.user.AwsUserService;
import com.example.demo.clients.user.LocalUserService;
import com.example.demo.clients.user.UserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CommonConfiguration {
    //
    @Bean
    @ConditionalOnProperty(value = "local.cms.path")
    public CmsService localCmsService() {
        //System.out.println("ZZZ LOCAL CmsService");
        return new LocalCmsService();
    }
    @Bean
    @ConditionalOnMissingBean(CmsService.class)
    public CmsService awsCmsService() {
        //System.out.println("ZZZ AWS CmsService");
        return new AwsCmsService();
    }
    //
    @Bean
    @ConditionalOnProperty(value = "local.iam.user")
    public IamService localIamService() {
        //System.out.println("ZZZ LOCAL IamService");
        return new LocalIamService();
    }
    @Bean
    @ConditionalOnMissingBean(IamService.class)
    public IamService awsIamService() {
        //System.out.println("ZZZ AWS IamService");
        return new AwsIamService();
    }
    //
    @Bean
    @ConditionalOnProperty(value = "local.user.data")
    public UserService localUserService() {
        //System.out.println("ZZZ LOCAL UserService");
        return new LocalUserService();
    }
    @Bean
    @ConditionalOnMissingBean(UserService.class)
    public UserService awsUserService() {
        //System.out.println("ZZZ AWS UserService");
        return new AwsUserService();
    }
}
