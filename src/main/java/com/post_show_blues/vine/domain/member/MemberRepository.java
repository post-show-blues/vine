package com.post_show_blues.vine.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByNicknameContainingOrEmailContaining(String nickname, String email);
    Member findByNickname(String nickname);
    Member findByEmail(String email);
}


