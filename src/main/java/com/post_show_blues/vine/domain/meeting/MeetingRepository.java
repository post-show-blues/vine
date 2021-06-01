package com.post_show_blues.vine.domain.meetingimg;

import com.post_show_blues.vine.domain.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
