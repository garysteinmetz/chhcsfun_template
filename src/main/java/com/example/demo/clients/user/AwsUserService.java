package com.example.demo.clients.user;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

public class AwsUserService implements UserService {
    @Value("${tf.var.aws.dynamodb.table.name.userAppData}") String tableNameUserAppData;

    AmazonDynamoDB dynamoDBClient;
    //
    //private static final String SEPERATOR = "|";
    //
    private static final String HASH_KEY = "app_id";
    private static final String SORT_KEY = "user_id";
    private static final String APP_DATA_FIELD = "app_data";
    private static final String LAST_MODIFIED_FIELD = "last_modified";

    @PostConstruct
    public void init() {
        dynamoDBClient = AmazonDynamoDBClientBuilder.standard().build();
    }
    public void saveToUserAppDataTable(String appName, String username, String appData) {
        DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
        Table userAppData = dynamoDB.getTable(tableNameUserAppData);
        Item item = new Item();
        item.withPrimaryKey(HASH_KEY, appName, SORT_KEY, username);
        item.withString(APP_DATA_FIELD, appData);
        item.withLong(LAST_MODIFIED_FIELD, System.currentTimeMillis());
        userAppData.putItem(item);
    }
    public UserData retrieveFromUserAppDataTable(String appName, String username) {
        UserData outValue = new UserData();
        DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
        Table userAppData = dynamoDB.getTable(tableNameUserAppData);
        PrimaryKey primaryKey = new PrimaryKey(HASH_KEY, appName, SORT_KEY, username);
        Item item = userAppData.getItem(primaryKey);
        if (item != null) {
            outValue.isPresent = true;
            outValue.lastModified = item.getLong(LAST_MODIFIED_FIELD);
            outValue.appData = item.getString(APP_DATA_FIELD);
        } else {
            outValue.isPresent = false;
        }
        return outValue;
    }
}
