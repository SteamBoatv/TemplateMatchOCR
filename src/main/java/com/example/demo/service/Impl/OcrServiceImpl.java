package com.example.demo.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import com.example.demo.Utils.TemplateMatcher;
import com.example.demo.common.Result;
import com.example.demo.common.ResultCodeEnum;
import com.example.demo.exceptions.TemplateNotSetException;
import com.example.demo.pojo.Template;
import com.example.demo.service.OcrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OcrServiceImpl implements OcrService {
    @Value("${MultipartConfig.imageLocation}")
    private String uploadDir;
    @Value("${MultipartConfig.pythonInter}")
    private String pythonInter;
    @Value("${MultipartConfig.pythonCode}")
    private String pythonCode;


    private List<Template> local_templates;

    private List<String> software_names;

    public void uploadTemplates(List<Template> templates) {
        local_templates = templates;
        log.debug("本次传入的模板为:{}",local_templates.stream().map(Template::toString).collect(Collectors.joining(", ")));
    }

    public void uploadSoftwareNames(List<String> names) {
        software_names = names;
        log.debug("本次传入的软件为:{}", String.join(", ", software_names));
    }

//    public List<String> identifyMatchingTemplates(MultipartFile imageFile) throws IOException, InterruptedException {
//        String pythonResult = runOCRAndGetResult(imageFile);
//        List<String> lines = TemplateMatcher.extractParagraphs(pythonResult, local_templates);
//        if(CollUtil.isEmpty(lines)){
//            log.debug("所有模板均未匹配");
//            log.debug("-----本次匹配结束-----");
//
//        }
//        return lines;
//    }

    public List<String> identifyMatchingTemplatesByFilePath(String filePath) throws IOException, InterruptedException {
        String pythonResult = runOcr(filePath);
        List<String> lines = TemplateMatcher.extractParagraphs(pythonResult, local_templates);
        if(CollUtil.isEmpty(lines)){
            log.debug("所有模板均未匹配");
            log.debug("-----本次匹配结束-----");

        }
        return lines;
    }
//    public String identifySoftwareNames(MultipartFile imageFile)throws IOException, InterruptedException{
//
//        String pythonResult = runOCRAndGetResult(imageFile);
//        List<String> result = findKeywordsInText(pythonResult, software_names);
//
//        //clearTemplates(); 去除 删除模板功能
//
//        if (CollUtil.isEmpty(result)) {
//            log.debug("所有模板均未匹配");
//            log.debug("-----本次匹配结束-----");
//            return "";
//        }
//        log.debug("-----本次匹配结束-----");
//        return result.get(0);
//    }

    public String identifySoftwareNamesByFilePath(String filePath)throws IOException, InterruptedException{

        String pythonResult = runOcr(filePath);
        //将所有的软件名全部去和识别出来的text文档匹配，如果成功，则返回第一个识别到的软件名称
        List<String> result = findKeywordsInText(pythonResult, software_names);

        //clearTemplates(); 去除 删除模板功能

        if (CollUtil.isEmpty(result)) {
            log.debug("所有模板均未匹配");
            log.debug("-----本次匹配结束-----");
            return "";
        }
        log.debug("-----本次匹配结束-----");
        return result.get(0);
    }

    private String runOCRAndGetResult(MultipartFile imageFile) throws IOException, InterruptedException {
        log.debug("-----本次匹配开始-----");
        if (CollUtil.isEmpty(local_templates) || CollUtil.isEmpty(software_names)) {
            throw new TemplateNotSetException("没有模板");
        }
        String filePath = saveImageFile(imageFile);
        return runOcr(filePath);
    }

    public String saveImageFile(MultipartFile imageFile) {
        String filePath = uploadDir +imageFile.getOriginalFilename() ;
        log.debug("图片临时保存地址={}",filePath);
        try {
            File file = new File(filePath);
            imageFile.transferTo(file);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String runOcr(String filePath) throws IOException, InterruptedException {
        String[] args = new String[]{pythonInter, pythonCode, filePath};
        log.debug("python启动参数={}", Arrays.toString(args));
        Process proc = Runtime.getRuntime().exec(args);
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        StringBuilder pythonResult = new StringBuilder();
        while ((line = in.readLine()) != null) {
            pythonResult.append(line);
        }
        in.close();
        proc.waitFor();
        log.debug("pythonResult = " + pythonResult.toString());
        return pythonResult.toString();
    }



    private void clearTemplates() {
        CollUtil.clear(local_templates);
        log.debug("模板已删除");
    }

    /**
     *
     * @param text 需要识别的文本内容，使用@符号分割
     * @param keywords 待识别的一些关键字集合
     * @return 出现在该文本中的关键字(可能为多个)
     */
    private List<String> findKeywordsInText(String text, List<String> keywords) {
        return keywords.stream().filter(text::contains).collect(Collectors.toList());
    }


    //    public Result processImage(MultipartFile imageFile) throws IOException, InterruptedException {
//        String pythonResult = runOCRAndGetResult(imageFile);
//        String templateMatcherResult = TemplateMatcher.match(local_templates, pythonResult);
//
//        //clearTemplates(); 去除 删除模板功能
//
//        if (templateMatcherResult == null) {
//            log.debug("所有模板均未匹配");
//            log.debug("-----本次匹配结束-----");
//            return Result.build(null, ResultCodeEnum.NOT_MATCH);
//        }
//
//        ImageMatchResult result = new ImageMatchResult(templateMatcherResult, LocalDateTime.now());
//        log.debug("-----本次匹配结束-----");
//        return Result.ok(result);
//    }
}
