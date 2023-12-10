package org.example.configuration;

import org.example.event.*;
import org.example.repository.EventRepository;
import org.example.repository.LocationRepository;
import org.example.event.EventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class AppConfig {
    @Bean
    public EventBatchService EventBatchService(TransactionTemplate transactionTemplate, EventRepository eventRepository, LocationRepository locationRepository) {
        return new EventBatchService(transactionTemplate, eventRepository,locationRepository);
    }
    @Bean
    public EventService EventService(EventRepository eventRepository, LocationRepository locationRepository){
        return new EventService(eventRepository, locationRepository, new EventReminder(Executors.newSingleThreadScheduledExecutor()));
    }

    @Bean
    public EventReminder EventReminder(ScheduledExecutorService scheduler) {
        return new EventReminder(scheduler);
    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(5);
    }

//    @Bean
//    public SubscribeService subscribeService(UserRepository userRepository, EventRepository eventRepository, SubscriptionRepository subscriptionRepository){
//        return new SubscribeService(userRepository, eventRepository, subscriptionRepository);
//    }
}


