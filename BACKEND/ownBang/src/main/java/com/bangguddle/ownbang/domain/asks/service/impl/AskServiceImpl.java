package com.bangguddle.ownbang.domain.asks.service.impl;

import com.bangguddle.ownbang.domain.asks.dto.AskContentCreateRequest;
import com.bangguddle.ownbang.domain.asks.entity.Ask;
import com.bangguddle.ownbang.domain.asks.repository.AskContentRepository;
import com.bangguddle.ownbang.domain.asks.repository.AskRepository;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.repository.RoomRepository;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.bangguddle.ownbang.global.enums.ErrorCode.ACCESS_DENIED;
import static com.bangguddle.ownbang.global.enums.ErrorCode.ASK_DUPLICATED;
import static com.bangguddle.ownbang.global.enums.SuccessCode.ASK_CONTENT_CREATE_SUCCESS;
import static com.bangguddle.ownbang.global.enums.SuccessCode.ASK_CREATE_SUCCESS;

@Service
@RequiredArgsConstructor
public class AskServiceImpl implements com.bangguddle.ownbang.domain.asks.service.AskService {

    private final AskRepository askRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final AskContentRepository askContentRepository;

    /**
     * 임차인이 문의 대화방을 만드는 메서드
     * @param userId 임차인 ID
     * @param roomId 임차인이 문의 할 매물의 ID
     * @return
     */
    @Override
    public SuccessResponse<NoneResponse> createAsk(Long userId, Long roomId) {
        if (askRepository.existsByUserIdAndRoomId(userId, roomId)) throw new AppException(ASK_DUPLICATED);
        Room room = roomRepository.getById(roomId);
        User user = userRepository.getById(userId);
        System.out.println("room = " + room);
        System.out.println("user = " + user);

        askRepository.save(Ask.builder()
                .room(room)
                .user(user)
                .lastSentTime(LocalDateTime.now())
                .build()
        );

        return new SuccessResponse<>(ASK_CREATE_SUCCESS, NoneResponse.NONE);
    }

    /**
     * 문의 내용을 생성하는 메서드
     * @param userId 임차인/중개인 상관없이 문의 내용을 올린 사람
     * @param request AskContentCreateRequest 문의 내용
     * @return
     */
    @Override
    public SuccessResponse<NoneResponse> createAskContent(Long userId, AskContentCreateRequest request) {
        Ask ask = askRepository.getById(request.askId());
        User user = userRepository.getById(userId);
        isAgent(userId, ask);

        askContentRepository.save(request.toEntity(ask, user, LocalDateTime.now()));
        return new SuccessResponse<>(ASK_CONTENT_CREATE_SUCCESS, NoneResponse.NONE);
    }

//    public SuccessResponse<List<AskContentSearchResponse>> getAskContent(Long userId, Long askId){
//        Ask ask = askRepository.getById(askId);
//        User user = userRepository.getById(userId);
//
//
//    }




    private boolean isAgent (Long userId, Ask ask) {
        if (ask.getUser().getId().equals(userId)) return false;
//        else if (ask.getRoom().getAgent()/*.getUser()*/.getId().equals(userId)) return true;
        else throw new AppException(ACCESS_DENIED);
    }



}
