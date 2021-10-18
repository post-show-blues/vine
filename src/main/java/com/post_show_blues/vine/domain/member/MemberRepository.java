package com.post_show_blues.vine.domain.member;

import com.post_show_blues.vine.dto.member.MemberListSQLDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

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


