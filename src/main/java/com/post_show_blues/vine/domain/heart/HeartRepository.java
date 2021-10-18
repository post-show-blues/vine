package com.post_show_blues.vine.domain.heart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    @Modifying
    @Query(value = "INSERT INTO heart (member_id, meeting_id) VALUES (:myId, :meetingId)", nativeQuery = true)
    void heart(@Param("myId") Long myId, @Param("meetingId") Long meetingId);

    @Modifying
    @Query(value = "DELETE FROM heart WHERE member_id=:myId AND meeting_id=:meetingId", nativeQuery = true)
    void unHeart(@Param("myId") Long myId, @Param("meetingId") Long meetingId);
}
