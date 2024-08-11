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

    Optional<User> findById(final Long id);

    default User getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
