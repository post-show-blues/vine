package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import com.post_show_blues.vine.domain.meetingimg.MeetingImgRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import com.post_show_blues.vine.dto.MeetingDTO;
import com.post_show_blues.vine.dto.MeetingImgDTO;
import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class MeetingServiceImpl implements MeetingService{

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    private final MeetingRepository meetingRepository;
    private final MeetingImgRepository meetingImgRepository;
    private final NoticeRepository noticeRepository;
    private final ParticipantRepository participantRepository;

    /**
     * 모임등록
     */
    @Override
    public Long register(MeetingDTO meetingDTO) {

        //활동날짜, 신청 마감날짜 비교교

       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");

        String meetDate = meetingDTO.getMeetDate();
        String reqDeadline = meetingDTO.getReqDeadline();

        try{
            Date meetDateType = dateFormat.parse(meetDate);
            Date deadlineDateType = dateFormat.parse(reqDeadline);

            if(meetDateType.before(deadlineDateType)){
                throw new IllegalStateException("활동일이 신청마감일보다 빠릅니다.");
            }
        }catch (ParseException e){
            e.printStackTrace();
            throw new IllegalStateException("형식에 맞게 입력해주세요.");

        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalStateException("활동일이 신청마감일보다 빠릅니다.");
        }



        Map<String, Object> result = dtoToEntity(meetingDTO);

        Meeting meeting = (Meeting) result.get("meeting");

        meetingRepository.save(meeting);

        List<MeetingImg> meetingImgList = (List<MeetingImg>) result.get("meetingImgList");

        // 사진 첨부 유무 확인
        if(meetingImgList != null && meetingImgList.size() > 0){
            for(MeetingImg meetingImg : meetingImgList){
                meetingImgRepository.save(meetingImg);
            }
        }


        //TODO 2021.06.02. 모임 등록시 자신의 팔로워들에게 알림 생성 - hyeongwoo

        return meeting.getId();
    }

    /**
     * 모임수정
     */
    @Override
    public void modify(MeetingDTO meetingDTO) {
        Optional<Meeting> result = meetingRepository.findById(meetingDTO.getMeetingId());

        if(result.isPresent()){
            Meeting meeting = result.get();

            //변경
            meeting.changeCategory(Category.builder().id(meetingDTO.getCategoryId()).build());
            meeting.changeMember(Member.builder().id(meetingDTO.getMasterId()).build());
            meeting.changeTitle(meetingDTO.getTitle());
            meeting.changeText(meetingDTO.getText());
            meeting.changePlace(meetingDTO.getPlace());
            meeting.changeMaxNumber(meetingDTO.getMaxNumber());
            meeting.changeMeetDate(meetingDTO.getMeetDate());
            meeting.changeReqDeadline(meetingDTO.getReqDeadline());
            meeting.changeChatLink(meetingDTO.getChatLink());


            /* 여기서부터 img 변경 */
            // meeting의 사진 모두 삭제
            List<MeetingImg> meetingImgResult = meetingImgRepository.findByMeeting(meeting);
            if(meetingImgResult != null && meetingImgResult.size() > 0 ) {
                meetingImgRepository.deleteByMeeting(meeting);
            }

            List<MeetingImg> meetingImgList = getImgDtoToEntity(meetingDTO, meeting);

            // 사진 첨부 유무 확인
            if(meetingImgList != null && meetingImgList.size() > 0){
                for(MeetingImg meetingImg : meetingImgList){
                    meetingImgRepository.save(meetingImg);
                }
            }
        }

    }

    /**
     * 모임삭제
     */
    @Override
    public void remove(Long meetingId) {
        Optional<Meeting> result = meetingRepository.findById(meetingId);
        Meeting meeting = result.get();

        // participant -> meetingImg -> meeting 순으로 삭제
        participantRepository.deleteByMeeting(meeting);


        //서버 컴퓨터에 저장된 사진파일 삭제
        //TODO 2021.06.16-실제 테스트 필요-hyeongwoo
        List<MeetingImg> meetingImgList = meetingImgRepository.findByMeeting(meeting);

        if(meetingImgList != null && meetingImgList.size() > 0 ){
            for(MeetingImg meetingImg: meetingImgList){
                String srcFileName = meetingImg.getFilePath() + File.separator + meetingImg.getFileName();
                File file = new File(uploadPath, srcFileName);
                file.delete();

                File thumbnail = new File(uploadPath + File.separator +
                        "s_" + File.separator + srcFileName);
                thumbnail.delete();
            }
        }

        meetingImgRepository.deleteByMeeting(meeting);

        meetingRepository.deleteById(meetingId);
    }

    /**
     * 모임조회 페이지
     */
    @Override
    @Transactional(readOnly = true)
    public MeetingDTO getMeeting(Long meetingId) {
        List<Object[]> result = meetingRepository.getMeetingWithAll(meetingId);

        Meeting meeting =(Meeting)result.get(0)[0];


        List<MeetingImg> meetingImgList = new ArrayList<>();

        //모임 사진 유무 체크
        if(result.get(0)[1] != null){
            result.forEach(arr ->{
                meetingImgList.add( (MeetingImg) arr[1] );
            });
        }

        Category category = (Category) result.get(0)[2];

        return readEntitiesToDTO(meeting, meetingImgList, category);
    }

    /**
     * 모임조회
     */
    @Override
    @Transactional(readOnly = true)
    public Meeting findOne(Long meetingId) {
        Optional<Meeting> result = meetingRepository.findById(meetingId);

        Meeting meeting = result.get();
        return meeting;
    }
}
