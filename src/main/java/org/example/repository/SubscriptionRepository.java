package org.example.repository;

import org.example.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByEventId(int eventId);
    List<Subscription> findByEventIdAndSubscriberName(int eventId, String subscriberName);
}
