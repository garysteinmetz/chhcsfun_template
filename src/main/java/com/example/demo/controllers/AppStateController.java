package com.example.demo.controllers;

//import com.example.demo.services.DynamoDBService;
import com.example.demo.clients.user.UserData;
import com.example.demo.clients.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

@Controller
public class AppStateController {
    //@Autowired
    //DynamoDBService dynamoDBService;
    @Autowired
    UserService userService;
    @ResponseBody
    @PostMapping("/appState/{appName}")
    public ResponseEntity saveAppState(
            @PathVariable String appName, @RequestParam("appData") String data, HttpSession httpSession) {
        //
        System.out.println("ZZZ Received - " + appName);
        System.out.println("ZZZ Received - " + data);
        Optional<UserSession> userSession = UserSession.getSession(httpSession);
        if (userSession.isPresent()) {
            userService.saveToUserAppDataTable(appName, userSession.get().getUsername(), data);
        } else {
            throw new IllegalStateException("User isn't logged in");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.add("Content-Type", "application/json");
        return new ResponseEntity<>("{}", headers, HttpStatus.OK);
        //return "{}";
    }
    @ResponseBody
    @GetMapping("/appState/{appName}")
    public ResponseEntity getAppStateForUser(@PathVariable String appName, HttpSession httpSession) {
        //
        UserData outValue;
        Optional<UserSession> userSession = UserSession.getSession(httpSession);
        if (userSession.isPresent()) {
            outValue = userService.retrieveFromUserAppDataTable(
                    appName, userSession.get().getUsername());
        } else {
            throw new IllegalStateException("User isn't logged in");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(outValue, headers, HttpStatus.OK);
    }
    //
    public String stripUnsupportedStrings(String jsCode, String appData) {
        String outValue;
        //https://www.graalvm.org/reference-manual/js/

        //final ScriptEngineManager manager = new ScriptEngineManager();
        //manager.get
        //final ScriptEngine engine = manager.getEngineByName("js");
        //
        //engine.eval("var outValue = 1 + 1;");
        //Object outValue = engine.get("outValue");
        //Object outValue = engine.eval("return (1 + 1);");
        //for (int i = 0; i < 10; i++) {
        //    System.out.println("ZZZ i - " + outValue);
        //}
        //String JS_CODE = "(function myFun(param){var FileClass = Java.type(\"java.io.File\"); console.log('hello '+FileClass);})";
        //final String[] JS_CODE = new String[]{
        //        "(function(param){",
        //        "  console.log('hello '+param);",
        //        "  return {'ab': 123, 'def': '456', 'ghi': function() {}};",
        //        "})"
        //};
        //String JS_CODE = "(function(param){console.log('hello '+param);})";
        //
        //appData = "{\"abc\": 123}";
        //
        Context context = Context.create();
        System.out.println("ZZZ A");
        //System.out.println("ZZZ AB - " + context.asValue(appData));
        //Value input1 = context.parse("js", appData);
        Value input1 = context.asValue(appData);//context.parse("js", appData);
        System.out.println("ZZZ B");
        System.out.println("ZZZ input1 - " + input1);
        System.out.println("ZZZ jsCode = " + jsCode);
        Value function1 = context.eval("js", "(" + jsCode + ")");
        Value result1 = function1.execute(input1);
        //System.out.println("ZZZ result1 - " + result1);
        //
        //
        final String[] SIMPLIFY_JSON = new String[]{
                "(function(param){",
                //"  return JSON.parse(JSON.stringify(param));",
                "  return JSON.parse(param);",
                "})"
        };
        Value function2 = context.eval("js", assembleMultiLine(SIMPLIFY_JSON));
        System.out.println("ZZZ result1 - " + result1);
        //System.out.println("ZZZ result1 - " + result1.);
        //System.out.println("ZZZ result1 - " + result1.asString());
        Value result2 = function2.execute(result1);
        System.out.println("ZZZ result2 - " + result2);
        //
        final String[] STRIP_STRINGS = new String[]{
                "(function stripStrings(param, allowedStrings) {",
                "console.log('param - ' + param);",
                "console.log('type param - ' + (typeof param));",
                "  if ((typeof param) === 'string') {",
                "    if (param.length <= 2 || allowedStrings.indexOf(param) != -1) {",
                "      return param;",
                "    } else {",
                "      return undefined;",
                "    }",
                "  } else if (Array.isArray(param)) {",
                "    for (var i = 0; i < param.length; i++) {",
                "      param[i] = stripStrings(param[i], allowedStrings);",
                "    }",
                "    return param;",
                "  } else if ((typeof param) === 'object') {",
                "    var keys = Object.keys(param);",
                "    for (var i = 0; i < keys.length; i++) {",
                "      if (stripStrings(keys[i], allowedStrings)) {",
                "        param[keys[i]] = stripStrings(param[keys[i]], allowedStrings);",
                "      } else {",
                "        delete param[keys[i]];",
                "      }",
                "    }",
                "    return param;",
                "  } else {",
                "    return param;",
                "  }",
                "})"
        };
        Value function3 = context.eval("js", assembleMultiLine(STRIP_STRINGS));
        Value array1 = context.eval("js", "['userData']");
        Value result3 = function3.execute(result2, array1);
        outValue = result3.toString();
        System.out.println("ZZZ result3 - " + result3);
        Value function4 = context.eval("js", "(function(o){return JSON.stringify(o)})");
        Value result4 = function4.execute(result3);
        //System.out.println("ZZZ result4 - " + result4);
        outValue = result4.as(String.class);
        System.out.println("ZZZ result4 - " + outValue);
        return outValue;
    }
    private String assembleMultiLine(String... lines) {
        StringWriter outValue = new StringWriter();
        PrintWriter pw = new PrintWriter(outValue);
        for (String nextLine : lines) {
            pw.println(nextLine);
        }
        return outValue.toString();
    }
}
