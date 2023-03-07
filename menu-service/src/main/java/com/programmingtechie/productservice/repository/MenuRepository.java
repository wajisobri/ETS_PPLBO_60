package com.programmingtechie.productservice.repository;

import com.programmingtechie.productservice.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuRepository extends MongoRepository<Menu, String> {
}
