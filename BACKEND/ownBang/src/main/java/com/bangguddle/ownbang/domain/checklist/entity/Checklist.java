package com.bangguddle.ownbang.domain.checklist.entity;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Checklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklist_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name="reservation_id")
    private Reservation reservation;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column(nullable = false)
    private Boolean isTemplate;

    @Builder
    public Checklist(User user, Reservation reservation, String title,
                     String contents, Boolean isTemplate){
        this.user = user;
        this.reservation = reservation;
        this.title = title;
        this.contents = contents;
        this.isTemplate = isTemplate;
    }

    public void update(String title, String contents){
        this.title = title;
        this.contents = contents;
    }
}