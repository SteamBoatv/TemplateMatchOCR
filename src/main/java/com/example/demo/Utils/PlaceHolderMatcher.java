package com.example.demo.Utils;

import com.example.demo.pojo.Template;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PlaceHolderMatcher {
    private static final String FAIL_INFORMATION = "找到模板，但未匹配到模板中的占位符-[此内容非占位符信息]";

    /**
     * 根据text文档的内容，寻找哪一个模板符合此文档
     * @param text 传入的文档，默认使用@分割
     * @param templates 传入的模板集合，准备进行匹配
     * @return 如果匹配成功，则返回模板在集合中的下标；失败则返回-1
     */
    public static int FindTheSerialNumberOfTheTemplte(String text, List<Template> templates){
        int the_key_row_num = 0;

        for (Template template:templates){
            List<String> keys = template.getKeys();
            //System.out.println(keys);
            //不管有用没用 首先写一个函数，获得[%1]所在的行数，从0开始的
            int row = where_is_the_row(keys);
            //System.out.println(row); 这一步没问题。已经获得了[%1]相应所在的行数

            //维护一个List,存储所有需要匹配的关键字，包括[%1]前后的关键字
            List<String> kmp = new ArrayList<>();
            //其实计算所在行，完全可以和下面的循环处理到一起，但是没这样做是为了证明代码所到之处没问题，比较清晰
            for(int i = 0 ; i < keys.size() ; i++){
                if(row != i){
                    kmp.add(keys.get(i));
                }else{
                    int index = keys.get(i).indexOf("[%1]");
                    if(!keys.get(i).substring(0,index).equals("")){ //占位符左侧有关键字
                        kmp.add(keys.get(i).substring(0,index));
                    }
                    if(!keys.get(i).substring(index+4).equals("")){ //占位符右侧有关键字
                        kmp.add(keys.get(i).substring(index+4));
                    }
                }
            }
            /*
                经过验证 没问题
                System.out.println(kmp);
                [询, Cop, WPS, Sheet]
                [Taobao, 询, C, 支付宝]
                [la, JingDong, 白条, 询, Cop]
            */
            boolean allKeysFound = true;
            for(int i = 0; i < kmp.size() ; i++){
                if(!text.contains(kmp.get(i))){
                    allKeysFound = false;
                    break;
                }
            }
            if(allKeysFound){
                return the_key_row_num;
            }
            the_key_row_num++;
        }
        return -1;
    }



    //获得[%1]所在的行数
    public static int where_is_the_row(List<String> keys){
        for(int i = 0 ; i < keys.size() ; i++){
            //我这里默认了一定存在[%1] 但是返回-1就是相当于不存在[%1]
            int index = keys.get(i).indexOf("[%1]");
            if(index != -1){
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @param text 传入的文档内容，默认使用@符号分割
     * @param placeholderString 传入的待识别占位符字符串 如"ABC[%1]DEF"
     * @return 如果识别到了，则直接返回占位符所占的内容，否则返回约定好的错误信息
     */
    public static String MatchPlaceholderContent(String text, String placeholderString) {
        for (String s : text.split("@")) {
            if (fun2(s,placeholderString) != null)
                return fun2(s,placeholderString);
        }
        return "匹配失败";
    }

    public static String fun2(String text,String moban){
        ArrayList<String> arr = divide(moban);
        //System.out.println(arr);
        int left = kmp(text, arr.get(0)) + arr.get(0).length();
        int right = kmp(text, arr.get(arr.size()-1));


//        System.out.println("arr.getLast():" + arr.get(arr.size()-1));
//        System.out.println("right:" + right);

        if (left == arr.get(0).length() - 1 || right == -1)
            return null;

        StringBuilder sb = new StringBuilder();
        for (int i = left; i < right; i++) {
            if (text.charAt(i) == '@')
                return null;
            sb.append(text.charAt(i));
        }

        return sb.toString();

    }





    public static ArrayList<String> divide(String moban) {
        StringBuilder left = new StringBuilder();
        StringBuilder right = new StringBuilder();

        int i = 0;
        while (moban.charAt(i) != '[') {
            left.append(moban.charAt(i));
            i++;
        }

        while (moban.charAt(i - 1) != ']') i++;

        while (i < moban.length()) {
            right.append(moban.charAt(i));
            i++;
        }

        ArrayList<String> arr = new ArrayList<>();
        arr.add(left.toString());
        arr.add(right.toString());
        return arr;
    }


    //获取模式串的next数组
    public static int[] getNext(String s) {
        int[] next = new int[s.length()];

        int j = 0;
        next[0] = 0;
        for (int i = 1; i < s.length(); i++) {
            while (j != 0 && s.charAt(i) != s.charAt(j)) j = next[j - 1];
            if (s.charAt(i) == s.charAt(j)) j++;
            next[i] = j;
        }

        return next;
    }

    public static int kmp(String text, String s) {
        int[] next = getNext(s);

        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j != 0 && text.charAt(i) != s.charAt(j)) j = next[j - 1];
            if (text.charAt(i) == s.charAt(j)) {
                if (j == s.length() - 1)
                    return i - j;
                j++;
            }
        }

        return -1;
    }

}
