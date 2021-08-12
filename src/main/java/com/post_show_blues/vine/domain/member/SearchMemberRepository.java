package com.post_show_blues.vine.domain.member;

import com.post_show_blues.vine.domain.follow.Follow;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.dto.MemberImgDTO;
import com.post_show_blues.vine.dto.member.MemberProfileDTO;
import com.post_show_blues.vine.dto.member.MyProfileDTO;
import com.post_show_blues.vine.dto.member.QMyProfileDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.post_show_blues.vine.domain.follow.QFollow.follow;
import static com.post_show_blues.vine.domain.member.QMember.member;
import static com.post_show_blues.vine.domain.memberimg.QMemberImg.memberImg;

@Log4j2
@RequiredArgsConstructor
@Repository
public class SearchMemberRepository{
    private final JPAQueryFactory queryFactory;

    public Optional<MemberProfileDTO> findMemberProfile(Long myId, Long findId) {
        JPAQuery<Tuple> query = queryFactory.select(member, memberImg, follow).from(member)
                .leftJoin(memberImg).on(member.eq(memberImg.member));

        if (myId != null) {
            query=query.leftJoin(follow).on(member.eq(follow.toMemberId).and(follow.fromMemberId.id.eq(myId).and(follow.toMemberId.id.eq(findId))));
        }
        Object[] objects = query.where(member.id.eq(findId)).fetchOne().toArray();

        Member resultMember = (Member)objects[0];
        MemberImg resultMemberImg = (MemberImg)objects[1];
        Follow resultFollow = (Follow)objects[2];
        MemberProfileDTO memberProfileDTO=new MemberProfileDTO();

        if(resultMemberImg!=null){
            memberProfileDTO.setMemberImgDTO(new MemberImgDTO(resultMemberImg.getFolderPath(), resultMemberImg.getStoreFileName()));

        }
        memberProfileDTO.setInstaurl(resultMember.getInstaurl());
        memberProfileDTO.setTwitterurl(resultMember.getTwitterurl());
        memberProfileDTO.setNickname(resultMember.getNickname());
        memberProfileDTO.setText(resultMember.getText());

        if(resultFollow!=null){
            memberProfileDTO.setIsFollow(true);
        }else{
            memberProfileDTO.setIsFollow(false);
        }

        return Optional.ofNullable(memberProfileDTO);
    }

    public MyProfileDTO findMyProfile(Long id){
        MyProfileDTO result = queryFactory.select(new QMyProfileDTO(member.id, member.nickname, member.text, member.instaurl, member.twitterurl,
                         memberImg.folderPath, memberImg.storeFileName)).from(member)
                .leftJoin(memberImg).on(memberImg.member.eq(member))
                .where(member.id.eq(id)).fetchOne();

        return result;
    }
}
