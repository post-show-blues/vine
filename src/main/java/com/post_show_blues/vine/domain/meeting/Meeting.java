package com.post_show_blues.vine.domain.meeting;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.post_show_blues.vine.domain.BaseEntity;
import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.comment.Comment;
import com.post_show_blues.vine.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member"})
public class Meeting extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "meeting", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    @Builder.Default
    private String text = "";

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private int maxNumber;

    @Builder.Default
    private int currentNumber=0;

    @Column(nullable = false)
    private LocalDateTime meetDate;

    @Column(nullable = false)
    private LocalDateTime reqDeadline;

    @Column(nullable = false)
    private Long dDay;

    private String chatLink;


    public void changeCategory(Category category){
        this.category = category;
    }

    public void changeMember(Member member){
        this.member = member;
    }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeText(String text){
        this.text = text;
    }

    public void changePlace(String place){
        this.place = place;
    }

    public void changeMaxNumber (int maxNumber){
        if(maxNumber < this.currentNumber){
            throw new IllegalStateException("참여인원 초과입니다.");
        }
        else{
            this.maxNumber = maxNumber;
        }
    }

    public void changeMeetDate (LocalDateTime meetDate){
        this.meetDate = meetDate;
        this.dDay = Duration.between(LocalDate.now().atStartOfDay(), meetDate.toLocalDate().atStartOfDay()).toDays();
    }

    public void changeReqDeadline (LocalDateTime reqDeadline){
        this.reqDeadline = reqDeadline;
    }

    public void changeChatLink (String chatLink){
        this.chatLink = chatLink;
    }

    public void addCurrentNumber(){
        if( (this.currentNumber + 1) > this.maxNumber){
            throw new IllegalStateException("참여인원 초과입니다.");
        }
        else{
            this.currentNumber += 1;
        }
    }

    public void removeCurrentNumber(){
       this.currentNumber -=1;
    }

    public void updateDDay(){
        this.dDay -= 1;
    }

    //연관관계 메서드
    public void addComment(Comment comment){
        this.commentList.add(comment);
        comment.setMeeting(this);
    }

}
