import org.example.*;
import org.example.models.Event;
import org.example.repository.EventRepository;
import org.example.models.Location;
import org.example.repository.LocationRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventRepositoryUnitTest {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private LocationRepository locationRepository;

    private Date date = new Date();

    @BeforeEach
    @AfterAll
    public void clean() {
        eventRepository.deleteAll();
        locationRepository.deleteAll();
    }

    @Test
    void testFindById() {
        // Prepare
        Location location = new Location("Tel Aviv");
        locationRepository.save(location);
        Event mockEvent = new Event("sunday", date, 5, location);
        Event event = eventRepository.save(mockEvent);

        // Act
        Optional<Event> result = eventRepository.findById(event.getId());

        // Verify
        assertNotNull(result);
        assertTrue(result.isPresent());
        Event check = result.get();
        assertEquals(check.getName(), "sunday");
        assertEquals(check.getLocation().getName(), "Tel Aviv");
        assertEquals(check.getPopularity(), 5);
    }

    @Test
    void testFindByLocation() {
        // Prepare
        Location location = new Location("Tel Aviv");
        Location location2 = new Location("Haifa");
        locationRepository.saveAll(Arrays.asList(location, location2));
        Event mockEvent = new Event("sunday", new Date(), 5, location);
        Event mockEvent2 = new Event("sunday", new Date(), 5, location2);
        eventRepository.saveAll(Arrays.asList(mockEvent, mockEvent2));
        List<Event> result = eventRepository.findAll();
        assertEquals(result.size(), 2);
        // Act
        result = eventRepository.findByLocation(location.getName());

        // Assert
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    @Test
    void testFindByLocationWithSort() {
        // Prepare
        Location location = new Location("Tel Aviv");
        Location location2 = new Location("Haifa");
        locationRepository.saveAll(Arrays.asList(location, location2));
        Event mockEvent = new Event("sunday", new Date(), 5, location);
        Event mockEvent2 = new Event("sunday", new Date(), 5, location2);
        Event mockEvent3 = new Event("monday", new Date(), 10, location);
        Event mockEvent4 = new Event("sunday", new Date(), 5, location2);
        eventRepository.saveAll(Arrays.asList(mockEvent, mockEvent2, mockEvent3, mockEvent4));
        List<Event> result = eventRepository.findAll();
        assertEquals(result.size(), 4);
        // Act
        result = eventRepository.findByLocation(location.getName(), Sort.by("popularity").descending());

        // Assert
        assertNotNull(result);
        assertEquals(result.size(), 2);
        mockEvent = result.get(0);
        mockEvent2 = result.get(1);
        assertTrue(mockEvent.getPopularity() > mockEvent2.getPopularity());
        assertEquals(mockEvent.getLocation().getName(), "Tel Aviv");
    }

    @Test
    void testCountByLocationId() {
        // Prepare
        Location location = new Location("Tel Aviv");
        locationRepository.save(location);
        Event mockEvent = new Event("sunday", new Date(), 5, location);
        Event mockEvent2 = new Event("monday", new Date(), 10, location);
        eventRepository.saveAll(Arrays.asList(mockEvent, mockEvent2));

        // Act
        long result = eventRepository.countByLocationId(location.getId());

        // Assert
        assertEquals(result,2);
    }
}