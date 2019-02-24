package com.sxk.service;

import com.sxk.constants.CategoryEnum;
import com.sxk.dao.PictureRepository;
import com.sxk.entity.Picture;
import com.sxk.thymeleaf.NoWebLinkBuilder;
import com.sxk.util.HttpUtils;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class PictureServiceImplTest {

  static String imgUrl = "http://pic.netbian.com";

  @Autowired
  private PictureService pictureService;
  @Autowired
  private PictureRepository dao;

  @Before
  public void before() {
    for (CategoryEnum category : CategoryEnum.values()) {
      String imgPath = "D:/www/img/" + category.getCode();
      String htmlListPath = "D:/www/pic/" + category.getCode();
      File imgDir = new File(imgPath);
      if (!imgDir.exists()) {
        imgDir.mkdirs();
      }
      File listDir = new File(htmlListPath);
      if (!listDir.exists()) {
        listDir.mkdirs();
      }
    }
  }

  @Test
  public void listAll() {
    Page<Picture> lists = dao.findAll(PageRequest.of(1, 10));
    System.out.println(lists);
  }

  @Test
  public void getById() {

  }

  @Test
  public void saveOrUpdate() {
    Picture picture = new Picture();
    picture.setName("幻想艺术 树风景4k壁纸");
    picture.setPicUrl("http://pic.netbian.com/uploads/allimg/180717/101607-15317937671548.jpg");
    picture.setVipUrl("http://pic.netbian.com/uploads/allimg/180717/101607-15317937671548.jpg");
    picture.setDescription("幻想艺术 树风景4k壁纸");
    pictureService.saveOrUpdate(picture);
    System.out.println(picture);
  }

  @Test
  public void updateAll() throws Exception {
    for (int i = 0; i < 10000; i++) {
      Picture pic = pictureService.getById(i);
      if (pic != null && pic.getPicUrl() != null && pic.getPicUrl().indexOf("/") < 0) {
        pic.setPicUrl(pic.getCategory() + "/" + pic.getPicUrl());
        pictureService.saveOrUpdate(pic);
        log.info("picInfo:{}", pic);
      }
    }
  }


  @Test
  public void delete() {
  }

  @Test
  public void pullAllPic() {
    String rootUrl = "http://pic.netbian.com/4kqiche/index_%d.html";
    int category = CategoryEnum.CAR.getCode();
    List<Picture> pictureList = new ArrayList<>();
    Date now = new Date();
    for (int i = 1; i < 50; i++) {
      String result = HttpUtils.get(String.format(rootUrl, i), null, null, "GBK", 30000);
      if (result == null) {
        continue;
      }
      try {
        Document doc = Jsoup.parse(result);
        Elements bodyList = doc.select(".slist").select(".clearfix");
        Elements imgList = bodyList.select("img[src$=.jpg]");
        Elements bList = bodyList.select("b");
        for (int j = 0; j < imgList.size(); j++) {
          Element img = imgList.get(j);
          Element b = bList.get(j);
          Picture picture = new Picture();
          picture.setName(b.text());
          picture.setCategory(category);
          picture.setNetUrl(imgUrl + img.attr("src"));
          picture.setDescription(img.attr("alt"));
          picture.setCreateTime(now);
          picture.setUpdateTime(now);
          picture.setStatus(0);
          pictureList.add(picture);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println(pictureList);
      pictureService.saveAll(pictureList);
      pictureList.clear();
    }
  }

  @Test
  public void createStaticHtml() throws Exception {

    //构造模板引擎
    ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
    resolver.setPrefix("templates/pic/");//模板所在目录，相对于当前classloader的classpath。
    resolver.setSuffix(".html");//模板文件后缀
    resolver.setTemplateMode(TemplateMode.HTML);
    TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(resolver);
    templateEngine.setLinkBuilder(new NoWebLinkBuilder());

    //构造上下文(Model)
    Context context = new Context();
    for (CategoryEnum category : CategoryEnum.values()) {
      for (int i = 1; i < 100; i++) {
        PageRequest pageRequest = PageRequest.of(i, 9, Sort.by(Order.desc("createTime")));
        Page<Picture> result = pictureService.list(category.getCode(), pageRequest);
        context.setVariable("page", result);
        context.setVariable("category", category.getCode());
        context.setVariable("categoryInfo", category);
        context.setVariable("categories", CategoryEnum.values());
        //渲染模板
        FileWriter write = new FileWriter("D:/www/pic/" + category.getCode() + "/" + i + ".html");
        templateEngine.process("list", context, write);
      }
    }

  }


  @Test
  public void createPicInfoHtml() throws Exception {

    //构造模板引擎
    ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
    resolver.setPrefix("templates/pic/");//模板所在目录，相对于当前classloader的classpath。
    resolver.setSuffix(".html");//模板文件后缀
    resolver.setTemplateMode(TemplateMode.HTML);
    TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(resolver);
    templateEngine.setLinkBuilder(new NoWebLinkBuilder());

    //构造上下文(Model)
    Context context = new Context();
    //WebContext context = new WebContext();
    for (int i = 0; i < 10; i++) {
      Picture pic = pictureService.getById(i);
      if (pic == null || pic.getCategory() == null) {
        continue;
      }
      if (StringUtils.isEmpty(pic.getNetUrl())) {
        continue;
      }
      context.setVariable("pic", pic);
      context.setVariable("categoryInfo", CategoryEnum.of(pic.getCategory()));
      //渲染模板
      FileWriter write = new FileWriter("D:/www/info/" + i + ".html");
      templateEngine.process("info", context, write);
    }

  }

  @Test
  public void downNetImage() throws Exception {
    String rootPath;
    String newFilePath;
    String newImageName;
    for (int i = 5500; i < 8500; i++) {
      Picture pic = pictureService.getById(i);
      if (pic == null || pic.getCategory() == null) {
        continue;
      }
      if (StringUtils.isEmpty(pic.getNetUrl())) {
        continue;
      }
      if (!StringUtils.isEmpty(pic.getPicUrl())) {
        continue;
      }
      rootPath = "D:/www/img/" + pic.getCategory();
      newImageName = System.currentTimeMillis() + ".jpg";
      newFilePath = rootPath + "/" + newImageName;
      File tempFile = HttpUtils.download(pic.getNetUrl());
      if (tempFile == null) {
        continue;
      }
      FileCopyUtils.copy(tempFile, new File(newFilePath));
      tempFile.delete();
      pic.setPicUrl(newImageName);
      dao.save(pic);
    }

  }
}