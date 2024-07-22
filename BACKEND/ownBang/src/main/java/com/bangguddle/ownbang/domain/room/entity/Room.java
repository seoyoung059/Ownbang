package com.bangguddle.ownbang.domain.room.entity;

import com.bangguddle.ownbang.domain.room.enums.DealType;
import com.bangguddle.ownbang.domain.room.enums.RoomType;
import com.bangguddle.ownbang.domain.room.enums.Structure;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

    @Id
    @Column(name = "room_id", nullable = false, columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "agent_id", nullable = false)
//    private User agent;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="room_appliances_id", nullable = false)
    private RoomAppliances roomAppliances;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "room_detail_id", nullable = false)
    private RoomDetail roomDetail;

    @Column(nullable = false)
    private float latitude;

    @Column(nullable = false)
    private float longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DealType dealType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Structure structure;

    @Column(nullable = false)
    private boolean isLoft;

    @Column(nullable = false, columnDefinition = "FLOAT(5,2)")
    private float exclusiveArea;

    @Column(nullable = false, columnDefinition = "FLOAT(5,2)")
    private float supplyArea;

    @Column(nullable = false)
    private byte roomFloor;

    @Column( columnDefinition = "INT UNSIGNED")
    private Long deposit;

    @Column(columnDefinition = "INT UNSIGNED")
    private Long monthlyRent;

    @Column( nullable = false, columnDefinition = "INT UNSIGNED")
    private Long maintenanceFee;

    @Column
    private String parcel;

    @Column(nullable = false)
    private String profileImageUrl;

    @Builder
    public Room(/*User agent, */RoomAppliances roomAppliances, RoomDetail roomDetail, List<RoomImage> roomImages,float latitude, float longitude, DealType dealType, RoomType roomType, Structure structure, boolean isLoft, float exclusiveArea, float supplyArea, byte roomFloor, Long deposit, Long monthlyRent, Long maintenanceFee, String parcel, String profileImageUrl) {
//        this.agent = agent;
        this.roomAppliances = roomAppliances;
        this.roomDetail = roomDetail;
//        this.roomImages = roomImages;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dealType = dealType;
        this.roomType = roomType;
        this.structure = structure;
        this.isLoft = isLoft;
        this.exclusiveArea = exclusiveArea;
        this.supplyArea = supplyArea;
        this.roomFloor = roomFloor;
        this.deposit = deposit;
        this.monthlyRent = monthlyRent;
        this.maintenanceFee = maintenanceFee;
        this.parcel = parcel;
        this.profileImageUrl = profileImageUrl;
    }
}
