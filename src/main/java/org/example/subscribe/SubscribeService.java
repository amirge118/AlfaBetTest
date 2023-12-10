package org.example.subscribe;

import org.example.models.Event;
import org.example.repository.EventRepository;
import org.example.repository.SubscriptionRepository;
import org.example.repository.SubscriberRepository;
import org.example.request.SubscribeRequest;
import org.example.models.Subscription;
import org.example.models.Subscriber;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubscribeService {

    private final SubscriberRepository subscriberRepository;
    private final EventRepository eventRepository;
    private final SubscriptionRepository subscriptionRepository;

    public SubscribeService(SubscriberRepository subscriberRepository, EventRepository eventRepository, SubscriptionRepository subscriptionRepository){
        this.subscriberRepository = subscriberRepository;
        this.eventRepository = eventRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public void subscribe(SubscribeRequest subscribeRequest) {
        String name = subscribeRequest.name();
        String mail = subscribeRequest.mail();
        int eventId = subscribeRequest.eventId();
        Subscriber subscriber = subscriberRepository.getByName(name).orElse(null);
        if (subscriber == null) {
            subscriber = new Subscriber(name, mail);
        }
        Event event;
        if (!eventRepository.existsById(eventId)){
            throw new IllegalArgumentException("Event not found");
        };
        event = eventRepository.getById(eventId);
        List<Subscription> subscriptionList = subscriptionRepository.findByEventIdAndSubscriberName(eventId, name);
        if (!subscriptionList.isEmpty()){
            throw new IllegalArgumentException("Already subscribed");
        }
        Subscription subscription = new Subscription(subscriber, event);
        subscriberRepository.save(subscriber);
        subscriptionRepository.save(subscription);
    }

    public void updateEvent(int eventId, String message) {
        // Check if event exist
        if (!eventRepository.existsById(eventId)){
            throw new IllegalArgumentException();
        };

        String updatedMessage = "Event updated: " + message;
        notifySubscribers(eventId, updatedMessage);
    }

    public void cancelEvent(int eventId, String message) {
        // Check if event exist
        if (!eventRepository.existsById(eventId)){
            throw new IllegalArgumentException();
        };
        String canceledMessage = "Event canceled: " + message;
        notifySubscribers(eventId, canceledMessage);
    }

    private void notifySubscribers(int eventId, String message) {
        List<Subscription> subscriptionList = subscriptionRepository.findByEventId(eventId);
        for (Subscription subscription: subscriptionList) {
            Subscriber subscriber = subscription.getSubscriber();
            System.out.println("massage send to: " + subscriber.getMail());
            System.out.println("Dear " + subscriber.getName() + ":");
            System.out.println(message);
        }
    }
}
