package com.sxk.web.controller;

import com.sxk.constants.CategoryEnum;
import com.sxk.constants.QualityEnum;
import com.sxk.entity.Picture;
import com.sxk.service.PictureService;
import com.sxk.thymeleaf.NoWebLinkBuilder;
import com.sxk.vo.RetMsg;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Slf4j
@Controller
@RequestMapping(value = "/admin")
public class AdminController {

  @Autowired
  private PictureService pictureService;

  @Value(value = "${pic.web.pageRootPath}")
  private String pageRootPath;
  @Value(value = "${pic.web.infoRootPath}")
  private String infoRootPath;
  @Value(value = "${pic.web.imageRootPath}")
  private String imageRootPath;

  private TemplateEngine templateEngine;


  @PostConstruct
  private void init() {
    //构造模板引擎
    ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
    resolver.setPrefix("templates/pic/");//模板所在目录，相对于当前classloader的classpath。
    resolver.setSuffix(".html");//模板文件后缀
    resolver.setTemplateMode(TemplateMode.HTML);
    templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(resolver);
    templateEngine.setLinkBuilder(new NoWebLinkBuilder());

    for (CategoryEnum category : CategoryEnum.values()) {
      String imgPath = imageRootPath + category.getCode();
      String htmlListPath = pageRootPath + category.getCode();
      File imgDir = new File(imgPath);
      if (!imgDir.exists()) {
        imgDir.mkdirs();
      }
      File listDir = new File(htmlListPath);
      if (!listDir.exists()) {
        listDir.mkdirs();
      }
    }

    for (QualityEnum qualityEnum : QualityEnum.values()) {
      String qListPath = pageRootPath + "q/" + qualityEnum.getCode();
      File qListDir = new File(qListPath);
      if (!qListDir.exists()) {
        qListDir.mkdirs();
      }
    }
  }

  @GetMapping("/info/add.html")
  public String picUpdatePage(Picture pic, Model model) {
    if (pic == null || pic.getId() == null) {
      pic = new Picture();
    } else {
      pic = pictureService.getById(pic.getId());
    }
    model.addAttribute("pic", pic);
    model.addAttribute("categories", CategoryEnum.values());
    model.addAttribute("qualities", QualityEnum.values());
    if (pic.getCategory() != null) {
      model.addAttribute("categoryInfo", CategoryEnum.of(pic.getCategory()));
    }
    return "admin/info_update";
  }

  @PostMapping("/info/update")
  public String picUpdate(Picture pic, @RequestParam("photos") MultipartFile uploadImage,
      Model model) {
    log.debug("pic:{}", pic);
    String picUrl = upload(pic, uploadImage);
    log.info("picUrl:{}", picUrl);
    if (pic.getId() == null) {
      if (StringUtils.isEmpty(picUrl)) {
        return picUpdatePage(pic, model);
      }
      pic.setPicUrl(picUrl);
      pictureService.saveOrUpdate(pic);
      createPicInfo(pic.getId());
      return picUpdatePage(pic, model);
    }
    Picture oldPic = pictureService.getById(pic.getId());
    if (pic.getCategory() != null) {
      oldPic.setCategory(pic.getCategory());
    }
    if (pic.getQuality() != null) {
      oldPic.setCategory(pic.getQuality());
    }
    if (pic.getName() != null) {
      oldPic.setName(pic.getName());
    }
    if (!StringUtils.isEmpty(picUrl)) {
      oldPic.setPicUrl(picUrl);
    }

    if (pic.getKeyWord() != null) {
      oldPic.setKeyWord(pic.getKeyWord());
    }
    if (pic.getDescription() != null) {
      oldPic.setDescription(pic.getDescription());
    }
    if (pic.getNetUrl() != null) {
      oldPic.setNetUrl(pic.getNetUrl());
    }
    if (pic.getVipUrl() != null) {
      oldPic.setVipUrl(pic.getVipUrl());
    }
    if (pic.getResolution() != null) {
      oldPic.setResolution(pic.getResolution());
    }

    pictureService.saveOrUpdate(oldPic);
    createPicInfo(oldPic.getId());
    return picUpdatePage(oldPic, model);
  }

