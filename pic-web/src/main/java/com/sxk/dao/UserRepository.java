package com.sxk.dao;

import com.sxk.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 *
 */
public interface UserRepository extends CrudRepository<User, Integer> {

}
