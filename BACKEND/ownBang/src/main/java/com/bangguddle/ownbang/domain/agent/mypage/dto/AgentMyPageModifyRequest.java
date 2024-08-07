package com.bangguddle.ownbang.domain.agent.mypage.dto;

public record AgentMyPageModifyRequest(
       String officeNumber,
       String officeAddress,
       String greetings,
       String officeName
) { }
