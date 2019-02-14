package com.sxk.dao;

import com.sxk.entity.Picture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 */
public interface PictureRepository extends JpaRepository<Picture, Integer> {

  Page<Picture> findByCategory(Integer category, Pageable pageable);

}
