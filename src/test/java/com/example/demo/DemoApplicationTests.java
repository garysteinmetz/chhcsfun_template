package com.example.demo;

import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.graalvm.polyglot.Context;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class DemoApplicationTests {
    @Test
    public void abc() throws ScriptException {
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
        final String[] JS_CODE = new String[]{
                "(function(param){",
                "  console.log('hello '+param);",
                "  return {'ab': 123, 'def': '456', 'ghi': function() {}};",
                "})"
        };
        //String JS_CODE = "(function(param){console.log('hello '+param);})";
        //
        //
        Context context = Context.create();
        Value input1 = context.eval("js", "123");
        Value function1 = context.eval("js", assembleMultiLine(JS_CODE));
        Value result1 = function1.execute(input1);
        //System.out.println("ZZZ result1 - " + result1);
        //
        //
        final String[] SIMPLIFY_JSON = new String[]{
                "(function(param){",
                "  return JSON.parse(JSON.stringify(param));",
                "})"
        };
        Value function2 = context.eval("js", assembleMultiLine(SIMPLIFY_JSON));
        Value result2 = function2.execute(result1);
        //System.out.println("ZZZ result2 - " + result2);
        //
        final String[] STRIP_STRINGS = new String[]{
                "(function stripStrings(param, allowedStrings) {",
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
        Value array1 = context.eval("js", "['def']");
        Value result3 = function3.execute(result2, array1);
        //System.out.println("ZZZ result3 - " + result3);
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

//@SpringBootTest
//class DemoApplicationTests {
//
//	@Test
//	void contextLoads() {
//	}
//
//}
