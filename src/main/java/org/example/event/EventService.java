package org.example.event;

import org.example.models.Event;
import org.example.request.EventRequest;
import org.example.request.SortedEventRequest;
import org.example.models.Location;
import org.example.repository.EventRepository;
import org.example.repository.LocationRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class EventService {
    private final EventRepository eventRepository;

    private final LocationRepository locationRepository;
    private final EventReminder eventReminder;

    public EventService(EventRepository eventRepository, LocationRepository locationRepository, EventReminder eventReminder) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.eventReminder = eventReminder;
    }

    @Transactional
    public void addEvent(EventRequest eventRequest) {
        // get location entity
        String locationName = eventRequest.getLocationName();
        // check if existed, if not create new entity
        Location location = locationRepository.findByName(locationName).orElse(null);

        if (location == null) {
            location = new Location(eventRequest.getLocationName());
            locationRepository.save(location);
        }
        // Create new event with location
        Event event = new Event(eventRequest.getName(), eventRequest.getDate(), eventRequest.getPopularity(), location);
        eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getEvents(SortedEventRequest sortedEventRequest) {
        String location = sortedEventRequest.getLocation();
        String sortBy = sortedEventRequest.getSortBy();
        if (location == null && sortBy == null) {
            return eventRepository.findAll();
        } else if (location != null && sortBy == null) {
            return eventRepository.findByLocation(location);
        } else if (location == null) {
            return eventRepository.findAll(Sort.by(sortBy));
        }
        return eventRepository.findByLocation(location, Sort.by(sortBy).descending());
    }

    public Event getEventById(int id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        return optionalEvent.orElse(null);
    }

    @Transactional
    public boolean updateEvent(Event event) {
        // Check if the event exists before updating
        Event oldEvent = eventRepository.findById(event.getId()).orElse(null);
        if (oldEvent != null) {
            checkSingleLocationIfChange(oldEvent.getLocation(), event.getLocation(), 1);
            eventRepository.save(event);
            return true;
        } else {
            // If event is not found, return null
            return false;
        }
    }


    private void checkSingleLocationIfChange(Location oldLocation, Location newLocation, int min) {
        if (!oldLocation.equals(newLocation)) {
            checkIfLocationsIsUsed(oldLocation, min);
            // Save/update new location
            locationRepository.save(newLocation);
        }
    }

    private void checkIfLocationsIsUsed(Location location, int min) {
        // Check if the old location needs to be deleted
        int oldLocationCount = eventRepository.countByLocationId(location.getId());
        if (oldLocationCount == min) {
            // If have one entity - it's the event that needs to be changed
            locationRepository.delete(location);
        }
    }

    @Transactional
    public boolean deleteEvent(Event event) {
        int id = event.getId();
        boolean exist = eventRepository.existsById(id);
        if (exist) {
            // Check if the location needs to be deleted
            checkIfLocationsIsUsed(event.getLocation(), 1);
            eventRepository.deleteById(id);
            return true;
        } else {
            // If event is not found, return null
            return false;
        }
    }

    public boolean addReminder(int eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()) {
            return false;
        }
        Event event = optionalEvent.get();
        Date eventTime = event.getDate();
        String eventName = event.getName();
        eventReminder.scheduler(eventTime, eventName);
        return true;
    }

}
