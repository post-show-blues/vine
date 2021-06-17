package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class FollowService {
    private final FollowRepository followRepository;

    @Transactional
    public void isFollow(long fromMemberId, long toMemberId){
        followRepository.rFollow(fromMemberId, toMemberId);
    }

    @Transactional
    public void isUnFollow(long fromMemberId, long toMemberId){
        followRepository.rUnFollow(fromMemberId, toMemberId);
    }
}
