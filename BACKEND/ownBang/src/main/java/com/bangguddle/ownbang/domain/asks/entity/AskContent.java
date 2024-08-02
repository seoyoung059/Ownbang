package com.bangguddle.ownbang.domain.asks.entity;

import com.bangguddle.ownbang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="ask_content")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AskContent {

    @Id
    @Column(name="ask_content_id", nullable = false, columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="ask_id", nullable = false)
    private Ask ask;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User sender;

    @Column(name="content",nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name="sent_time", nullable = false)
    private LocalDateTime sentTime;

    @Column(name="read", nullable = false)
    private Boolean read;

    @Builder
    public AskContent(User sender, Ask ask, String content, LocalDateTime sentTime, Boolean read) {
        this.sender = sender;
        this.ask = ask;
        this.content = content;
        this.sentTime = sentTime;
        this.read = read;
    }
}
