package com.springboot.bookshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.springboot.bookshow.model.Ticket;

@RepositoryRestResource
public interface TicketRepository extends JpaRepository<Ticket, Long>{
    int countByEventIdAndSeatLocation(Long eventId, String seatLocation);
}
