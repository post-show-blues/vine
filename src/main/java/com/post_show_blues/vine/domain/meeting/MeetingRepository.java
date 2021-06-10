package com.post_show_blues.vine.domain.meeting;

import com.post_show_blues.vine.domain.meeting.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long>, SearchMeetingRepository {

    //TODO 2021.06.05
    // -(D-?)부분 추가
    // meeting.reqDeadline - now() = D-?
    // meeting.reqDeadline -> string, now() -> date 타입
    // -hyeongwoo
    /*@Query("select m , mi1, min(mi2), CURRENT_DATE from Meeting m " +
            "left join Member me1 on me1.id =m.member.id " +
            "left join MemberImg mi1 on mi1.member.id = me1.id " +
            "left join Participant p on p.meeting.id = m.id " +
            "left join Member me2 on me2.id = p.member.id " +
            "left join MemberImg mi2 on mi2.member.id = me2.id " +
            "group by m")
    Page<Object[]> getListPage(Pageable pageable);
    */


    @Query(value = "select mi1.member_img_id, min(mi2.member_img_id), " +
            "m.meeting_id, m.title, m.text, m.meet_date, m.current_number, m.max_number, " +
            "cast(substr(to_date(formatdatetime(now(),'yy/MM/dd'),'yy/MM/dd') - to_date(m.req_deadline,'yyyy-MM-dd'),11,1) as int)" +
            "from meeting m " +
            "left join member me1 on me1.member_id = m.master_id " +
            "left join member_img mi1 on  mi1.member_id = me1.member_id " +
            "left join participant p on p.meeting_id = m.meeting_id " +
            "left join member me2 on me2.member_id = p.member_id " +
            "left join member_img mi2 on mi2.member_id = me2.member_id " +
            "group by m.meeting_id",
            nativeQuery = true)
    Page<Object[]> getListPage(Pageable pageable);


    @Query("select m, mi, c from Meeting m " +
            "left join Category c on m.category = c " +
            "left join MeetingImg mi on mi.meeting = m " +
            "where m.id = :meetingId")
    List<Object[]> getMeetingWithAll(Long meetingId);
}
