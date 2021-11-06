package com.post_show_blues.vine.domain.meeting;

import com.post_show_blues.vine.domain.meeting.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long>, SearchMeetingRepository {

    @Query("select m, mti, size(m.commentList), size(m.heartList) from Meeting m " +
            "left join MeetingImg mti on mti.meeting = m " +
            "where m.id = :meetingId")
    List<Object[]> getMeetingWithAll(Long meetingId);

    @Query("select m.member.nickname from Meeting m " +
            "left join Member me on me.id = m.member.id " +
            "where m.id = :meetingId")
    String getNicknameOfMaster(Long meetingId);

    @Query("select m from Meeting m where m.dDay = 0")
    List<Meeting> getDZeroMeeting();

    @Query("select m from Meeting m where m.dDay >= 0")
    List<Meeting> getUpdateMeetingDdayList();

}
