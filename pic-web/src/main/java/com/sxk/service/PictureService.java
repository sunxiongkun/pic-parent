package com.sxk.service;

import com.github.pagehelper.PageInfo;
import com.sxk.dao.PictureRepository;
import com.sxk.entity.Picture;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PictureService {

  @Autowired
  private PictureRepository dao;

  public List<Picture> listAll() {
    List<Picture> list = new ArrayList<>();
    dao.findAll().forEach(list::add);
    return list;
  }

  public Page<Picture> list(Integer category, PageRequest pageRequest) {
    Page<Picture> page;
    if (category == null) {
      page = dao.findAll(pageRequest);
    } else {
      page = dao.findByCategory(category, pageRequest);
    }
    PageInfo pageInfo = new PageInfo(page.getContent());
    pageInfo.getList();
    System.out.println(pageInfo);
    return page;
  }


  public Picture getById(Integer id) {
    return dao.findById(id).get();
  }


  public Picture saveOrUpdate(Picture picture) {
    return dao.save(picture);
  }


  public void saveAll(List<Picture> pictures) {
    dao.saveAll(pictures);
  }


  public void delete(Integer id) {

  }
}
