package com.bangguddle.ownbang.domain.agent.workhour.entity;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class AgentWorkhour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agent_workhour_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @Column(name = "weekday_start_time",nullable = false, columnDefinition = "TIME")
    private String weekdayStartTime;

    @Column(name = "weekday_end_time", nullable = false, columnDefinition = "TIME")
    private String weekdayEndTime;

    @Column(name = "weekend_start_time",nullable = false, columnDefinition = "TIME")
    private String weekendStartTime;

    @Column(name = "weekend_end_time",nullable = false, columnDefinition = "TIME")
    private String weekendEndTime;

    public void updateWorkhour(String weekdayStartTime, String weekdayEndTime, String weekendStartTime, String weekendEndTime) {

        this.weekdayStartTime = weekdayStartTime;
        this.weekdayEndTime = weekdayEndTime;
        this.weekendStartTime = weekendStartTime;
        this.weekendEndTime = weekendEndTime;
    }
}
