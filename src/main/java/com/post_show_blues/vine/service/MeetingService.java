package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.dto.*;
import com.post_show_blues.vine.dto.meeting.MeetingDTO;
import com.post_show_blues.vine.dto.meetingImg.MeetingImgDTO;
import com.post_show_blues.vine.dto.meetingImg.MeetingImgUploadDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.file.ResultFileStore;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface MeetingService {

    Long register(MeetingDTO meetingDTO) throws IOException;

    void modify(MeetingDTO meetingDTO) throws IOException;

    void remove(Long meetingId);

    PageResultDTO<MeetingDTO, Object[]> getMeetingList(PageRequestDTO pageRequestDTO);

    MeetingDTO getMeeting(Long meetingId);

    Meeting findOne(Long id);

    void updatedDay();

    default Meeting dtoToEntity(MeetingDTO meetingDTO){

        Meeting meeting = Meeting.builder()
                .id(meetingDTO.getMeetingId())
                .member(Member.builder().id(meetingDTO.getMasterId()).build())
                .category(Category.builder().id(meetingDTO.getCategoryId()).build())
                .title(meetingDTO.getTitle())
                .text(meetingDTO.getText())
                .place(meetingDTO.getPlace())
                .maxNumber(meetingDTO.getMaxNumber())
                .currentNumber(meetingDTO.getCurrentNumber())
                .meetDate(meetingDTO.getMeetDate())
                .reqDeadline(meetingDTO.getReqDeadline())
                .dDay(Duration.between(LocalDate.now().atStartOfDay(), meetingDTO.getMeetDate().toLocalDate().atStartOfDay()).toDays())
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

    default MeetingDTO listEntityToDTO(Meeting meeting, MemberImg masterImg, MemberImg participantImg){

        MeetingDTO meetingDTO = MeetingDTO.builder()
                .meetingId(meeting.getId())
                .masterId(meeting.getMember().getId())
                .categoryId(meeting.getCategory().getId())
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

        if(masterImg != null){
            MemberImgDTO masterImgDTO = MemberImgDTO.builder()
                    .filePath(masterImg.getFilePath())
                    .fileName(masterImg.getFileName())
                    .uuid(masterImg.getUuid())
                    .build();

            meetingDTO.setMasterImg(masterImgDTO);
        }

        if(participantImg != null){
            MemberImgDTO participantImgDTO = MemberImgDTO.builder()
                    .filePath(participantImg.getFilePath())
                    .fileName(participantImg.getFileName())
                    .uuid(participantImg.getUuid())
                    .build();

            meetingDTO.setParticipantImg(participantImgDTO);
        }
        return meetingDTO;
    }

    default MeetingDTO readEntitiesToDTO(Meeting meeting, List<MeetingImg> meetingImgList,
                                         Category category){

        MeetingDTO meetingDTO = MeetingDTO.builder()
                .meetingId(meeting.getId())
                .masterId(meeting.getMember().getId())
                .categoryId(category.getId())
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


        meetingDTO.setCategoryName(category.getName());

        return meetingDTO;
    }



}
