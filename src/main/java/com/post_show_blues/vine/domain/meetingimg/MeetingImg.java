package com.post_show_blues.vine.domain.meetingimg;

import com.post_show_blues.vine.domain.meeting.Meeting;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class MeetingImg {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_img_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="meeting_id", nullable = false)
    private Meeting meeting;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

}
