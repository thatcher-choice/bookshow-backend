package com.springboot.bookshow.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.bookshow.model.Event;
import com.springboot.bookshow.repository.EventRepository;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // Get all events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Get event by ID
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    // Create a new event
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // Update an existing event
//    public Event updateEvent(Long id, Event updatedEvent) {
//        Event existingEvent = eventRepository.findById(id).orElse(null);
//
//        if (existingEvent != null) {
//            existingEvent.setName(updatedEvent.getName());
//            existingEvent.setDate(updatedEvent.getDate());
//            existingEvent.setLocation(updatedEvent.getLocation());
//
//            return eventRepository.save(existingEvent);
//        } else {
//            // Handle the case where the event with the given id is not found
//            throw new RuntimeException("Event not found with id: " + id);
//        }
//    }

    // Delete an event
    public void deleteEvent(Long id) {
        Event existingEvent = eventRepository.findById(id).orElse(null);

        if (existingEvent != null) {
            eventRepository.deleteById(id);
        } else {
            // Handle the case where the event with the given id is not found
            throw new RuntimeException("Event not found with id: " + id);
        }
    }
}

