package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.participant.Participant;

public interface ParticipantService {

    Long request(Long meetingId, Long memberId);

    void accept(Long participantId);

    void reject(Long participantId);

    void remove(Long participantId, Long memberId); //2번째 파라미터 = 현재 로그인 id (알림구분 용도)

    Participant findOne(Long id);
}
