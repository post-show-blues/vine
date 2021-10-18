package com.post_show_blues.vine.service.heart;

import com.post_show_blues.vine.domain.heart.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HeartService {
    private final HeartRepository heartRepository;

    @Transactional()
    public void heart(Long myId, Long meetingId) {
        heartRepository.heart(myId, meetingId);
    }

    @Transactional()
    public void unheart(Long myId, Long meetingId) {
        heartRepository.unHeart(myId, meetingId);
    }
}
