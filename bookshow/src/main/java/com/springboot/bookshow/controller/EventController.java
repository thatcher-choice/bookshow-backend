package com.springboot.bookshow.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.bookshow.model.Buyer;
import com.springboot.bookshow.model.Event;
import com.springboot.bookshow.model.Ticket;
import com.springboot.bookshow.repository.BuyerRepository;
import com.springboot.bookshow.repository.EventRepository;
import com.springboot.bookshow.service.BuyerService;
import com.springboot.bookshow.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {

    @Autowired
    private final EventService eventService;
    
    private final BuyerService buyerService;

    

    private final EventRepository eventRepo;
    
    private final BuyerRepository buyerRepo;
    
    public EventController(EventService eventService, BuyerService buyerService, EventRepository eventRepo,BuyerRepository buyerRepo ) {
        this.buyerService = buyerService;
        this.eventRepo = eventRepo;
        this.eventService = eventService;
        this.buyerRepo = buyerRepo;
        
    }

    // Get all events
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    // Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        if (event != null) {
            return new ResponseEntity<>(event, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/buyTicket")
    public ResponseEntity<?> buyTicket(@RequestBody Buyer buyRequest) {
    	return buyerService.buyTicket(buyRequest);
    }

    // Create a new event
    @PostMapping("/addEvent")
    public void createEvent(@RequestBody Event eventRequest) {
    	System.out.print(eventRequest);
    	eventRepo.save(eventRequest);
    }

    // Update an existing event
//    @PutMapping("/{id}")
//    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventRequest) {
//        Event updatedEvent = convertToEvent(eventRequest);
//        Event result = eventService.updateEvent(id, updatedEvent);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    // Delete an event
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    // Convert EventRequest to Event entity (you need to implement this conversion logic)
    
}

