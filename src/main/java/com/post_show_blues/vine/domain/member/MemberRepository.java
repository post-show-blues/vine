package com.post_show_blues.vine.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByNickname(String nickname);
    Member findByEmail(String email);
}


