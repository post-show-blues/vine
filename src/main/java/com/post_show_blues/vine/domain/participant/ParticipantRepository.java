package com.post_show_blues.vine.domain.participant;

import com.post_show_blues.vine.domain.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;


import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query("select p from Participant p  where p.meeting = :meeting")
    List<Participant> findByMeeting(Meeting meeting);


    @Modifying
    @Query("delete from Participant p  where p.meeting = :meeting")
    void deleteByMeeting(Meeting meeting);


    //void deleteByMeeting(Meeting meeting);

}
