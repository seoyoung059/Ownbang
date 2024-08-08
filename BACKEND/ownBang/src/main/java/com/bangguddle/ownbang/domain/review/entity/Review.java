package com.bangguddle.ownbang.domain.review.entity;


import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor
public class Review {

    @Id
    @Column(name = "review_id", nullable = false, columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name="agent_id", nullable = false)
    private Agent agent;

    @Column(name = "star_rating", nullable = false, columnDefinition = "TINYINT(5)")
    private int starRating;

    @Column(name="content")
    private String content;

    @Builder
    public Review(Reservation reservation, Agent agent, int starRating, String content) {
        this.reservation = reservation;
        this.agent = agent;
        this.starRating = starRating;
        this.content = content;
    }


}
