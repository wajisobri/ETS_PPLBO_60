package com.programming.wajisobri.menuservice.repository;

import com.programming.wajisobri.menuservice.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuRepository extends MongoRepository<Menu, String> {
}
