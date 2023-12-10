package org.example.subscribe;

import org.example.request.SubscribeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/event/subscribe")
public class SubscribeController {

    private final SubscribeService subscribeService;

    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @PostMapping()
    public ResponseEntity<String> subscribeEvent(@RequestBody SubscribeRequest subscribeRequest) {
        try {
            subscribeService.subscribe(subscribeRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("User subscribe to event successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create event");
        }

    }

    @PostMapping("/update")
    public ResponseEntity<String> updateEvent(@RequestParam Integer eventId, @RequestBody String message) {
        try {
            subscribeService.updateEvent(eventId, message);
            return ResponseEntity.ok("Update alert send to all event's subscriber");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event not found");
        } catch (Exception e) {
            // Handle other exceptions and return a 500 Internal Server Error status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<String> cancelEvent(@RequestParam Integer eventId, @RequestBody String message) {
        try {
            subscribeService.cancelEvent(eventId, message);
            return ResponseEntity.ok("Cancel alert sened to all event's subscriber");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event not found");
        } catch (Exception e) {
            // Handle other exceptions and return a 500 Internal Server Error status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
