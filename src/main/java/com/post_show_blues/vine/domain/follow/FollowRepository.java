package com.post_show_blues.vine.domain.follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Modifying //INSERT, DELETE, UPDATE 를 네이티브 쿼리로 작성하려면 해당 어노테이션 필요
    @Query(value = "INSERT INTO follow (from_member_id, to_member_id) VALUES (:fromMemberId, :toMemberId)", nativeQuery = true)
    void rFollow(@Param("fromMemberId") long fromMemberId, @Param("toMemberId") long toMemberId);

    @Modifying
    @Query(value = "DELETE FROM follow WHERE from_member_id=:fromMemberId AND to_member_id=:toMemberId", nativeQuery = true)
    void rUnFollow(@Param("fromMemberId") long fromMemberId, @Param("toMemberId") long toMemberId);

    @Query("select me, mi from Follow f " +
            "left join Member me on me=f.toMemberId " +
            "left join MemberImg mi on mi.member = me  " +
            "where f.fromMemberId.id=:fromMemberId")
    List<Object[]> findFollowMembers(@Param("fromMemberId") long fromMemberId);

    @Query("select me, mi from Follow f " +
            "left join Member me on me=f.fromMemberId " +
            "left join MemberImg mi on mi.member = me  " +
            "where f.toMemberId.id=:toMemberId ")
    List<Object[]> findFollowerMembers(@Param("toMemberId") long toMemberId);

    @Query(value = "select count(*)>0 from Follow f " +
            "where f.from_member_id=:fromMemberId " +
            "and f.to_member_id=:toMemberId", nativeQuery = true)
    Boolean existsByFromMemberIdAndToMemberId(@Param("fromMemberId") long fromMemberId, @Param("toMemberId") long toMemberId);
}
