package com.bangguddle.ownbang.domain.user.repository;

import com.bangguddle.ownbang.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(final String email);

    Optional<User> findByPhoneNumber(final String phoneNumber);
}
