package com.bangguddle.ownbang.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 16)
    private String password;

    @Column(nullable = false, length = 11)
    private String phoneNumber;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false)
    private boolean oauthKakao;

    @Column(nullable = false)
    private boolean oauthNaver;

    @Column(nullable = false)
    private boolean oauthGoogle;

    private String profileImageUrl;

    @Column(nullable = false)
    private boolean isAgent;

    @Builder
    public User(String name, String email, String password, String phoneNumber, String nickname) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.oauthGoogle = false;
        this.oauthKakao = false;
        this.oauthNaver = false;
        this.isAgent = false;
    }
}
