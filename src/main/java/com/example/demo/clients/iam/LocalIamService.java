package com.example.demo.clients.iam;

import com.example.demo.controllers.UserSession;
import org.apache.http.Consts;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.StringReader;
import java.util.Base64;

public class LocalIamService implements IamService {

    @Value("${local.iam.user}")
    String localIamUser;

    @Override
    public void logout() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String processLoginRequest(String url, HttpSession httpSession) {
        StringReader sr = new StringReader(constructFauxOauthToken());
        UserSession.createSession(sr, httpSession);
        StringBuilder sb = new StringBuilder("redirect:");
        sb.append(url);
        return sb.toString();
    }

    @Override
    public String processOauthTwoCallback(String code, String redirectPath,
                                          HttpServletRequest request, HttpServletResponse httpResponse,
                                          HttpSession httpSession) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserSession getUserSession() {
        throw new UnsupportedOperationException();
    }

    private String constructFauxOauthToken() {
        StringBuilder outValue = new StringBuilder();
        outValue.append("{\"id_token\":\"");
        outValue.append("firstPart.");
        outValue.append(new String(Base64.getEncoder().encode(localIamUser.getBytes(Consts.UTF_8))));
        outValue.append(".thirdPart");
        outValue.append("\"}");
        return outValue.toString();
    }
}
