package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.dto.*;
import lombok.Builder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MeetingService {

    Long register(MeetingDTO meetingDTO);

    void modify(MeetingDTO meetingDTO);

    void remove(Long meetingId);

    PageResultDTO<MeetingDTO, Object[]> getMeetingList(PageRequestDTO pageRequestDTO);

    MeetingDTO getMeeting(Long meetingId);

    Meeting findOne(Long id);

    default Map<String, Object> dtoToEntity(MeetingDTO meetingDTO){

        Map<String, Object> entityMap = new HashMap<>();

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
                .dDay(Period.between(LocalDate.now(), meetingDTO.getMeetDate().toLocalDate()).getDays())
                .chatLink(meetingDTO.getChatLink())
                .build();

        entityMap.put("meeting", meeting);


        List<MeetingImg> meetingImgList = getImgDtoToEntity(meetingDTO, meeting);

        entityMap.put("meetingImgList", meetingImgList);

        return entityMap;
    }

    default List<MeetingImg> getImgDtoToEntity(MeetingDTO meetingDTO,Meeting meeting){

        List<MeetingImgDTO> imgDTOList = meetingDTO.getImgDTOList();

        // 사진 첨부 유무 확인
        if (imgDTOList != null && imgDTOList.size() > 0) {
            List<MeetingImg> meetingImgList = imgDTOList.stream().map(meetingImgDTO -> {
                MeetingImg meetingImg = MeetingImg.builder()
                        .uuid(meetingImgDTO.getUuid())
                        .fileName(meetingImgDTO.getFileName())
                        .filePath(meetingImgDTO.getFilePath())
                        .meeting(meeting)
                        .build();
                return meetingImg;
            }).collect(Collectors.toList());

            return meetingImgList;
        }

        return null;
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
                    .filePath(meetingImg.getFilePath())
                    .fileName(meetingImg.getFileName())
                    .uuid(meetingImg.getUuid())
                    .build();
        }).collect(Collectors.toList());

        meetingDTO.setImgDTOList(meetingImgDTOList);


        meetingDTO.setCategoryName(category.getName());

        return meetingDTO;
    }



}
