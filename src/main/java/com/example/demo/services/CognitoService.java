package com.example.demo.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClientBuilder;
import com.amazonaws.services.cognitoidentity.model.*;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;
//import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
//import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
//import com.amazonaws.services.identitymanagement.model.ListUsersRequest;
//import com.amazonaws.services.identitymanagement.model.ListUsersResult;
//import com.amazonaws.services.identitymanagement.model.User;
import com.example.demo.controllers.UserSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class CognitoService {
    @Value("${aws.user.access.key}") String accessKey;
    @Value("${aws.user.secret.key}") String secretKey;
    @Value("${aws.region}") String awsRegion;
    @Value("${aws.cognito.user.pool.id}") String userPoolId;

    AWSCognitoIdentityProvider cognitoIdentityProvider;
    AmazonCognitoIdentity cognitoIdentity;
    //AmazonIdentityManagement amazonIdentityManagement;

    @PostConstruct
    public void init() {
        //
        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );
        //
        cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.fromName(awsRegion))
                .build();
        cognitoIdentity = AmazonCognitoIdentityClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.fromName(awsRegion))
                .build();
    }
    public boolean isUserInGroup(UserSession userSession, String groupName) throws JsonProcessingException {
        boolean outValue = false;
        AdminListGroupsForUserRequest adminListGroupsForUserRequest = new AdminListGroupsForUserRequest();
        adminListGroupsForUserRequest.setUserPoolId(userPoolId);
        adminListGroupsForUserRequest.setUsername(userSession.getUserId());
        adminListGroupsForUserRequest.setLimit(50);
        AdminListGroupsForUserResult adminListGroupsForUserResult =
                cognitoIdentityProvider.adminListGroupsForUser(adminListGroupsForUserRequest);
        for (GroupType groupType : adminListGroupsForUserResult.getGroups()) {
            outValue |= groupType.getGroupName().equals(groupName);
        }
        return outValue;
    }
    public List<String> getCognitoUsersInGroup(String groupName) {
        List<String> outValue = new ArrayList<>();
        ListUsersInGroupRequest listUsersInGroupRequest = new ListUsersInGroupRequest();
        listUsersInGroupRequest.setGroupName(groupName);
        listUsersInGroupRequest.setUserPoolId(userPoolId);
        listUsersInGroupRequest.setLimit(50);
        ListUsersInGroupResult listUsersInGroupResult =
                cognitoIdentityProvider.listUsersInGroup(listUsersInGroupRequest);
        for (UserType userType : listUsersInGroupResult.getUsers()) {
            outValue.add(userType.getUsername());
            System.out.println("ZZZ group(" + groupName + ") - " + userType.getUsername());
        }
        return outValue;
    }
    //https://gorillalogic.com/blog/java-integration-with-amazon-cognito/
    public String getCognitoUserNames() {
        String outValue = "For User - ";
        DescribeUserPoolRequest describeUserPoolRequest = new DescribeUserPoolRequest();
        describeUserPoolRequest.setUserPoolId(userPoolId);
        DescribeUserPoolResult describeUserPoolResult = cognitoIdentityProvider.describeUserPool(describeUserPoolRequest);
        outValue = outValue + describeUserPoolResult.getUserPool().getName();
        //GetUserRequest request = new GetUserRequest();
        //GetUserResult user = cognitoIdentityProvider.getUser()
        AdminGetUserRequest adminGetUserRequest = new AdminGetUserRequest();
        adminGetUserRequest.setUserPoolId(userPoolId);
        adminGetUserRequest.setUsername("someUserName");
        AdminGetUserResult adminGetUserResult = cognitoIdentityProvider.adminGetUser(adminGetUserRequest);
        for (AttributeType at : adminGetUserResult.getUserAttributes()) {
            outValue = outValue + at.getName() + " = " + at.getValue() + ", ";
        }
        //
        AdminListGroupsForUserRequest adminListGroupsForUserRequest = new AdminListGroupsForUserRequest();
        adminListGroupsForUserRequest.setUserPoolId(userPoolId);
        adminListGroupsForUserRequest.setUsername("someUserName");
        AdminListGroupsForUserResult adminListGroupsForUserResult = cognitoIdentityProvider.adminListGroupsForUser(adminListGroupsForUserRequest);
        for (GroupType groupType : adminListGroupsForUserResult.getGroups()) {
            outValue = outValue + ", UserBelongsToGroup = " + groupType.getGroupName();
        }
        return outValue;
    }
}
