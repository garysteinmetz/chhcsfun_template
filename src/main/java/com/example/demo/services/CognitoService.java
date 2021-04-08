package com.example.demo.services;

import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClientBuilder;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;
import com.example.demo.controllers.UserSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class CognitoService {
    @Value("${tf.var.aws.cognito.user.pool.id}") String userPoolId;

    AWSCognitoIdentityProvider cognitoIdentityProvider;
    AmazonCognitoIdentity cognitoIdentity;

    @PostConstruct
    public void init() {
        //
        cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder.standard().build();
        cognitoIdentity = AmazonCognitoIdentityClientBuilder.standard().build();
    }
    public boolean isUserInGroup(UserSession userSession, String groupName) throws JsonProcessingException {
        boolean outValue = false;
        AdminListGroupsForUserRequest adminListGroupsForUserRequest = new AdminListGroupsForUserRequest();
        adminListGroupsForUserRequest.setUserPoolId(userPoolId);
        adminListGroupsForUserRequest.setUsername(userSession.getUsername());
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
