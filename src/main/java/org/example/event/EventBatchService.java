package org.example.event;

import org.example.models.Event;
import org.example.request.EventRequest;
import org.example.models.Location;
import org.example.repository.EventRepository;
import org.example.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventBatchService {

    private final TransactionTemplate transactionTemplate;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;

    public EventBatchService(TransactionTemplate transactionTemplate, EventRepository eventRepository1, LocationRepository locationRepository1) {
        this.transactionTemplate = transactionTemplate;
        this.eventRepository = eventRepository1;
        this.locationRepository = locationRepository1;
    }

    private boolean executeInTransaction(TransactionCallback<Boolean> callback) {
        return Boolean.TRUE.equals(transactionTemplate.execute(callback));
    }


    public boolean addEvents(List<EventRequest> eventRequestList) {
        return executeInTransaction(status -> {
            try {
                HashMap<String, Location> locationMap = new HashMap<>();
                List<Event> newEventslist = new ArrayList<>();

                for (EventRequest eventRequest : eventRequestList) {
                    String locationName = eventRequest.getLocationName();
                    // check if existed, if not create new entity
                    Location location = locationRepository.findByName(locationName).orElse(null);

                    if (location == null && !locationMap.containsKey(locationName)) {
                        Location newLocation = new Location(locationName);
                        locationMap.put(locationName, newLocation);
                        location = newLocation;
                    }
                    Event event = new Event(eventRequest.getName(), eventRequest.getDate(), eventRequest.getPopularity(), location);
                    newEventslist.add(event);

                }

                locationRepository.saveAll(locationMap.values());
                eventRepository.saveAll(newEventslist);
                return true;
            } catch (Exception e) {
                status.setRollbackOnly();
                return false;
            }
        });
    }

    public boolean updateEvents(List<Event> events) {
        return executeInTransaction(status -> {
            try {
                // Must check for delete location!!!
                List<Integer> idList = new ArrayList<>();
                for (Event event : events) {
                    idList.add(event.getId());
                }
                List<Event> checkEventExist = eventRepository.findAllById(idList);
                if (checkEventExist.size() != events.size()) {
                    return false;
                }
                eventRepository.saveAll(events);
                return true;
            } catch (Exception e) {
                status.setRollbackOnly();
                return false;
            }
        });
    }

    public boolean deleteEvents(List<Event> eventList) {
        return executeInTransaction(status -> {
            try {
                HashMap<Location, Integer> locationCountMap = new HashMap<>();
                for(Event event: eventList){
                    Location location = event.getLocation();
                    locationCountMap.put(location, locationCountMap.getOrDefault(location,0)+1);
                }
                for(Map.Entry<Location, Integer> entry: locationCountMap.entrySet()) {
                    Location location = entry.getKey();
                    int num = entry.getValue();
                    checkIfLocationsIsUsed(location, num);
                }
                eventRepository.deleteAll(eventList);
                return true;
            } catch (Exception e) {
                status.setRollbackOnly();
                return false;
            }
        });
    }

    private void checkIfLocationsIsUsed(Location location, int min) {
        // Check if the old location needs to be deleted
        int oldLocationCount = eventRepository.countByLocationId(location.getId());
        if (oldLocationCount == min) {
            // If have one entity - it's the event that needs to be changed
            locationRepository.delete(location);
        }
    }
}
