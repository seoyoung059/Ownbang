package com.bangguddle.ownbang.domain.bookmark.entity;

import com.bangguddle.ownbang.domain.room.entity.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="bookmark")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

    @Id
    @Column(name="bookmark_id", nullable = false, columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name="user_id", nullable = false)
//    private User user;

    @ManyToOne
    @JoinColumn(name="room_id", nullable = false)
    private Room room;

    @Builder
    public Bookmark(/*User user*/ Room room) {
//        this.user = user;
        this.room = room;
    }
}
