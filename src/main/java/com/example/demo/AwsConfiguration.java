package com.example.demo;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

@Configuration
@ConditionalOnProperty(
        value="aws.enabled",
        havingValue = "true",
        matchIfMissing = false)
@PropertySource("classpath:aws.properties")
public class AwsConfiguration {
    @Bean(name="sampleValueOne")
    public String getSampleValueOne() {
        return "awsConfiguration2";
    }
    //Note - not including the following bean causes 'lettuce' connection factory to be used
    //which causes this problem on AWS
    //io.lettuce.core.RedisCommandExecutionException: ERR unknown command `CONFIG`, with args beginning with: `GET`,
    //`notify-keyspace-events`,
    //https://stackoverflow.com/questions/28116156/jedisdataexception-when-upgrading-spring-session-from-1-0-0-rc1-to-1-0-0-release
    //https://github.com/spring-projects/spring-session/issues/124
    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
}
