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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
public class LoginController {

    @Value("${tf.var.aws.cognito.client.id}")
    String awsCognitoClientId;

    @Value("${tf.var.aws.cognito.client.secret}")
    String awsCognitoClientSecret;

    @Value("${tf.var.aws.cognito.oauth2.token.url}")
    String awsCognitoOauth2TokenUrl;

    @Value("${tf.var.aws.cognito.oauth2.authorize.url}")
    String awsCognitoLoginUrl;

    @ResponseBody
    @GetMapping(value = "/userInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> userInfo(HttpSession httpSession) throws Exception {
        Map<String, Object> outValue = new HashMap<>();
        outValue.put("isLoggedIn", false);
        Optional<UserSession> session = UserSession.getSession(httpSession);
        if (session.isPresent()) {
            outValue.put("isLoggedIn", true);
            outValue.put("displayName", session.get().getName());
        }
        return outValue;
    }
    @ResponseBody
    @GetMapping("/logout")
    public String logout(HttpSession httpSession) throws Exception {
        httpSession.invalidate();
        return "You have logged out, refresh any application pages";
    }
    //https://docs.aws.amazon.com/cognito/latest/developerguide/authorization-endpoint.html
    @GetMapping("/login")
    public String login(@RequestParam(name="url", required=true) String url) throws Exception {
        //
        URI callbackUrl = URI.create(url);
        URI oauthTwoCallbackUrl = new URI(
                callbackUrl.getScheme(), null, callbackUrl.getHost(), callbackUrl.getPort(),
                "/oauthTwoCallback", null, null);
        //
        StringBuilder sb = new StringBuilder("redirect:");
        sb.append(awsCognitoLoginUrl);
        //sb.append(oauthTwoCallbackUrl.toString());
        sb.append("&redirect_uri=");
        sb.append(URLEncoder.encode(oauthTwoCallbackUrl.toString(), StandardCharsets.UTF_8));
        sb.append("&state=");
        sb.append(URLEncoder.encode(callbackUrl.toString(), StandardCharsets.UTF_8));
        System.out.println("ZZZ LoginUrl - " + sb);
        //
        return sb.toString();
    }
    //https://docs.aws.amazon.com/cognito/latest/developerguide/token-endpoint.html
    @GetMapping("/oauthTwoCallback")
    public ModelAndView oauthTwoCallback(
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
        //String requestUrl = ensureHttps(request.getRequestURL().toString());
        String requestUrl = request.getRequestURL().toString();
        System.out.println("ZZZ request.getRequestURL().toString() - " + requestUrl);
        List<NameValuePair> form = new ArrayList<>();
        form.add(new BasicNameValuePair("grant_type", "authorization_code"));
        form.add(new BasicNameValuePair("redirect_uri", requestUrl));
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
            String username = UserSession.getSession(httpSession).get().getUsername();
            String name = UserSession.getSession(httpSession).get().getName();
            String email = UserSession.getSession(httpSession).get().getEmail();
            System.out.println("ZZZ userId - " + userId);
            System.out.println("ZZZ username - " + username);
            System.out.println("ZZZ name - " + name);
            System.out.println("ZZZ email - " + email);
            //
            //Cookie userDisplayNameCookie = new Cookie("userDisplayName", name);
            //httpResponse.addCookie(userDisplayNameCookie);
        }
        return new ModelAndView("redirect:" + redirectPath);
    }
    /*
    private static String ensureHttps(String value) {
        if (value.indexOf("localhost") != -1) {
            return value;
        } else {
            int colonIndex = value.indexOf(':');
            return "https" + value.substring(colonIndex);
        }
    }
    */
}
