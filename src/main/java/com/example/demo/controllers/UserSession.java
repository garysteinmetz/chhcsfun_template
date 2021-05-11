package com.example.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Consts;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

public class UserSession implements Serializable {
    //
    private String oauthToken;
    //
    private UserSession(Reader reader) {
        //
        try {
            oauthToken = convertReaderToUtfEightString(reader);
            //System.out.println("ZZZ oauthToken - " + oauthToken);
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }
        //
        //
        /*
        ObjectMapper om = new ObjectMapper();
        //id_token
        //access_token
        //refresh_token
        //expires_in
        //token_type
        Map<?, ?> map = om.readValue(inputStreamReader, Map.class);
        String idToken = map.get("id_token") + "";
        String[] idParts = idToken.split("\\.");
        String idPayloadBase64 = idParts[1];
        String payload = new String(Base64.getDecoder().decode(idPayloadBase64), Consts.UTF_8);
        sb.append(payload);
        //
        //at_hash
        //sub
        //aud
        //email_verified
        //token_use
        //auth_time
        //iss
        //cognito:username
        //exp
        //iat
        //email
        //
        //String header = new String(base64Url.decode(base64EncodedHeader));
        //int nextChar;
        //while ((nextChar = inputStreamReader.read()) != -1) {
        //    char nextOne = ((char)nextChar);
        //    sb.append(nextOne);
        //}
        */
    }
    private String getOauthToken() {
        return oauthToken;
    }
    public String getUserId() {
        return getUserCharacteristic("sub");
    }
    public String getUsername() {
        return getUserCharacteristic("cognito:username");
    }
    public String getName() {
        return getUserCharacteristic("name");
    }
    public String getEmail() {
        return getUserCharacteristic("email");
    }
    private String getUserCharacteristic(String characteristic) {
        try {
            String outValue;
            ObjectMapper om = new ObjectMapper();
            //id_token
            //access_token
            //refresh_token
            //expires_in
            //token_type
            Map<?, ?> map = om.readValue(oauthToken, Map.class);
            String idToken = map.get("id_token") + "";
            //System.out.println("ZZZ idToken - " + idToken);
            String[] idParts = idToken.split("\\.");
            String idPayloadBase64 = idParts[1];
            String payload = new String(Base64.getDecoder().decode(idPayloadBase64), Consts.UTF_8);
            //System.out.println("ZZZ payload = " + payload);
            Map<?, ?> payloadMap = om.readValue(payload, Map.class);
            outValue = ((String) payloadMap.get(characteristic));
            //System.out.println("ZZZ sub = " + outValue);
            return outValue;
        } catch (JsonProcessingException jpe) {
            throw new IllegalStateException(jpe);
        }
    }
    private static String convertReaderToUtfEightString(Reader reader) throws IOException {
        StringBuilder outValue = new StringBuilder();
        int nextInt;
        while ((nextInt = reader.read()) != -1) {
            char nextChar = ((char)nextInt);
            outValue.append(nextChar);
        }
        return outValue.toString();
    }
    public static void createSession(Reader reader, HttpSession httpSession) {
        UserSession userSession = new UserSession(reader);
        httpSession.setAttribute("userSession", userSession);
    }
    public static Optional<UserSession> getSession(HttpSession httpSession) {
        Optional<UserSession> outValue;
        if (httpSession != null) {
            Object o = httpSession.getAttribute("userSession");
            if (o != null) {
                outValue = Optional.of((UserSession) o);
            } else {
                outValue = Optional.empty();
            }
        } else {
            outValue = Optional.empty();
        }
        return outValue;
    }
    //public static boolean isUserMemberOfGroup(CognitoService cognitoService, HttpSession httpSession) {
    //    boolean outValue = false;
    //    Optional<UserSession> userSessionOpt = getSession(httpSession);
    //    if (userSessionOpt.isPresent()) {
    //        //
    //    }
    //    return outValue;
    //}
}
