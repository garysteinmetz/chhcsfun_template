package com.example.demo.clients.iam;

import com.example.demo.controllers.UserSession;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AwsIamService implements IamService {

    @Value("${tf.var.aws.cognito.oauth2.authorize.url}")
    String awsCognitoLoginUrl;

    @Value("${tf.var.aws.cognito.client.id}")
    String awsCognitoClientId;

    @Value("${tf.var.aws.cognito.client.secret}")
    String awsCognitoClientSecret;

    @Value("${tf.var.aws.cognito.oauth2.token.url}")
    String awsCognitoOauth2TokenUrl;

    @Override
    public void logout() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String processLoginRequest(String url, HttpSession httpSession) {
        //
        try {
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
            //System.out.println("ZZZ LoginUrl - " + sb);
            //
            //System.out.println("ZZZ Going to URL - " + sb);
            return sb.toString();
        } catch (URISyntaxException use) {
            throw new IllegalStateException(use);
        }
    }

    @Override
    public String processOauthTwoCallback(String code, String redirectPath,
                                          HttpServletRequest request, HttpServletResponse httpResponse,
                                          HttpSession httpSession) {
        //
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(awsCognitoOauth2TokenUrl);
            //
            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
                    awsCognitoClientId, awsCognitoClientSecret);
            httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            //
            //String requestUrl = ensureHttps(request.getRequestURL().toString());
            String requestUrl = request.getRequestURL().toString();
            //System.out.println("ZZZ request.getRequestURL().toString() - " + requestUrl);
            List<NameValuePair> form = new ArrayList<>();
            form.add(new BasicNameValuePair("grant_type", "authorization_code"));
            form.add(new BasicNameValuePair("redirect_uri", requestUrl));
            form.add(new BasicNameValuePair("code", code));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
            //System.out.println("ZZZ Sending HTTP POST to " + awsCognitoOauth2TokenUrl
            //    + " with code " + code + " which should then redirect to URL " + requestUrl);
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
                //System.out.println("ZZZ userId - " + userId);
                //System.out.println("ZZZ username - " + username);
                //System.out.println("ZZZ name - " + name);
                //System.out.println("ZZZ email - " + email);
                //
                //Cookie userDisplayNameCookie = new Cookie("userDisplayName", name);
                //httpResponse.addCookie(userDisplayNameCookie);
            }
            //return new ModelAndView("redirect:" + redirectPath);
            return "redirect:" + redirectPath;
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        } catch (AuthenticationException ae) {
            throw new IllegalStateException(ae);
        }
    }

    @Override
    public UserSession getUserSession() {
        throw new UnsupportedOperationException();
    }
}
