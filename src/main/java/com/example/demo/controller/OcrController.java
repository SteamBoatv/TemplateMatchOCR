package com.example.demo.controller;

import cn.hutool.core.collection.CollUtil;
import com.example.demo.Utils.TemplateMatcher;
import com.example.demo.common.Result;
import com.example.demo.common.ResultCodeEnum;
import com.example.demo.pojo.ImageMatchResult;
import com.example.demo.pojo.Template;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/ocr/")
@RestController
@Slf4j
public class OcrController {

    @Value("${MultipartConfig.imageLocation}")
    private String uploadDir;
    @Value("${MultipartConfig.pythonInter}")
    private String pythonInter;
    @Value("${MultipartConfig.pythonCode}")
    private String pythonCode;

    private List<Template> local_templates;

    @PostMapping("/template")
    @ResponseBody
    public Result uploadTemplates(@RequestBody List<Template> templates) throws ServletException, IOException {
        local_templates = templates;
        log.debug("本次传入的模板为:{}",local_templates.stream().map(Template::toString).collect(Collectors.joining(", ")));
        return Result.ok(null);
    }

    @PostMapping("/img")
    @ResponseBody
    public Result uploadImg(@RequestParam("image") MultipartFile imageFile) throws IOException, InterruptedException {
        log.debug("-----本次匹配开始-----");
        if(CollUtil.isEmpty(local_templates)){
            log.debug("模板不存在，停止检测");
            return Result.fail("没有找到模板，请先传输模板");
        }
        log.debug("模板存在，继续检测");
        //获取文件的保存路径
        String filePath = uploadDir + imageFile.getOriginalFilename();
        //将文件保存到指定位置
        try {
            File file = new File(filePath);
            log.debug("图片临时保存地址={}",filePath);
            imageFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //获取OCR启动参数
        String[] args = new String[]{pythonInter, pythonCode,filePath};
        log.debug("python启动参数={}",Arrays.toString(args));
        //调用ocr识别
        Process proc = Runtime.getRuntime().exec(args);
        //获取结果
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = null;
        StringBuilder pythonResult = new StringBuilder();
        while ((line = in.readLine()) != null) {
            pythonResult.append(line);
        }
        in.close();
        proc.waitFor();
        log.debug("pythonResult = " + pythonResult.toString());
        String templateMatcherResult = TemplateMatcher.match(local_templates, pythonResult.toString());
        //无论成功与否，清空本次传输的模板参数列表
        log.debug("模板已删除");
        CollUtil.clear(local_templates);
        if(templateMatcherResult == null){
            log.debug("所有模板均未匹配");
            log.debug("-----本次匹配结束-----");
            return Result.build(null, ResultCodeEnum.NOT_MATCH);
        }
        ImageMatchResult result = new ImageMatchResult(templateMatcherResult,LocalDateTime.now());
        log.debug("-----本次匹配结束-----");
        return Result.ok(result);
    }

}