  private String upload(Picture pic, MultipartFile uploadImage) {
    if (null == uploadImage) {
      return null;
    }
    String newName = pic.getCategory() + "/" + System.currentTimeMillis() + ".jpg";
    String newPath = imageRootPath + newName;
    if (!uploadImage.isEmpty()) {
      try (BufferedOutputStream bs = new
          BufferedOutputStream(new FileOutputStream(new File(newPath)));) {
        byte[] bytes = uploadImage.getBytes();
        bs.write(bytes);
      } catch (Exception e) {
        log.error("upload error");
        return null;
      }
    }
    return newName;
  }


  @GetMapping("/page/create")
  @ResponseBody
  public RetMsg<String> picPageCreate(@RequestParam(defaultValue = "100") Integer end) {

    //构造上下文(Model)
    Context context = new Context();
    for (CategoryEnum category : CategoryEnum.values()) {
      for (int i = 1; i < end; i++) {
        PageRequest pageRequest = PageRequest.of(i, 9, Sort.by(Order.desc("createTime")));
        Page<Picture> result = pictureService.list(category.getCode(), pageRequest);
        if (result.getContent().isEmpty()) {
          break;
        }
        context.setVariable("page", result);
        context.setVariable("category", category.getCode());
        context.setVariable("categoryInfo", category);
        context.setVariable("categories", CategoryEnum.values());
        //渲染模板
        String realPath = pageRootPath + category.getCode() + "/" + i + ".html";
        try (FileWriter write = new FileWriter(realPath)) {
          templateEngine.process("list", context, write);
        } catch (Exception e) {
          log.error("create html error:{}", realPath);
        }
      }
    }
    return new RetMsg<String>(0, "", "success");
  }

  @GetMapping("/page/q/create")
  @ResponseBody
  public RetMsg<String> picQualityCreate(@RequestParam(defaultValue = "100") Integer end) {

    //构造上下文(Model)
    Context context = new Context();
    for (QualityEnum quality : QualityEnum.values()) {
      for (int i = 1; i < end; i++) {
        PageRequest pageRequest = PageRequest.of(i, 9, Sort.by(Order.desc("createTime")));
        Page<Picture> result = pictureService.listByQuality(quality.getCode(), pageRequest);
        if (result.getContent().isEmpty()) {
          break;
        }
        context.setVariable("page", result);
        context.setVariable("categories", CategoryEnum.values());
        context.setVariable("qualityInfo", quality);
        context.setVariable("quality", quality.getCode());
        context.setVariable("qualities", QualityEnum.values());
        //渲染模板
        String realPath = pageRootPath + quality.getCode() + "/" + i + ".html";
        try (FileWriter write = new FileWriter(realPath)) {
          templateEngine.process("list_quality", context, write);
        } catch (Exception e) {
          log.error("create html error:{}", realPath);
        }
      }
    }
    return new RetMsg<String>(0, "", "success");
  }

  @GetMapping("/vip/create")
  @ResponseBody
  public RetMsg<String> vipCreate() {
    //构造上下文(Model)
    Context context = new Context();
    context.setVariable("categories", CategoryEnum.values());
    String realPath = pageRootPath + "vip.html";
    try (FileWriter write = new FileWriter(realPath)) {
      templateEngine.process("list", context, write);
    } catch (Exception e) {
      log.error("create html error:{}", realPath);
    }
    return new RetMsg<String>(0, "", "success");
  }

  @GetMapping("/info/create")
  @ResponseBody
  public RetMsg<String> picInfoCreate(@RequestParam(defaultValue = "100") Integer end) {
    for (int i = 0; i < end; i++) {
      createPicInfo(i);
    }
    return new RetMsg<String>(0, "", "success");
  }

  private void createPicInfo(Integer id) {
    Picture pic = pictureService.getById(id);
    if (pic != null && pic.getCategory() != null && !StringUtils.isEmpty(pic.getPicUrl())) {
      Context context = new Context();
      context.setVariable("pic", pic);
      context.setVariable("categoryInfo", CategoryEnum.of(pic.getCategory()));
      //渲染模板
      String realPath = infoRootPath + id + ".html";
      try (FileWriter write = new FileWriter(realPath)) {
        templateEngine.process("info", context, write);
      } catch (Exception e) {
        log.error("create html error:{}", realPath);
      }
    }
  }

}
