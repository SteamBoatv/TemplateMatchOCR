package com.example.demo.Utils;

import com.example.demo.pojo.Template;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TemplateMatcher {

    /**
     * 对传入的所有集合进行遍历，遍历要求：出现了模板中的name关键字就算匹配模板成功
     * 随后将该匹配到的模板名称先加入到集合中，随后，只要是text中出现了模板关键字，就返回整段内容至result中
     * @param text
     * @param templates
     * @return
     */
    public static List<String> extractParagraphs(String text, List<Template> templates) {
        List<String> result = new ArrayList<>();

        for (Template template : templates) {
            if (text.contains(template.getName())) {
                //"name":"网络与信息安全"
                result.add(template.getName());
                for (String key : template.getKeys()) {
                    String[] paragraphs = text.split("@", -1); // 使用负数作为第二个参数，以保留结尾空字符串
                    for (String paragraph : paragraphs) {
                        if (paragraph.contains(key)) {
                            result.add(paragraph);
                        }
                    }
                }
                // 找到匹配的模板后直接跳出循环
                break;
            }
        }

        return result;
    }

//    public static String match(List<Template> templateList, String target) {
//        int start = -1;
//        int end = -1;
//
//        while (true) {
//            start = target.indexOf('@', end + 1);
//            if (start == -1) {
//                break;
//            }
//            end = target.indexOf('@', start + 1);
//            if (end == -1) {
//                break;
//            }
//
//            String segment = target.substring(start + 1, end);
//            for (Template template : templateList) {
//                if (isMatchedTemplate(segment, template)) {
//                    return template.getName();
//                }
//            }
//        }
//        return null;
//    }
//
//    private static boolean isMatchedTemplate(String segment, Template template) {
//        for (String key : template.getKeys()) {
//            if (!segment.contains(key)) {
//                return false;
//            }
//        }
//        return true;
//    }
}
