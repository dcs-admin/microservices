package org.optmistic.service;

import org.optmistic.model.BookingTickets;
import org.optmistic.repository.BookingTicketsRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private final BookingTicketsRepository bookingTicketsRepository;

    public BookingService(BookingTicketsRepository bookingTicketsRepository) {
        this.bookingTicketsRepository = bookingTicketsRepository;
    }

    @Transactional
    public String bookTicket(String userId, String busNumber, String fromStation, String toStation, String journeyDate, String ticketId) {
        System.out.println("busNumber: "+busNumber+" fromStation: "+fromStation+" toStation: "+toStation+" journeyDate: "+journeyDate+" ticketId: "+ticketId);
        // Check if the ticket is already booked
        boolean isTicketBooked = bookingTicketsRepository.existsByUserIdAndBusNumberAndFromStationAndToStationAndJourneyDateAndTicketId(userId, busNumber, fromStation, toStation, journeyDate, ticketId);

        if (isTicketBooked) {
            throw new RuntimeException("Ticket is already booked for the given criteria.");
        }

        try {
            System.out.println("Ticket:"+ticketId+" booking in progress for userId: "+userId);
            Thread.sleep(5*1000);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Book the ticket
        BookingTickets booking = new BookingTickets();
        booking.setBusNumber(busNumber);
        booking.setFromStation(fromStation);
        booking.setToStation(toStation);
        booking.setJourneyDate(journeyDate);
        booking.setTicketId(ticketId);
        booking.setUserId(userId);

        try {
            bookingTicketsRepository.save(booking);
            System.out.println("SUCCESS:: Ticket:"+ticketId+" userId: "+userId);
        } catch (DataIntegrityViolationException e) {
            System.out.println("FAILED:: Ticket:"+ticketId+" userId: "+userId);
            throw new IllegalArgumentException("Duplicate booking for the specified criteria.");
        } catch (OptimisticLockingFailureException e) {
            // Handle optimistic locking failure, for example, retry or inform the user
            System.out.println("Optimistic Locking Failure. Ticket was updated by another user.");
            // Optionally, you can add a retry mechanism here
        }
        return "Ticket booked successfully!";
    }
}
