package com.bangguddle.ownbang.domain.webrtc.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebrtcToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "webrtc_token_id", nullable = false, updatable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "webrtc_id", nullable = false)
    private Webrtc webrtc;

    @Column(nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Builder
    public WebrtcToken(Webrtc webrtc, String token, UserRole userRole){
        this.webrtc = webrtc;
        this.token = token;
        this.userRole = userRole;
    }
}
