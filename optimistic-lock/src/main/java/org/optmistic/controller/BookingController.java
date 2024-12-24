package org.optmistic.controller;

import org.optmistic.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookTicket(@RequestParam String userId,
                                             @RequestParam String busNumber,
                                             @RequestParam String fromStation,
                                             @RequestParam String toStation,
                                             @RequestParam String journeyDate,
                                             @RequestParam String ticketId) {
        String result = "";
        try {
             result = bookingService.bookTicket(userId, busNumber, fromStation, toStation, journeyDate, ticketId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}
