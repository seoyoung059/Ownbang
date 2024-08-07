package com.bangguddle.ownbang.domain.agent.entity;

import com.bangguddle.ownbang.domain.agent.mypage.dto.AgentMyPageModifyRequest;
import com.bangguddle.ownbang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agent_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(length = 11)
    private String officeNumber;

    @Column(nullable = false, length = 20)
    private String licenseNumber;

    @Column(nullable = false)
    private String officeAddress;

    private String greetings;

    @Column(nullable = false)
    private String officeName;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Agent(String officeNumber, String licenseNumber, String officeAddress,
                 String greetings, String officeName, User user) {
        this.officeNumber = officeNumber;
        this.licenseNumber = licenseNumber;
        this.officeAddress = officeAddress;
        this.greetings = greetings;
        this.officeName = officeName;
        this.user = user;
    }

    public void updateAgent(AgentMyPageModifyRequest request) {
        this.officeNumber = request.officeNumber();
        this.officeAddress = request.officeAddress();
        this.greetings = request.greetings();
        this.officeName = request.officeName();
    }
}
