package org.optmistic.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.optmistic.model.BookingTickets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookingsParallel")
public class BookingCreateMocker {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/book")
    public ResponseEntity<List<ResponseDTO>> bookTicket(@RequestParam String userId,
                                                        @RequestParam String busNumber,
                                                        @RequestParam String fromStation,
                                                        @RequestParam String toStation,
                                                        @RequestParam String journeyDate,
                                                        @RequestParam String ticketId,
                                                        @RequestParam Integer numberOfThreads

    ) {

        String baseUrl = "http://localhost:9111/bookings/book?busNumber=" + busNumber + "&fromStation=" + fromStation + "&toStation=" + toStation + "&journeyDate=" + journeyDate + "&ticketId=" + ticketId +"&userId=" ;
        BookingTickets tempBookingTickets = new BookingTickets();
        tempBookingTickets.setBusNumber(busNumber);
        tempBookingTickets.setFromStation(fromStation);
        tempBookingTickets.setToStation(toStation);
        tempBookingTickets.setJourneyDate(journeyDate);
        tempBookingTickets.setTicketId(ticketId);
        tempBookingTickets.setUserId(userId);


        // List to hold threads
        List<Thread> threads = new ArrayList<>();
        List<ResponseDTO> responses = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            int user_Id = 1 + i; // Generate unique request IDs dynamically
            Thread thread = new Thread(() -> {
                try {
                    ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + user_Id, null, String.class);
                    tempBookingTickets.setUserId(String.valueOf(user_Id));
                    responses.add(new ResponseDTO(response.getStatusCode(), response.getBody(), tempBookingTickets.clone()));
                } catch (HttpClientErrorException e) {
                    // Handle 4xx errors
                    tempBookingTickets.setUserId(String.valueOf(user_Id));
                    responses.add(new ResponseDTO( e.getStatusCode(), e.getResponseBodyAsString(), tempBookingTickets.clone()));

                } catch (HttpServerErrorException e) {
                    // Handle 5xx errors
                    tempBookingTickets.setUserId(String.valueOf(user_Id));
                    responses.add(new ResponseDTO( e.getStatusCode(), e.getResponseBodyAsString(), tempBookingTickets.clone()));
                } catch (Exception e) {
                    // Handle other exceptions
                     tempBookingTickets.setUserId(String.valueOf(user_Id));
                    responses.add(new ResponseDTO( HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occurred", tempBookingTickets.clone()));

                }
            });
            threads.add(thread);
        }

        // Start all threads
        threads.forEach(Thread::start);

        // Wait for all threads to complete
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle interrupted exception
                System.err.println("Thread interrupted: " + e.getMessage());
            }
        });

        System.out.println("All threads completed.");
        //Convert the responses to string and return
        //All threads responses are not coming in following

//        return ResponseEntity.ok(
//                responses.stream()
//                        .map(response -> new ResponseDTO(response.getStatusCode(), response.getBody(), tempBookingTickets))
//                        .collect(Collectors.toList())
//        );

        return ResponseEntity.ok(responses);


    }


    @Getter
    @Setter

    class ResponseDTO {
        private HttpStatus statusCode;
        private Object body;
        private BookingTickets bookingTickets;

        public ResponseDTO(HttpStatus statusCode, Object body, BookingTickets bookingTickets) {
            this.statusCode = statusCode;
            this.body = body;
            this.bookingTickets = bookingTickets;
        }

        // Getters and setters
    }

}
