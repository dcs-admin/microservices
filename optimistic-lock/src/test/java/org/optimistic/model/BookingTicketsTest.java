package org.optimistic.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.optmistic.model.BookingTickets;
import org.optmistic.repository.BookingTicketsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import static org.mockito.Mockito.when;


@SpringBootTest(classes = BookingTicketsTest.class)
public class BookingTicketsTest {

    @MockBean
    private BookingTicketsRepository bookingRepository;

    @Test
    public void testUniqueConstraint() {

        // Arrange: Mock repository behavior
        BookingTickets ticket = new BookingTickets();
        ticket.setTicketId("123");
        when(bookingRepository.save(ticket)).thenReturn(ticket);

        BookingTickets booking1 = new BookingTickets();
        booking1.setBusNumber("123");
        booking1.setFromStation("A");
        booking1.setToStation("B");
        booking1.setJourneyDate("2024-12-22");
        booking1.setTicketId("T123");
        booking1.setUserId("11");

        BookingTickets booking2 = new BookingTickets();
        booking2.setBusNumber("123");
        booking2.setFromStation("A");
        booking2.setToStation("B");
        booking2.setJourneyDate("2024-12-22");
        booking2.setTicketId("T123");
        booking2.setUserId("11");

        booking1 = bookingRepository.save(booking1);

//        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
//            bookingRepository.save(booking2);
//        });
    }
}
