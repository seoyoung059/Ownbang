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

    @Column(nullable = false)
    private boolean refrigerator;

    @Column(nullable = false)
    private boolean washingMachine;

    @Column(nullable = false)
    private boolean airConditioner;

    @Column(nullable = false)
    private boolean bed;

    @Column(nullable = false)
    private boolean desk;

    @Column(nullable = false)
    private boolean microwave;

    @Column(nullable = false)
    private boolean closet;

    @Column(nullable = false)
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
