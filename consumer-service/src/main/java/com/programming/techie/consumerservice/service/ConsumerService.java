package com.programming.techie.consumerservice.service;

import com.programming.techie.consumerservice.dto.ConsumerRequest;
import com.programming.techie.consumerservice.dto.ConsumerResponse;
import com.programming.techie.consumerservice.dto.ConsumersResponse;
import com.programming.techie.consumerservice.model.Consumer;
import com.programming.techie.consumerservice.repository.ConsumerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final ConsumerRepository consumerRepository;

    public ConsumerResponse createConsumer(ConsumerRequest consumerRequest) {
        try {
            Consumer addedConsumer = Consumer.builder()
                    .name(consumerRequest.getName())
                    .username(consumerRequest.getUsername())
                    .password(consumerRequest.getPassword())
                    .phoneNumber(consumerRequest.getPhoneNumber())
                    .build();

            addedConsumer = consumerRepository.save(addedConsumer);
            return ConsumerResponse.builder()
                    .code(200)
                    .message("Consumer created")
                    .data(addedConsumer)
                    .build();
        } catch (DataAccessException e) {
            return ConsumerResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return ConsumerResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }

    public ConsumersResponse getAllConsumer() {
        try {
            List<Consumer> consumers = consumerRepository.findAll();

            if (consumers.isEmpty()) {
                return ConsumersResponse.builder()
                        .code(404)
                        .message("No consumer found")
                        .build();
            }

            consumers = consumers.stream().map(this::mapToConsumerResponse).toList();
            return ConsumersResponse.builder()
                    .code(200)
                    .message("List of consumer retrieved")
                    .data(consumers)
                    .build();
        } catch (DataAccessException e) {
            return ConsumersResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return ConsumersResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }

    private Consumer mapToConsumerResponse(Consumer consumer) {
        return Consumer.builder()
                .id(consumer.getId())
                .name(consumer.getName())
                .username(consumer.getUsername())
                .password(consumer.getPassword())
                .phoneNumber(consumer.getPhoneNumber())
                .build();
    }

    @Transactional(readOnly = true)
    public ConsumerResponse getConsumer(Long consumerId) {
        try {
            Optional<Consumer> cafe = consumerRepository.findById(consumerId);
            return ConsumerResponse.builder()
                    .code(200)
                    .message("Consumer with id " + consumerId + " retrieved")
                    .data(cafe.get())
                    .build();
        } catch (NoSuchElementException e) {
            return ConsumerResponse.builder()
                    .code(404)
                    .message("Consumer with id " + consumerId + " not found")
                    .build();
        } catch (Exception e) {
            return ConsumerResponse.builder()
                    .code(500)
                    .message(e.getMessage())
                    .build();
        }
    }

    public ConsumerResponse updateConsumer(@PathVariable Long consumerId, @RequestBody ConsumerRequest consumerRequest) {
        try {
            Optional<Consumer> consumerRepo = consumerRepository.findById(consumerId);
            if(consumerRepo.isEmpty()) {
                return ConsumerResponse.builder()
                        .code(404)
                        .message("Consumer with id " + consumerId + " not found")
                        .build();
            }

            Consumer updatedCafe = consumerRepo.get();
            if (consumerRequest.getName() != null) {
                updatedCafe.setName(consumerRequest.getName());
            }
            if (consumerRequest.getUsername() != null) {
                updatedCafe.setUsername(consumerRequest.getUsername());
            }
            if (consumerRequest.getPhoneNumber() != null) {
                updatedCafe.setPhoneNumber(consumerRequest.getPhoneNumber());
            }

            Consumer savedConsumer = consumerRepository.save(updatedCafe);
            if(savedConsumer != null) {
                return ConsumerResponse.builder()
                        .code(200)
                        .message("Consumer updated")
                        .data(savedConsumer)
                        .build();
            } else {
                return ConsumerResponse.builder()
                        .code(500)
                        .message("Failed to update consumer")
                        .build();
            }
        } catch (DataAccessException e) {
            return ConsumerResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return ConsumerResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }

    public ConsumerResponse deleteConsumer(Long consumerId) {
        try {
            Optional<Consumer> consumer = consumerRepository.findById(consumerId);
            if (!consumer.isPresent()) {
                return ConsumerResponse.builder()
                        .code(404)
                        .message("Consumer with id " + consumerId + " not found")
                        .build();
            }
            consumerRepository.deleteById(consumerId);
            return ConsumerResponse.builder()
                    .code(200)
                    .message("Consumer with id " + consumerId + " deleted")
                    .data(consumer.get())
                    .build();
        } catch (DataAccessException e) {
            return ConsumerResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return ConsumerResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }
}
