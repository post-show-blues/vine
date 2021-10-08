package com.post_show_blues.vine.domain.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from Bookmark b where b.meeting.id = :meetingId and b.member.id = :principalId")
    void deleteBookmark(Long meetingId, Long principalId);

}
