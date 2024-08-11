package com.bangguddle.ownbang.domain.bookmark.repository;

import com.bangguddle.ownbang.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByUserId(long userId);

    Optional<Bookmark> findBookmarkByRoomIdAndUserId(long roomId, long userId);
}
