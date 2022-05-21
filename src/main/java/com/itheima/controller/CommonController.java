package com.itheima.controller;

import com.itheima.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.picPath}")
    private String basePath;

    //上传图片,post请求
    @PostMapping("/upload")
    public R<String> update(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        //图片后缀名
        originalFilename=originalFilename.substring(originalFilename.lastIndexOf("."));
        //UUID+.jpg为图片名，以防重复
        originalFilename= UUID.randomUUID()+originalFilename;
        //首先检查目录picPath是否存在
        File file1=new File(basePath);
        if(!file1.exists()){
            file1.mkdirs();
        }
        File file2=new File(basePath+originalFilename);
        //将图片转存至服务器指定位置
        try {
            file.transferTo(file2);
        } catch (IOException e) {
            e.printStackTrace();
            R.error("图片上传失败！");
        }
        //将保存的文件名返回给前端，保证上传文件后可回显
        return R.success(originalFilename);
    }

    //下载图片，即图片回显在浏览器中
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        FileInputStream inputStream=null;  //先将图片读入inputStream中
        ServletOutputStream writer = null; //再由writer输出至浏览器
        try {
            inputStream=new FileInputStream(basePath+name);
            writer = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len=0;
            byte[] bytes=new byte[1024];
            while((len=inputStream.read(bytes))!=-1){
                writer.write(bytes,0,len);
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
