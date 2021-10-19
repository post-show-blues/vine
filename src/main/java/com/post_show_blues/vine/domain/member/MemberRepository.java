package com.post_show_blues.vine.domain.member;

import com.post_show_blues.vine.dto.member.MemberListSQLDTO;
import com.post_show_blues.vine.dto.member.ProfileMeetingSQLDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "select me.meeting_id, me.title, me.text, me.max_number, me.current_number, me.d_day, " +
            "            mi.folder_path folder_path1, mi.store_file_name store_file_name1, p.folder_path folder_path2, p.store_file_name store_file_name2 " +
            "from meeting me " +
            "left join member_img mi on me.master_id=mi.member_id " +
            "left join ( " +
            "   select mip.member_id, mip.folder_path, mip.store_file_name, p.meeting_id  from " +
            "       (select p.member_id, p.meeting_id, row_number() over (partition by meeting_id) num from participant p) p " +
            "   left join member_img mip on mip.member_id=p.member_id where num=1) p on p.meeting_id=me.meeting_id " +
            "left join participant on me.meeting_id=participant.meeting_id where participant.member_id=:id or me.master_id=:id "
            , nativeQuery = true)
    List<ProfileMeetingSQLDTO> findParticipantMeeting(@Param("id") Long id);

    @Query(value = "select me.*, mi.folder_path, mi.store_file_name, following, follower from member me " +

            "left join member_img mi on mi.member_id=me.member_id " +

            "left join (select from_member_id,count(*) following from follow " +
            "   group by from_member_id) following on me.member_id=following.from_member_id " +

            "left join (select to_member_id,count(*) follower from follow follower " +
            "   group by to_member_id) follower on me.member_id=follower.to_member_id " +

            "where me.nickname like %:keyword%", nativeQuery = true)
    List<MemberListSQLDTO> findMemberByNickname(@Param("keyword") String keyword);

    Optional<Member> findByNickname(String nickname);

    @Query("select me, mi from Member me " +
            "left join MemberImg mi on mi.member=me " +
            "where me.id=:id")
    List<Object[]> findMemberAndMemberImgById(@Param("id") Long id);

    Optional<Member> findByEmail(String email);

}


