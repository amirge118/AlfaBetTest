package org.example.repository;

import org.example.models.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriberRepository extends JpaRepository<Subscriber, Integer> {
    Optional<Subscriber> getByName(String name);
}
