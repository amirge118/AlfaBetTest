import org.example.models.Event;
import org.example.repository.EventRepository;
import org.example.repository.SubscriptionRepository;
import org.example.repository.SubscriberRepository;
import org.example.request.SubscribeRequest;
import org.example.subscribe.SubscribeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class SubscribeServiceTest {

    @Mock
    private SubscriberRepository subscriberRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @InjectMocks
    private SubscribeService subscribeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubscribe() {
        SubscribeRequest subscribeRequest = new SubscribeRequest(1, "John", "john@example.com");

        // Prepare
        when(eventRepository.existsById(1)).thenReturn(true);
        when(subscriberRepository.getByName("John")).thenReturn(Optional.empty());
        when(eventRepository.getById(1)).thenReturn(new Event());
        when(subscriptionRepository.findByEventIdAndSubscriberName(1, "John")).thenReturn(Collections.emptyList());

        // Act
        subscribeService.subscribe(subscribeRequest);

        // Verify
        verify(subscriberRepository, times(1)).save(any());
        verify(subscriptionRepository, times(1)).save(any());
    }

    @Test()
    void testSubscribeFailed() {
        SubscribeRequest subscribeRequest = new SubscribeRequest(1, "John", "john@example.com");

        // Prepare
        when(subscriberRepository.getByName("John")).thenReturn(Optional.empty());
        when(eventRepository.existsById(1)).thenReturn(false);


        // Act
        assertThrows(IllegalArgumentException.class, () -> {
            subscribeService.subscribe(subscribeRequest);
        });

        // Verify
        verify(subscriberRepository, times(0)).save(any());
        verify(subscriptionRepository, times(0)).save(any());
    }

    @Test
    void testUpdateEvent() {
        // Prepare
        when(eventRepository.existsById(1)).thenReturn(true);
        when(subscriptionRepository.findByEventId(1)).thenReturn(Collections.emptyList());
        // Act
        subscribeService.updateEvent(1, "Updated message");

        // Verify
        verify(subscriptionRepository).findByEventId(1);
    }

    @Test
    void testCancelEvent() {
        // Prepare
        when(eventRepository.existsById(1)).thenReturn(true);
        when(subscriptionRepository.findByEventId(1)).thenReturn(Collections.emptyList());

        // Act
        subscribeService.cancelEvent(1, "Updated message");

        // Verify
        verify(subscriptionRepository).findByEventId(1);
    }
}