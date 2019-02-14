package com.sxk.service;

import com.sxk.constants.CategoryEnum;
import com.sxk.entity.Picture;
import com.sxk.util.HttpUtils;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PictureServiceImplTest {

  static String rootUrl = "http://pic.netbian.com/4kbeijing/index_%d.html";
  static String imgUrl = "http://pic.netbian.com";
  static int category = 2;

  @Autowired
  private PictureService pictureService;

  @Test
  public void listAll() {
    List<Picture> lists = pictureService.listAll();
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
  public void delete() {
  }

  @Test
  public void pullAllPic() {
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
    TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(resolver);

    //构造上下文(Model)
    Context context = new Context();
    //WebContext context = new WebContext();
    for (int i = 1; i < 3; i++) {
      PageRequest pageRequest = PageRequest.of(i, 9);
      Page<Picture> result = pictureService.list(CategoryEnum.SCENIC.getCode(), pageRequest);
      context.setVariable("page", result);
      context.setVariable("category", category);
      context.setVariable("categories", CategoryEnum.values());
      //渲染模板
      //FileWriter write = new FileWriter("/www/" + i + ".html");
      FileWriter write = new FileWriter("result.html");
      templateEngine.process("list", context, write);
    }


  }
}