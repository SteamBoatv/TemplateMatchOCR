package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.common.ImageMatchResult;
import com.example.demo.pojo.Template;
import com.example.demo.service.OcrService;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @Autowired
    OcrService ocrService;

    @PostMapping("/upload/template")
    @ResponseBody
    public Result uploadTemplates(@RequestBody List<Template> templates) throws ServletException, IOException {
        ocrService.uploadTemplates(templates);
        return Result.ok();
    }

    @PostMapping("/upload/softwareNames")
    @ResponseBody
    public Result uploadSoftwareNames(@RequestParam List<String> names) throws ServletException, IOException {
        ocrService.uploadSoftwareNames(names);
        return Result.ok();
    }

    @PostMapping("/img")
    @ResponseBody
    public Result OCRImg(@RequestParam("image") MultipartFile imageFile) throws IOException, InterruptedException {
        String filePaTth = ocrService.saveImageFile(imageFile);
        String s = ocrService.identifySoftwareNamesByFilePath(filePaTth);
        List<String> strings = ocrService.identifyMatchingTemplatesByFilePath(filePaTth);
        ImageMatchResult imageMatchResult = new ImageMatchResult(strings.get(0),s, LocalDateTime.now(),strings.subList(1,strings.size()));
        return Result.ok(imageMatchResult);
    }

    @PostMapping("/placeHolder")
    @ResponseBody
    public Result placeHolder(@RequestParam("image") MultipartFile imageFile) throws IOException, InterruptedException {
        return ocrService.placeHolderByLine(imageFile);
    }


}
