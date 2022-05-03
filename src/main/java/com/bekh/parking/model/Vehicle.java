package com.bekh.parking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_type")
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Column(name = "vehicle_number")
    @NotBlank(message = "Number cannot be empty.")
    private String vehicleNumber;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Column(name = "vehicle_name")
    @NotBlank(message = "Name cannot be empty.")
    private String vehicleName;

    @OneToMany(mappedBy = "vehicle")
    private Set<ParkingLot> parkingLots;
}
