package com.post_show_blues.vine.domain.meeting;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.member.QMember;
import com.post_show_blues.vine.domain.memberimg.QMemberImg;
import com.post_show_blues.vine.domain.participant.QParticipant;
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

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchMeetingRepositoryImpl extends QuerydslRepositorySupport
        implements SearchMeetingRepository {

    public SearchMeetingRepositoryImpl() {
        super(Meeting.class);
    }


    @Override
    public Page<Object[]> searchPage(List<Category> categoryList, String keyword, Pageable pageable) {

        log.info("search..............................");

        //객체 생성
        QMeeting meeting = QMeeting.meeting;

        QMember member1 = new QMember("member1");
        QMemberImg memberImg1 = new QMemberImg("memberImg1");


        QMember member2 = new QMember("member2");
        QMemberImg memberImg2 = new QMemberImg("memberImg2");

        log.info("==============================");
        log.info(memberImg1);
        log.info(memberImg2);


        QParticipant participant = QParticipant.participant;

        //쿼리 작성
        JPQLQuery<Meeting> jpqlQuery = from(meeting);

        //방장
        jpqlQuery.leftJoin(member1).on(meeting.member.eq(member1));
        jpqlQuery.leftJoin(memberImg1).on(memberImg1.member.eq(member1));

        //참여자들
        jpqlQuery.leftJoin(participant).on(participant.meeting.eq(meeting));
        jpqlQuery.leftJoin(member2).on(participant.member.eq(member2));
        jpqlQuery.leftJoin(memberImg2).on(memberImg2.member.eq(member2));

        JPQLQuery<Tuple> tuple = jpqlQuery.select( meeting, memberImg1, memberImg2.id.min());

        //where 문
        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression expression = meeting.id.gt(0L);

        builder.and(expression);

        //활동이 지난 모임은 출력하지 않음
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

        log.info("++++++++++++++++++++++++++");
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
