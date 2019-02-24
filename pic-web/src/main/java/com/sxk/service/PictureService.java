package com.sxk.service;

import com.sxk.dao.PictureRepository;
import com.sxk.entity.Picture;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    return page;
  }

  public Page<Picture> listByQuality(Integer quality, PageRequest pageRequest) {
    return dao.findByQuality(quality, pageRequest);
  }

  public Picture getById(Integer id) {
    Optional<Picture> optional = dao.findById(id);
    return optional.isPresent() ? optional.get() : null;
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
