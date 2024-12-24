package org.optmistic.repository;

import org.optmistic.model.BookingTickets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingTicketsRepository extends JpaRepository<BookingTickets, Long> {
    //boolean existsByUserIdAndBusNumberAndJourneyDateAndTicketId(String userId, String busNumber, String journeyDate, String ticketId);

    boolean existsByUserIdAndBusNumberAndFromStationAndToStationAndJourneyDateAndTicketId(String userId, String busNumber, String fromStation, String toStation, String journeyDate, String ticketId);
}
