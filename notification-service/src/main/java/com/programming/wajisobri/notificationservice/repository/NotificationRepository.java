package com.programming.wajisobri.notificationservice.repository;

import com.programming.wajisobri.notificationservice.model.NotificationEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<NotificationEvent, String> {
    List<NotificationEvent> findByUsernameRecipient(String usernameRecipient);
}
