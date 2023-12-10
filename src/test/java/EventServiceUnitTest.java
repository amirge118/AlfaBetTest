import org.example.event.*;
import org.example.models.Event;
import org.example.request.EventRequest;
import org.example.models.Location;
import org.example.request.SortedEventRequest;
import org.example.repository.EventRepository;
import org.example.repository.LocationRepository;
import org.example.event.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceUnitTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private LocationRepository locationRepository;
    @Mock
    private EventReminder eventReminder;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddEvent() {
        // Prepare

        EventRequest eventRequest = new EventRequest("sunday", new Date(), 5, "Tel Aviv");

        when(locationRepository.findByName(any())).thenReturn(Optional.empty());

        // Act
       eventService.addEvent(eventRequest);

        // Verify
        verify(locationRepository, times(1)).findByName("Tel Aviv");
        verify(locationRepository, times(1)).save(any());
        verify(eventRepository, times(1)).save(any());
    }

    @Test
    void testGetAllEvents() {
        // Prepare
        List<Event> mockEvents = new ArrayList<>();
        Location location =  new Location("Tel Aviv");
        Event event = new Event("sunday", new Date(), 5, location);
        mockEvents.add(event);
        when(eventRepository.findAll()).thenReturn(mockEvents);

        // Act
        List<Event> result = eventService.getAllEvents();

        // Verify
        assertEquals(mockEvents, result);
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void testGetEventById() {
        // Prepare
        int eventId = 1;
        Location location =  new Location("Tel Aviv");
        Event mockEvent = new Event("sunday", new Date(), 5, location);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(mockEvent));

        // Act
        Event result = eventService.getEventById(eventId);

        // Verify
        assertEquals(mockEvent, result);
        verify(eventRepository, times(1)).findById(eventId);
    }


    @Test
    public void testUpdateEvent() {
        // Prepare
        Location location = new Location("Alenby");
        Event existingEvent = new Event(1, "Existing Event", new Date(), 0, location);
        Event updatedEvent = new Event(1, "Updated Event", new Date(), 1, location);
        when(eventRepository.findById(existingEvent.getId())).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        // Act
        boolean result = eventService.updateEvent(updatedEvent);

        // Verify
        assertTrue(result);
        verify(eventRepository, times(1)).findById(existingEvent.getId());
        verify(eventRepository, times(1)).save(updatedEvent);
    }

    @Test
    public void testUpdateEventEventNotFound() {
        // Prepare
        Event nonExistingEvent = new Event(1, "event", null, 0, null);
        when(eventRepository.findById(nonExistingEvent.getId())).thenReturn(Optional.empty());

        // Act
        boolean result = eventService.updateEvent(nonExistingEvent);

        // Verify
        assertFalse(result);
        verify(eventRepository, times(1)).findById(nonExistingEvent.getId());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void testDeleteEventWithLocationDelete() {
        // Prepare
        int eventId =1;
        Location location = new Location(1,"Tel Aviv");
        Event event = new Event(eventId, "Non-Existing Event", null, 0, location);
        when(eventRepository.existsById(event.getId())).thenReturn(true);
        when(eventRepository.countByLocationId(1)).thenReturn(1);

        // Act
        boolean result = eventService.deleteEvent(event);

        // verify
        assertTrue(result);
        verify(locationRepository, times(1)).delete(any());
        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    void testDeleteEventWithoutLocationDelete() {
        // Prepare
        int eventId =1;
        Location location = new Location(1,"Tel Aviv");
        Event event = new Event(eventId, "Non-Existing Event", null, 0, location);
        when(eventRepository.existsById(event.getId())).thenReturn(true);
        when(eventRepository.countByLocationId(1)).thenReturn(2);

        // Act
        boolean result = eventService.deleteEvent(event);

        // verify
        assertTrue(result);
        verify(locationRepository, times(0)).delete(any());
        verify(eventRepository, times(1)).deleteById(eventId);
    }


    @Test
    void testGetSortedEventsWithLocation() {
        // Prepare
        SortedEventRequest sortedEventRequest = new SortedEventRequest("popularity", "Tel Aviv");
        Location location =  new Location("Tel Aviv");
        Event mockEvent = new Event("sunday", new Date(), 5, location);
        List<Event> eventList = new ArrayList<>();
        eventList.add(mockEvent);
        when(eventRepository.findByLocation(any(),any())).thenReturn(eventList);

        // Act
        List<Event> result = eventService.getEvents(sortedEventRequest);

        // Verify
        assertNotNull(result);
        verify(eventRepository, times(1)).findByLocation(any(), any());
    }

    @Test
    void testGetSortedEventsWithoutLocation() {
        // Prepare
        SortedEventRequest sortedEventRequest = new SortedEventRequest("popularity", null);
        when(eventRepository.findAll((Sort) any())).thenReturn(new ArrayList<>());
        // Act
        List<Event> result = eventService.getEvents(sortedEventRequest);

        // Verify
        assertNotNull(result);
        verify(eventRepository, times(1)).findAll((Sort) any());
    }

    @Test
    void testGetSortedEventsWithLocationWithoutSort() {
        // Prepare
        SortedEventRequest sortedEventRequest = new SortedEventRequest( null, "Tel Aviv");
        when(eventRepository.findByLocation(any())).thenReturn(new ArrayList<>());
        // Act
        List<Event> result = eventService.getEvents(sortedEventRequest);

        // Verify
        assertNotNull(result);
        verify(eventRepository, times(1)).findByLocation(any());
    }

    @Test
    public void testAddReminder_Success() {
        // Prepare
        int eventId = 1;
        Event event = new Event( "EventName", new Date(), 10, new Location("LocationName"));

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // Act
        eventService.addReminder(eventId);

        // Verify
        verify(eventReminder, times(1)).scheduler(event.getDate(), event.getName());
    }

    @Test
    public void testAddReminder_EventNotFound() {
        // Prepare
        int eventId = 1;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act
        boolean result = eventService.addReminder(eventId);

        // Verify
        assertFalse(result);
    }
}
