package com.bangguddle.ownbang.domain.agent.auth.service;

import com.bangguddle.ownbang.domain.agent.auth.dto.AgentSignUpRequest;
import com.bangguddle.ownbang.domain.agent.repository.AgentRepository;
import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.bangguddle.ownbang.global.enums.ErrorCode.ALREADY_AGENT;
import static com.bangguddle.ownbang.global.enums.ErrorCode.LICENSE_NUMBER_DUPLICATED;
import static com.bangguddle.ownbang.global.enums.SuccessCode.UPGRADE_SUCCESS;

@Service
@RequiredArgsConstructor
public class AgentAuthServiceImpl implements AgentAuthService {

    private final AgentRepository agentRepository;
    private final UserRepository userRepository;
    @Override
    public SuccessResponse<NoneResponse> signUp(Long id, AgentSignUpRequest request) {
        User user = userRepository.getById(id);
        validateByLicenseNumber(request.licenseNumber());
        if(user.isAgent()) throw new AppException(ALREADY_AGENT);
        user.updateIsAgent(true);
        Agent agent = request.toEntity(user);
        agentRepository.save(agent);
        return new SuccessResponse<>(UPGRADE_SUCCESS, NoneResponse.NONE);
    }

    private void validateByLicenseNumber(String licenseNumber) {
        if(agentRepository.existsByLicenseNumber(licenseNumber))
            throw new AppException(LICENSE_NUMBER_DUPLICATED);
    }
}
