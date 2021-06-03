package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService{

    private final ParticipantRepository participantRepository;

    @Override
    public Participant findOne(Long id) {
        Optional<Participant> result = participantRepository.findById(id);

        Participant participant = result.get();

        return participant;
    }
}
