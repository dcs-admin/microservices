@Version atttribute 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class TicketBookingService {

    @Autowired
    private BookingTicketsRepository bookingRepository;

    public void bookTicket(Long ticketId, String newFromStation, String newToStation, String newJourneyDate) {
        try {
            // Fetch the ticket details
            BookingTickets ticket = bookingRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

            // Update ticket details
            ticket.setFromStation(newFromStation);
            ticket.setToStation(newToStation);
            ticket.setJourneyDate(newJourneyDate);

            // Save the updated ticket (optimistic locking will automatically check for version conflict)
            bookingRepository.save(ticket);

        } catch (OptimisticLockingFailureException e) {
            // Handle optimistic locking failure, for example, retry or inform the user
            System.out.println("Optimistic Locking Failure. Ticket was updated by another user.");
            // Optionally, you can add a retry mechanism here
        }
    }
}


```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
public class TicketBookingController {

    @Autowired
    private TicketBookingService ticketBookingService;

    @RequestMapping("/book")
    public String bookTicket(@RequestParam Long ticketId,
                             @RequestParam String fromStation,
                             @RequestParam String toStation,
                             @RequestParam String journeyDate) {
        ticketBookingService.bookTicket(ticketId, fromStation, toStation, journeyDate);
        return "Ticket booked successfully";
    }
}
```

Database Behavior:
Initial State: The database contains the BookingTickets with a version column. Initially, this version will be 1.
ticketId	busNumber	fromStation	toStation	journeyDate	version
1	101	A	B	2024-12-01	1
2	102	C	D	2024-12-02	1
Step 1: User 1 fetches the ticket with ticketId = 1. The version is 1 at this point.
Step 2: User 2 fetches the ticket with ticketId = 1. The version is also 1.
Step 3: User 1 updates the ticket and saves it. The version is incremented to 2.
Step 4: User 2 tries to update the ticket. Since the version has changed (from 1 to 2), Spring will throw an OptimisticLockingFailureException.
Optimistic Locking Flow:
User 1 gets the ticket, modifies it, and saves it. The version is updated from 1 to 2.
User 2, who fetched the same ticket, tries to save their changes. The version conflict is detected because the version number in the database has changed since User 2 fetched the ticket.
The OptimisticLockingFailureException is thrown, and User 2 is notified of the conflict, allowing them to retry or be informed that someone else has updated the ticket.
Conclusion:
Optimistic locking provides a way to handle concurrent data modifications in a multi-user environment without locking the resource entirely. By adding a @Version annotation to the BookingTickets entity, Spring Boot's JPA automatically checks for version conflicts when saving the entity. This is ideal for scenarios like ticket booking where multiple users might attempt to modify the same resource.