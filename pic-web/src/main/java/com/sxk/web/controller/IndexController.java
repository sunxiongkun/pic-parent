package com.sxk.web.controller;

import com.sxk.constants.CategoryEnum;
import com.sxk.entity.Picture;
import com.sxk.service.PictureService;
import com.sxk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

  @Autowired
  private UserService userService;
  @Autowired
  private PictureService pictureService;

  @RequestMapping(value = {"/pic/page.html", "/"}, method = {RequestMethod.GET, RequestMethod.POST})
  public String index(Model model) {
    return page(0, model);
  }

  @GetMapping("/pic/page/{number}.html")
  public String page(@PathVariable("number") Integer number, Model model) {
    return page(null, number, model);
  }

  @GetMapping("/pic/page/{category}/{number}.html")
  public String page(@PathVariable("category") Integer category,
      @PathVariable("number") Integer number, Model model) {
    PageRequest pageRequest = PageRequest.of(number, 9, Sort.by(Order.desc("createTime")));
    if (category != null) {
      model.addAttribute("categoryInfo", CategoryEnum.of(category));
    }
    model.addAttribute("page", pictureService.list(category, pageRequest));
    model.addAttribute("category", category);
    model.addAttribute("categories", CategoryEnum.values());
    return "pic/list";
  }

  @GetMapping("/pic/info/{id}.html")
  public String index(@PathVariable("id") Integer id, Model model) {
    Picture pic = pictureService.getById(id);
    model.addAttribute("pic", pic);
    if (pic != null) {
      model.addAttribute("categoryInfo", CategoryEnum.of(pic.getCategory()));
    }
    return "pic/info";
  }
}
