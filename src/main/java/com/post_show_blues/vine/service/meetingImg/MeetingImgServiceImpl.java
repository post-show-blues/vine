package com.post_show_blues.vine.service.meetingImg;

import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import com.post_show_blues.vine.domain.meetingimg.MeetingImgRepository;
import com.post_show_blues.vine.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@RequiredArgsConstructor
@Service
public class MeetingImgServiceImpl implements MeetingImgService{

    private final MeetingImgRepository meetingImgRepository;

    /**
     * 모임사진조회
     */
    @Transactional(readOnly = true)
    @Override
    public MeetingImg findOne(Long meetingImgId) {

        if(meetingImgId == null){
            return null;
        }else{
            return meetingImgRepository.findById(meetingImgId).orElseThrow(() ->
                    new CustomException("존재하지 않은 모임사진입니다."));
        }
    }
}
