package org.example.models;

import org.example.models.Event;
import org.example.models.Subscriber;

import javax.persistence.*;

@Entity
@Table(schema = "my_schema", name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private Subscriber subscriber;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    public Subscription(Subscriber subscriber, Event event) {
        this.subscriber = subscriber;
        this.event = event;
    }

    public Subscription() {
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public Event getEvent() {
        return event;
    }

}
