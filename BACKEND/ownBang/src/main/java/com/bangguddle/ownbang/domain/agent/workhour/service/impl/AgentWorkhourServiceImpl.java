package com.bangguddle.ownbang.domain.agent.workhour.service.impl;

import com.bangguddle.ownbang.domain.agent.workhour.dto.AgentWorkhourRequest;
import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.workhour.dto.AgentWorkhourResponse;
import com.bangguddle.ownbang.domain.agent.workhour.entity.AgentWorkhour;
import com.bangguddle.ownbang.domain.agent.repository.AgentRepository;
import com.bangguddle.ownbang.domain.agent.workhour.repository.AgentWorkhourRepository;
import com.bangguddle.ownbang.domain.agent.workhour.service.AgentWorkhourService;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;

@Service
@RequiredArgsConstructor
public class AgentWorkhourServiceImpl implements AgentWorkhourService {

    private final AgentWorkhourRepository agentWorkhourRepository;
    private final AgentRepository agentRepository;
    private final UserRepository userRepository;

    private static final String DEFAULT_START_TIME = "09:00";
    private static final String DEFAULT_END_TIME = "18:00";

    /**
     * 중개인 업무시간 생성 메서드
     *
     * @param request 중개인 업무시간 생성 DTO
     * @return SuccessResponse
     * @throws AppException 시작시간이 종료시간보다 늦을 시 WORKHOUR_UNAVAILABLE 발생
     */
    @Override
    @Transactional
    public SuccessResponse<NoneResponse> createAgentWorkhour(Long userId, AgentWorkhourRequest request) {
        User user = userRepository.getById(userId);
        Agent agent = agentRepository.getByUserId(userId);

        String weekdayStartTime = getDefaultIfNull(request.weekdayStartTime(), DEFAULT_START_TIME);
        String weekdayEndTime = getDefaultIfNull(request.weekdayEndTime(), DEFAULT_END_TIME);
        String weekendStartTime = getDefaultIfNull(request.weekendStartTime(), DEFAULT_START_TIME);
        String weekendEndTime = getDefaultIfNull(request.weekendEndTime(), DEFAULT_END_TIME);

        if (!isValidTimeRange(weekdayStartTime, weekdayEndTime) ||
                !isValidTimeRange(weekendStartTime, weekendEndTime)) {
            throw new AppException(WORKHOUR_UNAVAILABLE);
        }

        AgentWorkhour agentWorkhour = AgentWorkhour.builder()
                .agent(agent)
                .weekdayStartTime(weekdayStartTime)
                .weekdayEndTime(weekdayEndTime)
                .weekendStartTime(weekendStartTime)
                .weekendEndTime(weekendEndTime)
                .build();

        agentWorkhourRepository.save(agentWorkhour);

        return new SuccessResponse<>(AGENT_WORKHOUR_CREATE_SUCCESS, NoneResponse.NONE);
    }

    private String getDefaultIfNull(String time, String defaultTime) {
        return time == null ? defaultTime : time;
    }

    /**
     * 중개인 업무시간 조회 메서드
     *
     * @param agentId 업무시간 조회할 중개인 id
     * @return SuccessResponse AgentWorkhourResponse
     * @throws AppException 중개인 id가 유효하지 않을 경우 WORKHOUR_NOT_FOUND 발생
     * @throws AppException 중개인 업무시간 id가 유효하지 않을 경우 WORKHOUR_NOT_FOUND 발생
     */
    @Override
    @Transactional(readOnly = true)
    public SuccessResponse<AgentWorkhourResponse> getAgentWorkhour(Long agentId) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new AppException(WORKHOUR_NOT_FOUND));
        AgentWorkhour agentWorkhour = agentWorkhourRepository.findByAgent(agent)
                .orElseThrow(() -> new AppException(WORKHOUR_NOT_FOUND));
        return new SuccessResponse<>(AGENT_WORKHOUR_GET_SUCCESS, AgentWorkhourResponse.from(agentWorkhour));
    }

    /**
     * 중개인 업무시간 생성 메서드
     *
     * @param request 중개인 업무시간 생성 DTO
     * @return SuccessResponse
     * @throws  AppException 중개인 업무시간이 없을 경우, WORKHOUR_NOT_FOUND 발생
     * @throws AppException 시작시간이 종료시간보다 늦을 시 WORKHOUR_UNAVAILABLE 발생
     */
    @Override
    @Transactional
    public SuccessResponse<NoneResponse> updateAgentWorkhour( Long userId, AgentWorkhourRequest request) {
        User user = userRepository.getById(userId);
        Agent agent = agentRepository.getByUserId(userId);
        Long agentId = agent.getId();

        AgentWorkhour agentWorkhour = agentWorkhourRepository.findByAgent(agent)
                .orElseThrow(() -> new AppException(WORKHOUR_NOT_FOUND));

        if (!isValidTimeRange(request.weekdayStartTime(), request.weekdayEndTime()) ||
                !isValidTimeRange(request.weekendStartTime(), request.weekendEndTime())) {
            throw new AppException(WORKHOUR_UNAVAILABLE);
        }
        agentWorkhour.updateWorkhour(request.weekdayStartTime(), request.weekdayEndTime(), request.weekendStartTime(), request.weekendEndTime());
        agentWorkhourRepository.save(agentWorkhour);

        return new SuccessResponse<>(AGENT_WORKHOUR_UPDATE_SUCCESS, NoneResponse.NONE);
    }
    private boolean isValidTimeRange(String startTime, String endTime) {
        return startTime.compareTo(endTime) < 0;
    }
}