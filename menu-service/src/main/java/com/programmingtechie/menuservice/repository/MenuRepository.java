package com.programmingtechie.menuservice.repository;

import com.programmingtechie.menuservice.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuRepository extends MongoRepository<Menu, String> {
}
