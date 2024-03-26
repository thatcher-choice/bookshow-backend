package com.springboot.bookshow.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.springboot.bookshow.model.Buyer;
import com.springboot.bookshow.model.Event;
import com.springboot.bookshow.model.Ticket;
import com.springboot.bookshow.repository.BuyerRepository;
import com.springboot.bookshow.repository.EventRepository;
import com.springboot.bookshow.repository.TicketRepository;

import jakarta.transaction.Transactional;

@Service
public class BuyerService {
	private final BuyerRepository buyerRepo;
	private final EventRepository eventRepo; 
	private final TicketRepository ticketRepo;
	public BuyerService(BuyerRepository buyerRepository, EventRepository eventRepo, TicketRepository ticketRepo) {
        this.buyerRepo = buyerRepository;
        this.eventRepo = eventRepo;
        this.ticketRepo = ticketRepo;
        
    }
	public static final List<String> ALPHABET_LIST = Collections.unmodifiableList(Arrays.asList(
	        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
	        "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
	    ));
	 private static final Map<String, Integer> SEAT_AVAILABILITY_MAP = new HashMap<>();

	    static {
	        // Initialize the map with seat locations and total seats
	        SEAT_AVAILABILITY_MAP.put("front", 100);
	        SEAT_AVAILABILITY_MAP.put("middle", 100);
	        SEAT_AVAILABILITY_MAP.put("back", 60);
	    }

	
	public ResponseEntity<?> buyTicket(Buyer buyRequest)
	{
		
		
		long numberOfTicketsBought =  buyRequest.getNumberOfTickets();
		String seatLocation = buyRequest.getSeatLocation();
		Event event = eventRepo.findById(buyRequest.getEventId())
	            .orElseThrow();
		long eventId = event.getId();
		int seatsBooked = ticketRepo.countByEventIdAndSeatLocation(eventId, seatLocation);
		int seatTypeTotalSeats = SEAT_AVAILABILITY_MAP.get(seatLocation);
		if((seatsBooked + numberOfTicketsBought) > seatTypeTotalSeats) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Seats not available for " +seatLocation +" row");
		
		Buyer savedBuyer = buyerRepo.save(buyRequest);
		
		long availableTickets = event.getAvailableSeats();
		
		
		if(availableTickets >= numberOfTicketsBought && (seatsBooked + numberOfTicketsBought) <= seatTypeTotalSeats)
		{
			event.setAvailableSeats(availableTickets - numberOfTicketsBought);
			eventRepo.save(event);
			
			BigDecimal ticketPrice = calculateTicketPrice(event.getPrice(), buyRequest.getSeatLocation());
			
			List<String> seatNumbers = generateSeatNumbers(savedBuyer);
			List<Ticket> tickets = new ArrayList<>();
			for (String seatNumber : seatNumbers) {
                Ticket ticket = new Ticket();
                ticket.setEvent(event);
                ticket.setBuyer(savedBuyer);
                ticket.setTicketPrice(ticketPrice);
                ticket.setSeatNumber(seatNumber);
                ticket.setSeatLocation(seatLocation);
                ticket.setPurchaseDate(savedBuyer.getDateOfPurchase());
                tickets.add(ticket);
            }
			ticketRepo.saveAll(tickets);
			return ResponseEntity.ok(tickets);
		}
		else {
            // Handle insufficient available seats scenario
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Seats not available");
        }
	}

	private BigDecimal calculateTicketPrice(BigDecimal basePrice, String seatLocation) {
		if ("front".equalsIgnoreCase(seatLocation)) {
	        return basePrice; // Use the base price for front seats
	    } else if ("middle".equalsIgnoreCase(seatLocation)) {
	        return basePrice.multiply(BigDecimal.valueOf(1.2)); // Increase price for middle seats
	    } else {
	        return basePrice.multiply(BigDecimal.valueOf(1.5)); // Increase price for back seats
	    }
	
	}

	private List<String> generateSeatNumbers(Buyer buyer) {
		List<String> ticketList = new ArrayList<>();
		String seatLocation = buyer.getSeatLocation();
		long eventId = buyer.getEventId();
		long numberOfTicketsBought = buyer.getNumberOfTickets();
		int seatsBooked = ticketRepo.countByEventIdAndSeatLocation(eventId, seatLocation);
		long ticketNo = seatsBooked + 1;
		System.out.print(seatsBooked + "seatsBooked");
		int seatTypeTotalSeats = SEAT_AVAILABILITY_MAP.get(seatLocation);
		if((seatsBooked + numberOfTicketsBought) < seatTypeTotalSeats) {
			for(int i  = 0 ; i < numberOfTicketsBought; i++) {
				int factor = 0;
				if("middle".equalsIgnoreCase(seatLocation)) {
					factor = 100;
				}
				else if("back".equalsIgnoreCase(seatLocation)) {
					factor = 200;
				}
				int codeAlphabetNo = (int) ((ticketNo + factor)/10);
				String code = (int) ticketNo % 10 == 0 ?  ALPHABET_LIST.get(codeAlphabetNo - 1) : ALPHABET_LIST.get(codeAlphabetNo);
				int codeNo =(int) ticketNo % 10 == 0 ? 10 :(int) ticketNo % 10 ;
				String seatNum = code + codeNo;
				ticketList.add(seatNum);
				ticketNo++;
			}
			return ticketList;
		}
		else {
			return null;
		}
	}
	
}
