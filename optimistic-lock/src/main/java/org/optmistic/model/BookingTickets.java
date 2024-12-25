package org.optmistic.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "booking_tickets", uniqueConstraints = @UniqueConstraint(  columnNames = {"busNumber", "journeyDate", "ticketId"})
)
public class BookingTickets implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String userId;
    @Column(length = 10)
    private String busNumber;
    @Column(length = 10)
    private String fromStation;
    @Column(length = 10)
    private String toStation;
    @Column(length = 10)
    private String journeyDate; // Format: YYYY-MM-DD
    @Column(length = 10)
    private String ticketId;

    @Version
    private int version; // For optimistic locking

    @Override
   public BookingTickets clone() {
        return new BookingTickets(this.id, this.userId, this.busNumber, this.fromStation, this.toStation, this.journeyDate, this.ticketId, this.version);
    }

}
