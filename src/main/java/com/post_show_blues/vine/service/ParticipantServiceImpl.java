package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipantServiceImpl implements ParticipantService{

    private final ParticipantRepository participantRepository;


    /**
     * id로 참여찾기
     **/
    @Override
    @Transactional(readOnly = true)
    public Participant findOne(Long id) {
        Optional<Participant> result = participantRepository.findById(id);

        Participant participant = result.get();

        return participant;
    }
}
