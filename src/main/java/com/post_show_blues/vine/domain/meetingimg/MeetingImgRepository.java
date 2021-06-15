package com.post_show_blues.vine.domain.meetingimg;

import com.post_show_blues.vine.domain.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeetingImgRepository extends JpaRepository<MeetingImg, Long> {

    @Query("select mi from MeetingImg  mi where mi.meeting = :meeting")
    List<MeetingImg> findByMeeting(Meeting meeting);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from MeetingImg mi where mi.meeting = :meeting")
    void deleteByMeeting(@Param("meeting") Meeting meeting);

}
