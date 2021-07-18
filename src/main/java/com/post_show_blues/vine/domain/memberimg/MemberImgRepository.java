package com.post_show_blues.vine.domain.memberimg;

import com.post_show_blues.vine.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberImgRepository extends JpaRepository<MemberImg, Long> {
    MemberImg findByMember(Member member);
}
