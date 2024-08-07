package com.bangguddle.ownbang.domain.agent.workhour.entity;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "agent_workhour", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"agent_id", "day"})
})
public class AgentWorkhour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agent_workhour_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @Column(nullable = false, columnDefinition = "ENUM('WEEKDAY','WEEKEND')")
    @Enumerated(EnumType.STRING)
    private Day day;

    @Column(nullable = false, columnDefinition = "TIME")
    private String startTime;

    @Column(nullable = false, columnDefinition = "TIME")
    private String endTime;


    public enum Day {
        WEEKEND, WEEKDAY
    }

    public void updateWorkhour(String startTime, String endTime) {

        this.startTime = startTime;
        this.endTime = endTime;
    }
}
