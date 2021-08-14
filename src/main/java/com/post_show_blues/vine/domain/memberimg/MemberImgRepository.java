package com.post_show_blues.vine.domain.memberimg;

import com.post_show_blues.vine.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberImgRepository extends JpaRepository<MemberImg, Long> {
    Optional<MemberImg> findByMember(Member member);
    void deleteByMember(Member member);
}
