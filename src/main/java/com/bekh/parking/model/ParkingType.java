package com.bekh.parking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "parking_type")
public class ParkingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @Column(name = "lots_amount")
    private Integer lotsAmount;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @OneToMany(mappedBy = "parkingType")
    private Set<ParkingLot> parkingLots;
}
