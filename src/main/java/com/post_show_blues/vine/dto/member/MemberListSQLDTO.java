package com.post_show_blues.vine.dto.member;

public interface MemberListSQLDTO {
    Long getMember_Id();

    String getEmail();

    String getNickname();

    String getText();

    String getFolder_Path();

    String getStore_File_Name();

    Long getFollowing();

    Long getFollower();

}
