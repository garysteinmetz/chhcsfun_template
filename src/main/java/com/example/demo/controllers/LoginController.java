package com.example.demo.controllers;

import com.example.demo.clients.iam.IamService;
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
    @Autowired
    IamService iamService;

    @ResponseBody
    @GetMapping(value = "/userInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> userInfo(HttpSession httpSession) {
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
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "You have logged out, refresh any application pages";
    }
    //https://docs.aws.amazon.com/cognito/latest/developerguide/authorization-endpoint.html
    @GetMapping("/login")
    public String login(@RequestParam(name="url", required=true) String url, HttpSession httpSession) {
        return iamService.processLoginRequest(url, httpSession);
    }
    //https://docs.aws.amazon.com/cognito/latest/developerguide/token-endpoint.html
    @GetMapping("/oauthTwoCallback")
    public String oauthTwoCallback(
            @RequestParam(name="code", required=true) String code,
            @RequestParam(name="state", required=false, defaultValue="/") String redirectPath,
            HttpServletRequest request, HttpServletResponse httpResponse, HttpSession httpSession) {
        //System.out.println("ZZZ Received OAuth2 Code - " + code);
        //System.out.println("ZZZ   State - " + redirectPath);
        return iamService.processOauthTwoCallback(code, redirectPath, request, httpResponse, httpSession);
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
