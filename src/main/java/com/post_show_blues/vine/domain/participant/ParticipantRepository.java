package com.post_show_blues.vine.domain.participant;

import com.post_show_blues.vine.domain.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query("select p from Participant p  where p.meeting = :meeting")
    List<Participant> findByMeeting(Meeting meeting);

    @Query("select mi, me, p from Participant p " +
            "left join Member me on me = p.member " +
            "left join MemberImg mi on mi.member = me " +
            "where p.meeting.id = :meetingId ")
    List<Object[]> getListParticipantByMeetingId(Long meetingId);

    //테스트코드에 사용
    @Query("select count(p.id) from Participant p where p.meeting.id = :meetingId")
    Long participantCount(Long meetingId);

    @Modifying(clearAutomatically = true) //쿼리 실행시 JPA 캐싱 clear
    @Query("delete from Participant p where p.meeting= :meeting")
    void deleteByMeeting(Meeting meeting);

    //requestParticipantServiceImpl 테스트코드에 사용
    @Query("select p from Participant p where p.meeting.id = :meetingId and " +
            "p.member.id = :memberId")
    Optional<Participant> findByMeetingIdMemberId(@Param("meetingId") Long meetingId,
                                                  @Param("memberId") Long memberId);

    @Query("select m.nickname from Participant p " +
            "left join Member m on m.id = p.member.id " +
            "where p.id = :participantId")
    String getNicknameOfParticipant(Long participantId);


}
