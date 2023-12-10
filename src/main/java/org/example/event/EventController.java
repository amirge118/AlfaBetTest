package org.example.event;

import org.example.event.*;
import org.example.models.Event;
import org.example.request.EventRequest;
import org.example.request.SortedEventRequest;
import org.example.event.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/event")
public class EventController {

    private final EventService eventService;
    private final EventBatchService eventBatchService;
    public EventController(EventService eventService, EventBatchService eventBatchService) {
        this.eventService = eventService;
        this.eventBatchService = eventBatchService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addEvent(@RequestBody EventRequest eventRequest) {
        try {
            eventService.addEvent(eventRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Event created successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create event");
        }
    }

    @PostMapping("/add-batch")
    public ResponseEntity<String> addEvents(@RequestBody List<EventRequest> eventRequestList) {
        try {
            eventBatchService.addEvents(eventRequestList);
            return ResponseEntity.status(HttpStatus.CREATED).body("Events created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create event");
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Event>> getAllEvents() {
        try {
            List<Event> list = eventService.getAllEvents();
            return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<Event>> getEvents(@RequestBody SortedEventRequest sortedEventRequest) {
        try {
            List<Event> list = eventService.getEvents(sortedEventRequest);
            return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/byId")
    public ResponseEntity<Event> getEventById(@RequestParam int id) {
        try {
            Event event = eventService.getEventById(id);
            return event != null ? ResponseEntity.ok(event): ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Handle other exceptions and return a 500 Internal Server Error status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> updateEvent(@RequestBody Event event) {
        try {
            boolean success = eventService.updateEvent(event);
            return success ? ResponseEntity.ok(true): ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            // Handle other exceptions and return a 500 Internal Server Error status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/update-batch")
    public ResponseEntity<Boolean> updateEvents(@RequestBody List<Event> events) {
        try {
            boolean success = eventBatchService.updateEvents(events);
            return success ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }  catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            // Handle other exceptions and return a 500 Internal Server Error status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteEvent(@RequestBody Event event) {
        try {
            boolean deleted = eventService.deleteEvent(event);
            if (deleted) {
                return ResponseEntity.ok("Event deleted successfully");
            }
            // Return 404 Not Found if the event is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        } catch (Exception e) {
            // Handle other exceptions and return a 500 Internal Server Error status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete event");
        }
    }

    @PostMapping("/delete-batch")
    public ResponseEntity<String> deleteEvents(@RequestBody List<Event> eventList) {
        try {
            boolean deleted = eventBatchService.deleteEvents(eventList);
            if (deleted) {
                return ResponseEntity.ok("Event deleted successfully");
            }
            // Return 404 Not Found if the event is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error will trying to finding events");
        } catch (Exception e) {
            // Handle other exceptions and return a 500 Internal Server Error status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete event");
        }
    }

    @PostMapping("/reminder")
    public ResponseEntity<String> addReminders(@RequestParam int eventId) {
        try {
            boolean check = eventService.addReminder(eventId);
            if (check) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Reminder scheduled successfully for the event");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to schedule reminder");
        }
    }


}
