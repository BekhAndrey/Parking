package com.bekh.parking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "parking_lot")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parking_type_id", referencedColumnName = "id", nullable = false)
    private ParkingType parkingType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

    @OneToOne(mappedBy = "parkingLot")
    private Order order;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Column(name = "enter_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate enterDate;

    @Column(name = "exit_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate exitDate;
}
