package com.sxk.service;

import com.sxk.entity.Picture;
import com.sxk.util.HttpUtils;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupTest {

  static String rootUrl = "http://pic.yituyiyu.com/4kfengjing/index_%d.html";
  static String imgUrl = "http://pic.yituyiyu.com";

  public static void main(String[] args) {
    List<Picture> pictureList = new ArrayList<>();
    for (int i = 1; i < 3; i++) {
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
          picture.setNetUrl(imgUrl + img.attr("src"));
          picture.setDescription(img.attr("alt"));
          pictureList.add(picture);
          System.out.println(picture);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      //System.out.println(pictureList);
      pictureList.clear();
    }
  }
}
