package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.participant.Participant;

public interface ParticipantService {

    Long request(Long meetingId, Long memberId);

    Participant findOne(Long id);
}
