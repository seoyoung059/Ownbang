package com.bangguddle.ownbang.domain.asks.entity;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ask")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ask_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="room_id", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "ask", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AskContent> askContentList;

    @Column(name="last_sent_time")
    private LocalDateTime lastSentTime;

    @Builder
    public Ask(User user, Room room, LocalDateTime lastSentTime) {
        this.user = user;
        this.room = room;
        this.askContentList = new ArrayList<>();
        this.lastSentTime = lastSentTime;
    }
}
