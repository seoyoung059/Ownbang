package com.bangguddle.ownbang.domain.room.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, columnDefinition = "INT UNSIGNED")
    private Room room;

    @Column(nullable = false)
    private String roomImageUrl;

    @Builder
    public RoomImage(Room room, String roomImageUrl) {
        this.room = room;
        this.roomImageUrl = roomImageUrl;
    }
}
