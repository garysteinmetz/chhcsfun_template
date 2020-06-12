package com.example.demo.controllers;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {

    @Value("${aws.cognito.client.id}")
    String awsCognitoClientId;

    @Value("${aws.cognito.client.secret}")
    String awsCognitoClientSecret;

    @Value("${aws.cognito.oauth2.token.url}")
    String awsCognitoOauth2TokenUrl;

    @GetMapping("/login")
    public ModelAndView login(
            @RequestParam(name="code", required=true) String code,
            @RequestParam(name="state", required=false, defaultValue="/") String redirectPath,
            HttpServletRequest request, HttpServletResponse httpResponse, HttpSession httpSession) throws Exception {
        //
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(awsCognitoOauth2TokenUrl);
        //
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(awsCognitoClientId, awsCognitoClientSecret);
        httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        //
        List<NameValuePair> form = new ArrayList<>();
        form.add(new BasicNameValuePair("grant_type", "authorization_code"));
        form.add(new BasicNameValuePair("redirect_uri", request.getRequestURL().toString()));
        form.add(new BasicNameValuePair("code", code));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
        httpPost.setEntity(entity);
        //
        CloseableHttpResponse response = client.execute(httpPost);
        try (
                InputStream content = response.getEntity().getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(content, Consts.UTF_8);) {
            //
            UserSession.createSession(inputStreamReader, httpSession);
            String userId = UserSession.getSession(httpSession).get().getUserId();
            System.out.println("ZZZ userId - " + userId);
        }
        return new ModelAndView("redirect:" + redirectPath);
    }
}
