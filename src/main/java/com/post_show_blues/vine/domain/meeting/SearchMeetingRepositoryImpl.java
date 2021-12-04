package com.post_show_blues.vine.domain.meeting;

import com.post_show_blues.vine.domain.bookmark.Bookmark;
import com.post_show_blues.vine.domain.bookmark.QBookmark;
import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.follow.QFollow;
import com.post_show_blues.vine.domain.meetingimg.QMeetingImg;
import com.post_show_blues.vine.domain.member.QMember;
import com.post_show_blues.vine.domain.memberimg.QMemberImg;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchMeetingRepositoryImpl extends QuerydslRepositorySupport
        implements SearchMeetingRepository {

    public SearchMeetingRepositoryImpl() {
        super(Meeting.class);
    }

    @Override
    public Page<Object[]> searchPage(List<Category> categoryList, String keyword, Long principalId, Pageable pageable) {


        //객체 생성
        QMeeting meeting = QMeeting.meeting;

        QMeetingImg meetingImg = QMeetingImg.meetingImg;

        QMember master = QMember.member;

        QMemberImg masterImg = QMemberImg.memberImg;

        QFollow follow = QFollow.follow;

        //쿼리 작성
        JPQLQuery<Meeting> jpqlQuery = from(meeting);

        //방장
        jpqlQuery.leftJoin(meetingImg).on(meetingImg.meeting.eq(meeting));
        jpqlQuery.leftJoin(master).on(meeting.member.eq(master));
        jpqlQuery.leftJoin(masterImg).on(masterImg.member.eq(master));
        
        if(principalId != null){
            jpqlQuery.leftJoin(follow).on(follow.toMemberId.eq(master));
        }

        //select 문
        JPQLQuery<Tuple> tuple = jpqlQuery.select(meeting, meetingImg.id.min(), master, masterImg,
                meeting.commentList.size(), meeting.heartList.size());

        //where 문
        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression expression = meeting.id.gt(0L);

        builder.and(expression);

        //참가 마감일이 지난 모임은 출력하지 않음
        builder.and(meeting.dDay.goe(0));

        //카테고리 검색
        if(categoryList != null && categoryList.size() > 0){

            BooleanBuilder categoryBuilder = new BooleanBuilder();

            for(Category category : categoryList){

                categoryBuilder.or(meeting.category.eq(category));

            }

            builder.and(categoryBuilder);
        }

        //키워드 검색
        if(keyword != null){
            builder.and(meeting.title.contains(keyword));
        }

        //팔로우한 사람이 방장인 모임만 출력
        if(principalId != null){
            builder.and(follow.fromMemberId.id.eq(principalId));
            }

        tuple.where(builder);

        //order by절(meeting.id.desc());
        Sort sort = pageable.getSort();

        sort.stream().forEach(order ->{
            Order direction = order.isAscending()? Order.ASC : Order.DESC;
            String property = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Meeting.class, "meeting");

            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(property)));
        });

        //group by절
        tuple.groupBy(meeting);

        //page 처리
        tuple.offset(pageable.getOffset()); //RequestDTO.page
        tuple.limit(pageable.getPageSize()); //RequestDTO.size

        log.info(pageable.getOffset());

        List<Tuple> result = tuple.fetch();

        long count = tuple.fetchCount();
        log.info("COUNT: " + count);

        return new PageImpl<Object[]>(
                result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable,
                count
        );
    }

    @Override
    public Page<Object[]> bookmarkPage(Long principalId, Pageable pageable) {

        //객체 생성
        QMeeting meeting = QMeeting.meeting;

        QMeetingImg meetingImg = QMeetingImg.meetingImg;

        QMember member = QMember.member;

        QMemberImg memberImg = QMemberImg.memberImg;

        QBookmark bookmark = QBookmark.bookmark;

        //쿼리 작성
        JPQLQuery<Meeting> jpqlQuery = from(meeting);

        //방장 정보 - left join
        jpqlQuery.leftJoin(meetingImg).on(meetingImg.meeting.eq(meeting));
        jpqlQuery.leftJoin(member).on(member.eq(meeting.member));
        jpqlQuery.leftJoin(memberImg).on(memberImg.member.eq(member));
        jpqlQuery.leftJoin(bookmark).on(bookmark.meeting.eq(meeting));

        //select 문
        JPQLQuery<Tuple> tuple = jpqlQuery.select(meeting, meetingImg.id.min(), member, memberImg,
                meeting.commentList.size(), meeting.heartList.size());

        //where 문
        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression expression = meeting.id.gt(0L);
        builder.and(expression);

        //참가 마감일이 지난 모임은 출력하지 않음
        builder.and(meeting.dDay.goe(0));

        //현재 유저의 북마크 출력
        builder.and(bookmark.member.id.eq(principalId));

        tuple.where(builder);

        //order by절(meeting.id.desc());
        Sort sort = pageable.getSort();

        sort.stream().forEach(order ->{
            Order direction = order.isAscending()? Order.ASC : Order.DESC;
            String property = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Meeting.class, "meeting");

            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(property)));
        });

        //group by절
        tuple.groupBy(meeting);

        //page 처리
        tuple.offset(pageable.getOffset()); //RequestDTO.page
        tuple.limit(pageable.getPageSize()); //RequestDTO.size

        log.info(pageable.getOffset());

        List<Tuple> result = tuple.fetch();

        long count = tuple.fetchCount();
        log.info("COUNT: " + count);

        return new PageImpl<Object[]>(
                result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable,
                count
        );
    }
}
