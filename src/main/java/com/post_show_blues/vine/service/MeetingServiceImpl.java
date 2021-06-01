package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.dto.RoomDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService{

    private final MeetingRepository meetingRepository;

    @Override
    public Long register(RoomDTO roomDTO) {
        return null;
    }
}
