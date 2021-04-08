package com.example.demo.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class DynamoDBService {
    @Value("${tf.var.aws.dynamodb.table.name.userAppData}") String tableNameUserAppData;

    AmazonDynamoDB dynamoDBClient;
    //
    private static final String SEPERATOR = "|";
    //
    private static final String HASH_KEY = "author_id";
    private static final String SORT_KEY = "user_app_id";
    private static final String APP_DATA_FIELD = "app_data";
    private static final String LAST_MODIFIED_FIELD = "last_modified";

    @PostConstruct
    public void init() {
        //
        //AWSCredentials credentials = new BasicAWSCredentials(
        //        accessKey,
        //        secretKey
        //);
        //
        dynamoDBClient = AmazonDynamoDBClientBuilder
                .standard()
                //.withCredentials(new AWSStaticCredentialsProvider(credentials))
                //.withRegion(Regions.fromName(awsRegion))
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
    public Set<String> getAllAppNamesByAuthor(String authorId) {
        Set<String> outValue = new TreeSet<>();
        DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
        Table userAppData = dynamoDB.getTable(tableNameUserAppData);
        QuerySpec querySpec = new QuerySpec();
        querySpec.withKeyConditionExpression("author_id = :authorId");
        ValueMap params = new ValueMap();
        params.withString(":authorId", authorId.trim().toLowerCase());
        querySpec.withValueMap(params);
        querySpec.withProjectionExpression("user_app_id");
        ItemCollection<QueryOutcome> queryOutcome = userAppData.query(querySpec);
        Iterator<Item> iter = queryOutcome.iterator();
        while (iter.hasNext()) {
            Item nextItem = iter.next();
            String userAppId = nextItem.getString("user_app_id");
            String app = getAppFromUserAppId(userAppId);
            if (app != null) {
                outValue.add(app);
            }
        }
        return outValue;
    }
    public ObjectNode retrieveAppDataForAllUsersAsJsonObject(String authorId, String app) {
        ObjectNode outValue = JsonNodeFactory.instance.objectNode();
        ObjectNode userDataNode = outValue.with("userData");
        DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
        Table userAppData = dynamoDB.getTable(tableNameUserAppData);
        QuerySpec querySpec = new QuerySpec();
        querySpec.withKeyConditionExpression("author_id = :authorId and begins_with(user_app_id, :userAppIdPrefix)");
        ValueMap params = new ValueMap();
        params.withString(":authorId", authorId.trim().toLowerCase());
        params.withString(":userAppIdPrefix", app.trim().toLowerCase() + SEPERATOR);
        querySpec.withValueMap(params);
        querySpec.withProjectionExpression("user_app_id, app_data, last_modified");
        ItemCollection<QueryOutcome> queryOutcome = userAppData.query(querySpec);
        Iterator<Item> iter = queryOutcome.iterator();
        while (iter.hasNext()) {
            Item nextItem = iter.next();
            String userAppId = nextItem.getString("user_app_id");
            long lastModified = nextItem.getLong("last_modified");
            String appData = nextItem.getString("app_data");
            //
            String username = getUsernameFromUserAppId(userAppId);
            if (username != null) {
                //
                try {
                    ObjectMapper objectMapper = new ObjectMapper();

                    JsonNode appDataWithoutStrings = objectMapper.readTree(appData);
                    appDataWithoutStrings = objectMapper.readTree(appDataWithoutStrings.asText());
                    ObjectNode usernameNode = userDataNode.with(username);
                    usernameNode.put("last_modified", lastModified);
                    usernameNode.set("app_data", appDataWithoutStrings);
                } catch (JsonProcessingException e) {
                    System.out.println("ZZZ error processing for username - " + username);
                    e.printStackTrace();
                }
            } else {
                System.out.println("ZZZ user_app_id doesn't contain valid username");
            }
        }
        return outValue;
    }
    private static String getUsernameFromUserAppId(String userAppId) {
        String outValue = null;
        if (userAppId != null) {
            //
            int separatorIndex = userAppId.indexOf(SEPERATOR);
            if (separatorIndex != -1) {
                String candidateValue = userAppId.substring(separatorIndex + SEPERATOR.length());
                if (!candidateValue.isEmpty()) {
                    outValue = candidateValue;
                }
            }
        }
        return outValue;
    }
    private static String getAppFromUserAppId(String userAppId) {
        String outValue = null;
        if (userAppId != null) {
            //
            int separatorIndex = userAppId.indexOf(SEPERATOR);
            if (separatorIndex != -1) {
                String candidateValue = userAppId.substring(0, separatorIndex);
                if (!candidateValue.isEmpty()) {
                    outValue = candidateValue;
                }
            }
        }
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
            outValue = appName.trim().toLowerCase() + SEPERATOR + username.trim().toLowerCase();
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
    //
    //
    //
    //
    //
}
