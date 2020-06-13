package com.example.demo.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class DynamoDBService {
    @Value("${aws.user.access.key}") String accessKey;
    @Value("${aws.user.secret.key}") String secretKey;
    @Value("${aws.region}") String awsRegion;
    @Value("${aws.dynamodb.table.name.userAppData}") String tableNameUserAppData;

    AmazonDynamoDB dynamoDBClient;
    //
    private static final String HASH_KEY = "author_id";
    private static final String SORT_KEY = "user_app_id";
    private static final String APP_DATA_FIELD = "app_data";
    private static final String LAST_MODIFIED_FIELD = "last_modified";

    @PostConstruct
    public void init() {
        //
        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );
        //
        dynamoDBClient = AmazonDynamoDBClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.fromName(awsRegion))
                .build();
    }
    public void saveToUserAppDataTable(String author, String appName, String username, String appData) {
        DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
        Table userAppData = dynamoDB.getTable(tableNameUserAppData);
        Item item = new Item();
        item.withPrimaryKey(HASH_KEY, constructHashKeyValue(author),
                SORT_KEY, constructSortKeyValue(appName, username));
        item.withString(APP_DATA_FIELD, appData);
        item.withLong(LAST_MODIFIED_FIELD, System.currentTimeMillis());
        userAppData.putItem(item);
    }
    public Map<String, Object> retrieveFromUserAppDataTable(String author, String appName, String username) {
        Map<String, Object> outValue = new HashMap<>();
        DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
        Table userAppData = dynamoDB.getTable(tableNameUserAppData);
        PrimaryKey primaryKey = new PrimaryKey(HASH_KEY, constructHashKeyValue(author),
                SORT_KEY, constructSortKeyValue(appName, username));
        Item item = userAppData.getItem(primaryKey);
        if (item != null) {
            //
            outValue.put("isPresent", true);
            outValue.put("lastModified", item.getLong(LAST_MODIFIED_FIELD));
            outValue.put("appData", item.getString(APP_DATA_FIELD));
        } else {
            outValue.put("isPresent", false);
        }
        //Item item = new Item();
        //item.withPrimaryKey(HASH_KEY, constructHashKeyValue(author),
        //        SORT_KEY, constructSortKeyValue(appName, username));
        //item.withString(APP_DATA_FIELD, appData);
        //item.withLong(LAST_MODIFIED_FIELD, System.currentTimeMillis());
        //userAppData.putItem(item);
        return outValue;
    }
    private String constructHashKeyValue(String author) {
        String outValue;
        if (author != null && author.trim().length() > 0) {
            outValue = author.trim().toLowerCase();
        } else {
            throw new IllegalStateException("author - '" + author);
        }
        return outValue;
    }
    private String constructSortKeyValue(String appName, String username) {
        String outValue;
        if (appName != null && appName.trim().length() > 0 && username != null && username.trim().length() > 0) {
            outValue = appName.trim().toLowerCase() + "|" + username.trim().toLowerCase();
        } else {
            throw new IllegalStateException("appname - '" + appName + "', username - '" + username + "'");
        }
        return outValue;
    }
    //public String getEntryValueAsString() {
    //    DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
    //    Table sample1 = dynamoDB.getTable("Sample1");
    //    PrimaryKey primaryKey = new PrimaryKey(new KeyAttribute("uuid", "abc"));
    //    Item item = sample1.getItem(primaryKey);
    //    return item.getString("value");
    //}
}
