package com.post_show_blues.vine.domain.member;

import com.post_show_blues.vine.domain.follow.QFollow;
import com.post_show_blues.vine.dto.member.MemberProfileDTO;
import com.post_show_blues.vine.dto.member.MyProfileDTO;
import com.post_show_blues.vine.dto.member.QMemberProfileDTO;
import com.post_show_blues.vine.dto.member.QMyProfileDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.post_show_blues.vine.domain.follow.QFollow.follow;
import static com.post_show_blues.vine.domain.member.QMember.member;
import static com.post_show_blues.vine.domain.memberimg.QMemberImg.memberImg;

@Log4j2
@RequiredArgsConstructor
@Repository
public class SearchMemberRepository {
    private final JPAQueryFactory queryFactory;
    QFollow following = new QFollow("following");
    QFollow follower = new QFollow("follower");

    public Optional<MemberProfileDTO> findMemberProfile(Long myId, Long findId) {
        MemberProfileDTO result = queryFactory.select(new QMemberProfileDTO(member.id, member.email, member.nickname, member.text, member.instaurl, member.facebookurl,
                        memberImg.folderPath, memberImg.storeFileName, following.count(), follower.count(), follow.isNotNull())).from(member)
                .leftJoin(memberImg).on(memberImg.member.eq(member))
                .leftJoin(following).on(following.fromMemberId.eq(member))
                .leftJoin(follower).on(follower.toMemberId.eq(member))
                .leftJoin(follow).on(follow.fromMemberId.id.eq(myId).and(follow.toMemberId.id.eq(findId)))
                .where(member.id.eq(findId)).fetchOne();
        return Optional.ofNullable(result);
    }

    public MyProfileDTO findMyProfile(Long id) {
        MyProfileDTO result = queryFactory.select(new QMyProfileDTO(member.id, member.email, member.nickname, member.text, member.instaurl, member.facebookurl,
                        memberImg.folderPath, memberImg.storeFileName, following.count(), follower.count())).from(member)
                .leftJoin(memberImg).on(memberImg.member.eq(member))
                .leftJoin(following).on(following.fromMemberId.eq(member))
                .leftJoin(follower).on(follower.toMemberId.eq(member))
                .where(member.id.eq(id)).fetchOne();

        return result;
    }
}
