package com.example.demo.Utils;

import com.example.demo.pojo.Template;

import java.util.List;

public class TemplateMatcher {
    public static String match(List<Template> templateList, String target) {
        int start = -1;
        int end = -1;

        while (true) {
            start = target.indexOf('@', end + 1);
            if (start == -1) {
                break;
            }
            end = target.indexOf('@', start + 1);
            if (end == -1) {
                break;
            }

            String segment = target.substring(start + 1, end);
            for (Template template : templateList) {
                if (isMatchedTemplate(segment, template)) {
                    return template.getName();
                }
            }
        }
        return null;
    }

    private static boolean isMatchedTemplate(String segment, Template template) {
        for (String key : template.getKeys()) {
            if (!segment.contains(key)) {
                return false;
            }
        }
        return true;
    }
}
