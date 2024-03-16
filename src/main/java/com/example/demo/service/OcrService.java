package com.example.demo.service;

import com.example.demo.common.Result;
import com.example.demo.pojo.Template;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OcrService {
    void uploadTemplates(List<Template> templates);

    void uploadSoftwareNames(List<String> names);

    /**
     * 根据图片内容，识别是否有模板中的所有关键字均出现在了同一个视觉行中。
     *
     * @param imageFile
     * @return  如果有，则返回模板名称。
     * 如果没有，则返回fail信息
     * @throws IOException
     * @throws InterruptedException
     */
//    Result processImage(MultipartFile imageFile) throws IOException, InterruptedException;

    /**
     * 根据传入的图片，和ocrService中保存的软件类型本地变量software_names
     * 来识别哪个软件出现在了屏幕中
     * @return 如果有则返回软件名称，如果没有，则返回""
     * @throws IOException
     * @throws InterruptedException
     */
    public List<String> identifyMatchingTemplatesByFilePath(String filePath)throws IOException, InterruptedException;

    /**
     * 根据传入的图片，进行模板的匹配。如果找到了模板的名称，那么认定该图片是对应的模板。
     *
     * @return 集合的第一位就是模板的名称，剩下的是出现了关键字的段落
     * @throws IOException
     * @throws InterruptedException
     */
    public String identifySoftwareNamesByFilePath(String filePath)throws IOException, InterruptedException;

    public String saveImageFile(MultipartFile imageFile);
}
