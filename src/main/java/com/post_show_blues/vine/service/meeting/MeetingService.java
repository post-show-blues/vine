package com.post_show_blues.vine.service.meeting;

import com.post_show_blues.vine.domain.bookmark.Bookmark;
import com.post_show_blues.vine.domain.comment.Comment;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.dto.*;
import com.post_show_blues.vine.dto.meeting.MeetingDTO;
import com.post_show_blues.vine.dto.meeting.MeetingResDTO;
import com.post_show_blues.vine.dto.meetingImg.MeetingImgDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.file.ResultFileStore;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface MeetingService {

    Long register(MeetingDTO meetingDTO) throws IOException;

    void modify(MeetingDTO meetingDTO) throws IOException;

    void remove(Long meetingId);

    PageResultDTO<MeetingResDTO, Object[]> getAllMeetingList(PageRequestDTO pageRequestDTO, Long principalId);

    PageResultDTO<MeetingResDTO, Object[]> getFollowMeetingList(PageRequestDTO pageRequestDTO, Long principalId);

    PageResultDTO<MeetingResDTO, Object[]> getBookmarkMeetingList(PageRequestDTO pageRequestDTO, Long principalId);

    MeetingDTO getMeeting(Long meetingId);

    Meeting findOne(Long id);

    void updatedDay();

    default Meeting dtoToEntity(MeetingDTO meetingDTO){

        Meeting meeting = Meeting.builder()
                .id(meetingDTO.getMeetingId())
                .member(Member.builder().id(meetingDTO.getMasterId()).build())
                .category(meetingDTO.getCategory())
                .title(meetingDTO.getTitle())
                .text(meetingDTO.getText())
                .place(meetingDTO.getPlace())
                .maxNumber(meetingDTO.getMaxNumber())
                .currentNumber(meetingDTO.getCurrentNumber())
                .meetDate(meetingDTO.getMeetDate())
                .reqDeadline(meetingDTO.getReqDeadline())
                .dDay(Duration.between(LocalDate.now().atStartOfDay(), meetingDTO.getReqDeadline().toLocalDate().atStartOfDay()).toDays())
                .chatLink(meetingDTO.getChatLink())
                .build();

        return meeting;
    }

    default MeetingImg toMeetingImg(Meeting meeting, ResultFileStore resultFileStore){


        MeetingImg meetingImg = MeetingImg.builder()
                .meeting(meeting)
                .folderPath(resultFileStore.getFolderPath())
                .storeFileName(resultFileStore.getStoreFileName())
                .build();

        return meetingImg;

    }

    default MeetingResDTO listEntityToDTO(Meeting meeting, MeetingImg meetingImg,
                                          Member master, MemberImg masterImg, Long principalId){

        MeetingResDTO meetingResDTO = MeetingResDTO.builder()
                .meetingId(meeting.getId())
                .masterId(master.getId())
                .masterNickname(master.getNickname())
                .title(meeting.getTitle())
                .text(meeting.getText())
                .place(meeting.getPlace())
                .maxNumber(meeting.getMaxNumber())
                .currentNumber(meeting.getCurrentNumber())
                .dDay(meeting.getDDay())
                .commentCount(meeting.getCommentList().size())
                .bookmarkState(false)
                .build();

        //현재 사용자 모임 북마크
        for(Bookmark bookmark : meeting.getBookmarkList()){
            if(bookmark.getMember().getId() == principalId){
                meetingResDTO.setBookmarkState(true);
                break;
            }
        }

        //모임 사진
        if(meetingImg != null){
            MeetingImgDTO meetingImgDTO = MeetingImgDTO.builder()
                    .folderPath(meetingImg.getFolderPath())
                    .storeFileName(meetingImg.getStoreFileName())
                    .build();

            meetingResDTO.setMeetingImgDTO(meetingImgDTO);
        }

        //방장 프로필 사진
        if(masterImg != null){
            MemberImgDTO masterImgDTO = MemberImgDTO.builder()
                    .folderPath(masterImg.getFolderPath())
                    .storeFileName(masterImg.getStoreFileName())
                    .build();

            meetingResDTO.setMasterImgDTO(masterImgDTO);
        }

        return meetingResDTO;
    }

    default MeetingDTO readEntitiesToDTO(Meeting meeting, List<MeetingImg> meetingImgList){

        List<Comment> commentList = new ArrayList<>();

        if(meeting.getCommentList() != null && meeting.getCommentList().size() > 0){
            for(Comment comment : meeting.getCommentList()){
                if(comment.getParent() == null){
                    commentList.add(comment);
                }
            }
        }

        MeetingDTO meetingDTO = MeetingDTO.builder()
                .meetingId(meeting.getId())
                .masterId(meeting.getMember().getId())
                .category(meeting.getCategory())
                .commentList(commentList)
                .commentCount(meeting.getCommentList().size())
                .title(meeting.getTitle())
                .text(meeting.getText())
                .place(meeting.getPlace())
                .maxNumber(meeting.getMaxNumber())
                .currentNumber(meeting.getCurrentNumber())
                .meetDate(meeting.getMeetDate())
                .reqDeadline(meeting.getReqDeadline())
                .dDay(meeting.getDDay())
                .chatLink(meeting.getChatLink())
                .regDate(meeting.getRegDate())
                .modDate(meeting.getModDate())
                .build();


        List<MeetingImgDTO> meetingImgDTOList = meetingImgList.stream().map(meetingImg -> {
            return MeetingImgDTO.builder()
                    .folderPath(meetingImg.getFolderPath())
                    .storeFileName(meetingImg.getStoreFileName())
                    .build();
        }).collect(Collectors.toList());

        meetingDTO.setImgDTOList(meetingImgDTOList);

        return meetingDTO;
    }



}
