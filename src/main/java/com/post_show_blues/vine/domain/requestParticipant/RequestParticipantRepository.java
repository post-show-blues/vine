package com.post_show_blues.vine.domain.requestParticipant;

import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.participant.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestParticipantRepository extends JpaRepository<RequestParticipant, Long> {

    @Query("select mi, me, r from RequestParticipant r " +
            "left join Member me on me = r.member " +
            "left join MemberImg mi on mi.member = me " +
            "where r.meeting.id = :meetingId ")
    List<Object[]> getListRequestParticipantByMeetingId(Long meetingId);

    //테스트코드에 사용
    @Query("select count(r.id) from RequestParticipant r where r.meeting.id = :meetingId")
    Long requestParticipantCount(Long meetingId);

    @Modifying(clearAutomatically = true) //쿼리 실행시 JPA 캐싱 clear
    @Query("delete from RequestParticipant r where r.meeting= :meeting")
    void deleteByMeeting(Meeting meeting);

    boolean existsByMeetingAndMember(Meeting meeting, Member member);
}
