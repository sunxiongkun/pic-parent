package com.sxk.web.controller;

import com.sxk.entity.User;
import com.sxk.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by jt on 1/10/17.
 */
@Controller
public class UserController {

  @Autowired
  private UserService userService;


  @RequestMapping("/user")
  public String redirectToList() {
    return "redirect:/user/list";
  }

  @RequestMapping({"/user/list", "/user"})
  public String listProducts(Model model) {
    model.addAttribute("users", userService.listAll());
    return "user/list";
  }

  @RequestMapping("/user/show/{id}")
  public String getProduct(@PathVariable String id, Model model) {
    model.addAttribute("user", userService.getById(Integer.parseInt(id)));
    return "user/show";
  }

  @RequestMapping("user/edit/{id}")
  public String edit(@PathVariable String id, Model model) {
    User user = userService.getById(Integer.parseInt(id));
    model.addAttribute("user", user);
    return "user/productform";
  }

  @RequestMapping("/user/new")
  public String newProduct(Model model) {
    model.addAttribute("user", new User());
    return "user/productform";
  }

  @RequestMapping(value = "/user", method = RequestMethod.POST)
  public String saveOrUpdateProduct(@Valid User productForm, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return "user/productform";
    }

    User savedProduct = userService.saveOrUpdateUserForm(productForm);

    return "redirect:/user/show/" + savedProduct.getId();
  }

  @RequestMapping("/user/delete/{id}")
  public String delete(@PathVariable String id) {
    userService.delete(Integer.parseInt(id));
    return "redirect:/user/list";
  }
}
