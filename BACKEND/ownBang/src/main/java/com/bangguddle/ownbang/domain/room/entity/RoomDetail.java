package com.bangguddle.ownbang.domain.room.entity;

import com.bangguddle.ownbang.domain.room.dto.RoomDetailUpdateRequest;
import com.bangguddle.ownbang.domain.room.enums.Facing;
import com.bangguddle.ownbang.domain.room.enums.HeatingType;
import com.bangguddle.ownbang.domain.room.enums.Purpose;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "room_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_detail_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;


    @Column(nullable = false)
    private byte roomCount;

    @Column(nullable = false)
    private byte bathroomCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HeatingType heatingType;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date moveInDate;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long buildingFloor;

    @Column(nullable = false)
    private boolean elevator;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long totalParking;

    @Column(nullable = false)
    private float parking;

    @Column
    @Temporal(TemporalType.DATE)
    private Date approvalDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date firstRegistrationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Facing facing;

    @Enumerated(EnumType.STRING)
    @Column
    private Purpose purpose;


    @Builder
    public RoomDetail(byte roomCount, byte bathroomCount, HeatingType heatingType, Date moveInDate, Long buildingFloor, boolean elevator, Long totalParking, float parking, Date approvalDate, Date firstRegistrationDate, Facing facing, Purpose purpose) {
        this.roomCount = roomCount;
        this.bathroomCount = bathroomCount;
        this.heatingType = heatingType;
        this.moveInDate = moveInDate;
        this.buildingFloor = buildingFloor;
        this.elevator = elevator;
        this.totalParking = totalParking;
        this.parking = parking;
        this.approvalDate = approvalDate;
        this.firstRegistrationDate = firstRegistrationDate;
        this.facing = facing;
        this.purpose = purpose;
    }


    public void updateFromDto(RoomDetailUpdateRequest roomDetailUpdateRequest) {
        this.roomCount = roomDetailUpdateRequest.roomCount();
        this.bathroomCount = roomDetailUpdateRequest.bathroomCount();
        this.heatingType = roomDetailUpdateRequest.heatingType();
        this.moveInDate = roomDetailUpdateRequest.moveInDate();
        this.buildingFloor = roomDetailUpdateRequest.buildingFloor();
        this.elevator = roomDetailUpdateRequest.elevator();
        this.totalParking = roomDetailUpdateRequest.totalParking();
        this.parking = roomDetailUpdateRequest.parking();
        this.approvalDate = roomDetailUpdateRequest.approvalDate();
        this.firstRegistrationDate = roomDetailUpdateRequest.firstRegistrationDate();
        this.facing = roomDetailUpdateRequest.facing();
        this.purpose = roomDetailUpdateRequest.purpose();
    }
}

