package car.car2024.Utils.DataHandle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParser {
    //输入xml字符串返回hash
    public static Map<String, String> parseXML(String xmlData) {
        Map<String, String> map = new HashMap<>();

        Document document = Jsoup.parse(xmlData, "", Parser.xmlParser());
        Element rootElement = document.selectFirst("message");

        if (rootElement != null) {
            for (Element element : rootElement.children()) {
                String tagName = element.tagName();
                String value = element.text();
                map.put(tagName, value);
            }
        }

        return map;
    }

    //                Map<String, Object> test2 = DataParser.parseJSON("{\n" + "\"code\": \"2023\",\n" + "\"algorithm\": \"0123456789ABCDE \",\n" + "\"result\":\n" + "{\n" + " \"purpose\": \"赛课融通，综合育人\"\n" + "},\n" + "\"number\": 1\n" + "}\n");
//                String purposeValue = DataParser.findPurposeValue(test2, "purpose");
//                String codeValue = DataParser.findPurposeValue(twest2, "code");
//                String algorithmValue = DataParser.findPurposeValue(test2, "algorithm");
//                String numberValue = DataParser.findPurposeValue(test2, "number");
//                String text = "科技强国";
//                Double f = Double.valueOf(numberValue);
//                int a = (int) Math.ceil(f) - 1;
//                char Character = text.charAt(a);
//                OcrIdentify.content = String.valueOf(Character);
//                String expression = "A+B*C";
//                int var1 = 1;
//                int var2 = 2;
//                int var3 = 3;
//                String formattedExpression = InfixToPostfixEvaluator.formatExpression(expression, var1, var2, var3);
//
//                String postfix = InfixToPostfixEvaluator.convertToPostfix(formattedExpression);
//                System.out.println("后缀表达式: " + postfix);
//                int result = InfixToPostfixEvaluator.evaluatePostfix(postfix);
//                System.out.println("后缀表达式的计算结果: " + result);
//                try {
//                    byte[] test = {24};
//                    SendDataUtils.result1 =  "FFFFFFFFFFFF".getBytes("GBK");
//                    SendDataUtils.result2 =  test;
//                } catch (UnsupportedEncodingException e) {
//                    throw new RuntimeException(e);
//                }
    public static Map<String, Object> parseJSON(String jsonData) {
        Gson gson = new Gson();
        return gson.fromJson(jsonData, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    public static String findPurposeValue(Map<String, Object> map, String target) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                String result = findPurposeValue((Map<String, Object>) value, target);
                if (result != null) {
                    return result;
                }
            } else if (entry.getKey().equals(target)) {
                return value.toString();
            }
        }
        return null;
    }

    public static JSONObject convertToJSONObject(String inputString) {
        String pattern = "<(\\w+)>(.*?)<\\/\\w+>";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(inputString);

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            jsonBuilder.append("\"").append(key).append("\"").append(":").append("\"").append(value).append("\"").append(",");
        }
        jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        jsonBuilder.append("}");

        String jsonString = jsonBuilder.toString();

        try {
            return new JSONObject(jsonString);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
            return null; // 或者根据需要返回适当的默认值或错误处理
        }
    }
//使用案例
//    Map<String, String> test = DataParser.parseXML("<message>\n" +
//            "<other>test</other>\n" +
//            "<key>1111</key>\n" +
//            "</message>\n");
//
//    Map<String, Object> test2 = DataParser.parseJSON("{\n" +
//            "\"code\": \"2023\",\n" +
//            "\"algorithm\": \"0123456789ABCDE \",\n" +
//            "\"result\":\n" +
//            "{\n" +
//            " \"purpose\": \"赛课融通，综合育人\"\n" +
//            "},\n" +
//            "\"number\": 1\n" +
//            "}\n");
//
//    JSONObject one = DataParser.convertToJSONObject("<message>\n" +
//            "<other>test</other>\n" +
//            "<key>\n" +
//            "<test>test</test>\n" +
//            "</key>\n" +
//            "</message>\n");
//
//    Map<String, Object> test3 = DataParser.parseJSON(one.toString());
//
//    String purposeValue = DataParser.findPurposeValue(test2, "purpose");
//
//    if (purposeValue != null) {
//        System.out.println("purpose的值为：" + purposeValue);
//    } else {
//        System.out.println("未找到purpose字段或其值。");
//    }

}