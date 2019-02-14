package com.sxk.service;

import com.sxk.dao.UserRepository;
import com.sxk.entity.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;


  public List<User> listAll() {
    List<User> users = new ArrayList<>();
    userRepository.findAll().forEach(users::add); //fun with Java 8
    return users;
  }


  public User getById(Integer id) {
    return userRepository.findById(id).get();
  }


  public User saveOrUpdate(User user) {
    userRepository.save(user);
    return user;
  }


  public void delete(Integer id) {
    userRepository.deleteById(id);

  }


  public User saveOrUpdateUserForm(User userForm) {
    User savedProduct = saveOrUpdate(userForm);

    System.out.println("Saved User Id: " + savedProduct.getId());
    return savedProduct;
  }
}
