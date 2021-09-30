package com.post_show_blues.vine.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select me, mi from Member me " +
            "left join MemberImg mi on mi.member=me " +
            "where me.nickname like %:keyword% or me.email like %:keyword%")
    List<Object[]> findMemberByNicknameOrEmail(@Param("keyword") String keyword);

    Member findByNickname(String nickname);

    Member findByEmail(String email);

}


