package com.bangguddle.ownbang.domain.agent.workhour.repository;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.workhour.entity.AgentWorkhour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentWorkhourRepository extends JpaRepository<AgentWorkhour, Long> {
    AgentWorkhour findByAgentAndDay(Agent agent, AgentWorkhour.Day day);
    List<AgentWorkhour> findByAgent(Agent agent);
    Optional<AgentWorkhour> findByAgent_IdAndDay(Long agentId, AgentWorkhour.Day day);
}