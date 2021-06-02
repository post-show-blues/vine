package com.post_show_blues.vine.domain.meetingimg;

import com.post_show_blues.vine.domain.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MeetingImgRepository extends JpaRepository<MeetingImg, Long> {

    //TODO 2021.06.02.-서버 컴퓨터에 저장되어 있는 사진도 실제로 제거해야하나?- hyeongwoo
    @Modifying
    @Query("delete from MeetingImg mi where mi.meeting = :meeting")
    void deleteByMeeting(@Param("meeting") Meeting meeting);
}
