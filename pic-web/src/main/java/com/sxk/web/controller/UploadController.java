package com.sxk.web.controller;

import com.sxk.vo.RetMsg;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

  @Value(value = "${pic.web.imageRootPath}")
  private String imageRootPath;

  @RequestMapping(value = "/upload/image", method = RequestMethod.POST)
  @ResponseBody
  public RetMsg<String> uploadImage(HttpServletRequest request, MultipartFile uploadImage) {
    RetMsg<String> httpResponse = new RetMsg<>();
    if (null != uploadImage) {
      File uploadImageDir = new File(imageRootPath);
      if (!uploadImageDir.exists()) {
        boolean success = uploadImageDir.mkdir();
        if (success) {
          System.out.println("文件夹创建成功");
        } else {
          System.out.println("文件夹创建失败");
        }
      }
      String newPath = imageRootPath + System.currentTimeMillis() + ".jpg";
      if (!uploadImage.isEmpty()) {
        try (BufferedOutputStream bs = new
            BufferedOutputStream(new FileOutputStream(new File(newPath)));) {
          byte[] bytes = uploadImage.getBytes();
          bs.write(bytes);
        } catch (Exception e) {

        }
      }
    }
    httpResponse.setCode(0);
    httpResponse.setData("");
    httpResponse.setMsg("图片上传失败");
    return httpResponse;
  }

}
