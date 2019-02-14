package com.sxk.web.controller;

import com.sxk.constants.CategoryEnum;
import com.sxk.service.PictureService;
import com.sxk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {

  @Autowired
  private UserService userService;
  @Autowired
  private PictureService pictureService;

  @GetMapping("/pic/page.html")
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
    PageRequest pageRequest = PageRequest.of(number, 9);
    if (category != null && category > 2) {
      category = 2;
    }
    model.addAttribute("page", pictureService.list(category, pageRequest));
    model.addAttribute("category", category);
    model.addAttribute("categories", CategoryEnum.values());
    return "pic/list";
  }
}
