package com.bangguddle.ownbang.domain.room.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "room_appliances")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomAppliances {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_appliances_id", nullable = false, columnDefinition = "UNSIGNED")
    private Long id;

    @Column(name = "refrigerator", nullable = false)
    private boolean refrigerator;

    @Column(name = "washing_machine", nullable = false)
    private boolean washingMachine;

    @Column(name = "air_conditioner", nullable = false)
    private boolean airConditioner;

    @Column(name = "bed", nullable = false)
    private boolean bed;

    @Column(name = "desk", nullable = false)
    private boolean desk;

    @Column(name = "microwave", nullable = false)
    private boolean microwave;

    @Column(name = "closet", nullable = false)
    private boolean closet;

    @Column(name = "chair", nullable = false)
    private boolean chair;

    @Builder
    public RoomAppliances(boolean refrigerator, boolean washingMachine, boolean airConditioner, boolean bed, boolean desk, boolean microwave, boolean closet, boolean chair) {
        this.refrigerator = refrigerator;
        this.washingMachine = washingMachine;
        this.airConditioner = airConditioner;
        this.bed = bed;
        this.desk = desk;
        this.microwave = microwave;
        this.closet = closet;
        this.chair = chair;
    }
}
