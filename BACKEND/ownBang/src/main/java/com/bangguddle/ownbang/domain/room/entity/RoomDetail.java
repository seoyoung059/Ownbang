package com.bangguddle.ownbang.domain.room.entity;

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
    @Column(name="room_detail_id", nullable = false, columnDefinition = "UNSIGNED")
    private Long id;


    @Column(name = "room_count", nullable = false)
    private byte roomCount;

    @Column(name = "bathroom_count", nullable = false)
    private byte bathroomCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "heating_type", nullable = false)
    private HeatingType heatingType;

    @Column(name = "movein_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date moveInDate;

    @Column(name = "building_floor", nullable = false, columnDefinition = "UNSIGNED")
    private Long buildingFloor;

    @Column(name = "elevator", nullable = false)
    private boolean elevator;

    @Column(name = "total_parking", nullable = false, columnDefinition = "UNSIGNED")
    private Long totalParking;

    @Column(name = "parking", nullable = false)
    private float parking;

    @Column(name = "approval_date")
    @Temporal(TemporalType.DATE)
    private Date approvalDate;

    @Column(name = "first_registration_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date firstRegistrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "facing", nullable = false)
    private Facing facing;

    @Enumerated(EnumType.STRING)
    @Column(name = "purpose")
    private Purpose purpose;

    @Column(name = "road")
    private String road;

    @Column(name = "detail_address")
    private String detailAddress;

    @Builder
    public RoomDetail(byte roomCount, byte bathroomCount, HeatingType heatingType, Date moveInDate, Long buildingFloor, boolean elevator, Long totalParking, float parking, Date approvalDate, Date firstRegistrationDate, Facing facing, Purpose purpose, String road, String detailAddress) {
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
        this.road = road;
        this.detailAddress = detailAddress;
    }
}

