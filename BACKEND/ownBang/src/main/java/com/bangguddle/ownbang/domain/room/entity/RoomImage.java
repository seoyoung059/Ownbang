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
    @Column(name="room_image_id", nullable = false, columnDefinition = "UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name="room_id", nullable = false, columnDefinition = "UNSIGNED")
    private Room room;

    @Column(name="room_image_url", nullable = false, length = 255)
    private String roomImageUrl;

    @Builder
    public RoomImage(Room room, String roomImageUrl) {
        this.room = room;
        this.roomImageUrl = roomImageUrl;
    }
}
