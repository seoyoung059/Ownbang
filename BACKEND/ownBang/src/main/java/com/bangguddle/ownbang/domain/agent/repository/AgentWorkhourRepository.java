package com.bangguddle.ownbang.domain.agent.repository;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.entity.AgentWorkhour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentWorkhourRepository extends JpaRepository<AgentWorkhour, Long> {
    AgentWorkhour findByAgentAndDay(Agent agent, AgentWorkhour.Day day);
    List<AgentWorkhour> findByAgent(Agent agent);
}