package com.post_show_blues.vine.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select me, mi from Member me " +
            "left join MemberImg mi on mi.member=me " +
            "where me.nickname like %:keyword%")
    List<Object[]> findMemberByNickname(@Param("keyword") String keyword);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByEmail(String email);

}


