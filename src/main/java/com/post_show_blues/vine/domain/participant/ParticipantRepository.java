package com.post_show_blues.vine.domain.participant;

import com.post_show_blues.vine.domain.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {


    @Query("select p from Participant p  where p.meeting = :meeting")
    List<Participant> findByMeeting(Meeting meeting);

    @Query("select mi, me, p from Participant p " +
            "left join Member me on me = p.member " +
            "left join MemberImg mi on mi.member = me " +
            "order by p.req desc ")
    List<Object[]> getListParticipantByMeetingId(Long meetingId);

    @Query("select count(p.id) from Participant p where p.meeting.id = :meetingId")
    Long participantCount(Long meetingId);

    @Modifying
    @Query("delete from Participant p where p.meeting= :meeting")
    void deleteByMeeting(Meeting meeting);


    //void deleteByMeeting(Meeting meeting);

}
