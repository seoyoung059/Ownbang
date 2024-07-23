package com.bangguddle.ownbang.domain.user.repository;

import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.handler.AppException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(final String email);

    Optional<User> findByPhoneNumber(final String phoneNumber);

    default void validateByEmail(final String email) {
        if(findByEmail(email).isPresent())
            throw new AppException(ErrorCode.EMAIL_DUPLICATED);
    }

    default void validateByPhoneNumber(final String phoneNumber) {
        if(findByPhoneNumber(phoneNumber).isPresent())
            throw new AppException(ErrorCode.PHONE_NUMBER_DUPLICATED);
    }
}
