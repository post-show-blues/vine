package com.post_show_blues.vine.service.meeting;

import com.post_show_blues.vine.domain.bookmark.Bookmark;
import com.post_show_blues.vine.domain.heart.Heart;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.dto.meeting.DetailMeetingDTO;
import com.post_show_blues.vine.dto.meeting.MeetingDTO;
import com.post_show_blues.vine.dto.meeting.MeetingResDTO;
import com.post_show_blues.vine.dto.meetingImg.MeetingImgDTO;
import com.post_show_blues.vine.dto.member.MemberImgDTO;
import com.post_show_blues.vine.dto.member.MemberListDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.dto.participant.ParticipantDTO;
import com.post_show_blues.vine.file.ResultFileStore;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public interface MeetingService {

    Long register(MeetingDTO meetingDTO) throws IOException;

    void modify(MeetingDTO meetingDTO) throws IOException;

    void remove(Long meetingId);

    PageResultDTO<MeetingResDTO, Object[]> getAllMeetingList(PageRequestDTO pageRequestDTO, Long principalId);

    PageResultDTO<MeetingResDTO, Object[]> getFollowMeetingList(PageRequestDTO pageRequestDTO, Long principalId);

    PageResultDTO<MeetingResDTO, Object[]> getBookmarkMeetingList(PageRequestDTO pageRequestDTO, Long principalId);

    DetailMeetingDTO getMeeting(Long meetingId, Long participantId);

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
                                          Member master, MemberImg masterImg,
                                          Integer commentCount, Integer heartCount, Long principalId){

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
                .commentCount(commentCount)
                .heartCount(heartCount)
                .heartState(false)
                .bookmarkState(false)
                .build();

        //현재 사용자 모임 북마크, 하트
        if(principalId != null) {

            //하트 상태 체크
            for (Heart heart : meeting.getHeartList()) {
                if (heart.getMember().getId() == principalId) {
                    meetingResDTO.setHeartState(true);
                    break;
                }
            }

            //북마크 상태 체크
            for (Bookmark bookmark : meeting.getBookmarkList()) {
                if (bookmark.getMember().getId() == principalId) {
                    meetingResDTO.setBookmarkState(true);
                    break;
                }
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

    default DetailMeetingDTO readEntitiesToDTO(Meeting meeting, List<MeetingImg> meetingImgList,
                                               Member master, MemberImg masterImg,
                                               Integer commentCount, Integer heartCount,
                                               List<ParticipantDTO> participantDTOList, Long principalId){

        DetailMeetingDTO detailMeetingDTO = DetailMeetingDTO.builder()
                .meetingId(meeting.getId())
                .category(meeting.getCategory())
                .commentCount(commentCount)
                .heartCount(heartCount)
                .bookmarkState(false)
                .title(meeting.getTitle())
                .text(meeting.getText())
                .place(meeting.getPlace())
                .maxNumber(meeting.getMaxNumber())
                .currentNumber(meeting.getCurrentNumber())
                .meetDate(meeting.getMeetDate())
                .dDay(meeting.getDDay())
                .chatLink(meeting.getChatLink())
                .build();

        //현재 사용자 모임 북마크, 하트
        if(principalId != null) {

            //하트 상태 체크
            for (Heart heart : meeting.getHeartList()) {
                if (heart.getMember().getId() == principalId) {
                    detailMeetingDTO.setHeartState(true);
                    break;
                }
            }

            //북마크 상태 체크
            for (Bookmark bookmark : meeting.getBookmarkList()) {
                if (bookmark.getMember().getId() == principalId) {
                    detailMeetingDTO.setBookmarkState(true);
                    break;
                }
            }
        }

        //모임 사진
        if(meetingImgList != null && meetingImgList.size() > 0){
            List<MeetingImgDTO> meetingImgDTOList = meetingImgList.stream().map(meetingImg -> {
                return MeetingImgDTO.builder()
                        .folderPath(meetingImg.getFolderPath())
                        .storeFileName(meetingImg.getStoreFileName())
                        .build();
            }).collect(Collectors.toList());

            detailMeetingDTO.setImgDTOList(meetingImgDTOList);
        }

        //방장 memberListDTO 생성
        MemberListDTO memberListDTO = MemberListDTO.builder()
                .id(master.getId())
                .nickname(master.getNickname())
                .text(master.getText())
                .build();

        //방장 프로필 사진
        if(masterImg != null){
            MemberImgDTO masterImgDTO = MemberImgDTO.builder()
                    .folderPath(masterImg.getFolderPath())
                    .storeFileName(masterImg.getStoreFileName())
                    .build();

            //memberListDTO 프로필 사진 넣기
            memberListDTO.setMemberImgDTO(masterImgDTO);
        }

        detailMeetingDTO.setMasterDTO(memberListDTO);

        //participantDTO 리스트
        if(participantDTOList.size() > 0){
            detailMeetingDTO.setParticipantDTOList(participantDTOList);
        }

        return detailMeetingDTO;
    }



}
