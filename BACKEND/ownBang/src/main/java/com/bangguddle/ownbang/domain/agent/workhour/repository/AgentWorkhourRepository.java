package com.bangguddle.ownbang.domain.agent.workhour.repository;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.workhour.entity.AgentWorkhour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentWorkhourRepository extends JpaRepository<AgentWorkhour, Long> {

    Optional<AgentWorkhour> findByAgent(Agent agent);

}