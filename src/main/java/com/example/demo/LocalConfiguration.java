package com.example.demo;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConditionalOnProperty(
        value="tf.var.aws.enabled",
        havingValue = "false",
        matchIfMissing = true)
@PropertySource("classpath:local.properties")
public class LocalConfiguration {
    @Bean(name="sampleValueOne")
    public String getSampleValueOne() {
        return "localConfiguration";
    }
}
