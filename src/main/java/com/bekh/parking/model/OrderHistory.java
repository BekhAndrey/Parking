package com.bekh.parking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_history")
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "parking_lot_id")
    private Long parkingLotId;

    @Column(name = "enter_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate enterDate;

    @Column(name = "exit_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate exitDate;

    @Column(name = "vehicle_type")
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Column(name = "vehicle_number")
    @NotBlank(message = "Number cannot be empty.")
    private String vehicleNumber;

    private String price;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Enumerated(EnumType.STRING)
    private Status status;
}
