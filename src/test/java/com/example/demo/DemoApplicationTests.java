package com.example.demo;

import com.example.demo.Utils.TemplateMatcher;
import com.example.demo.pojo.Template;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class DemoApplicationTests {

    @Test
    void contextLoads() {
        String text = "北创科技北创联合编目系统@代码变量名称翻译@编目管理英国管理列表@O团面心国学sl学lisen适输入查询内容Q@老进图书典藏正题备案号名ISBN副题名丛编题名编著者分类号@";
        List<Template> templates = new ArrayList<>();
        Template template1 = new Template("代码变量名称翻译", Arrays.asList("sql", "备案号"));
        Template template2 = new Template("Params", Arrays.asList("value2", "Response"));
        templates.add(template1);
        templates.add(template2);
        List<String> strings = TemplateMatcher.extractParagraphs(text, templates);
        strings.stream().forEach(System.out::println);
    }

    void testMatcher(){
//        TemplateMatcher.extractParagraphs("O@O@马北京邮电大学2023级专业学位硕士研究生培...×x@白固日「<[65 ]/144 〉l白Q自适应宽v□导航栏、视图因 书签A标记、L画笔v橡皮擦O批注设置☆Q口回@1世小E公丁LOco以マ业丁出以WIU工中个力不@网络与信息安全（085412）领域@电子信息专业学位硕士培养方案@学分总学分≥37学分@学@类别分组情况课程编号课程名称学时@分@3121101213工程伦理（MOOC）322@第1组,选4门(公共必修3181101768网络空间安全学科论文写作指导161@课)自然辩证法概论161@3311100704@3321101666新时代中国特色社会主义理论与实践32@2@3121101520研究生英语2@ 32@第2组，至少选1门(英语@3311101694研究生英语国际学术交流322@必修课。英语免修学生在@3311101696研究生英语科技读译与科技传播322@制定培养计划时，请选择@开究生龙语”@3311101699研究生英语跨文化职场交流322@",)
    }
}
