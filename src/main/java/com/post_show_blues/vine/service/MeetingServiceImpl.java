package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import com.post_show_blues.vine.domain.meetingimg.MeetingImgRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import com.post_show_blues.vine.domain.requestParticipant.RequestParticipantRepository;
import com.post_show_blues.vine.dto.meeting.MeetingDTO;
import com.post_show_blues.vine.dto.meetingImg.MeetingImgUploadDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.file.FileStore;
import com.post_show_blues.vine.file.ResultFileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class MeetingServiceImpl implements MeetingService{

    @Value("${org.zerock.upload.path}")
    private String uploadPath;
    private final FileStore fileStore;

    private final MeetingRepository meetingRepository;
    private final MeetingImgRepository meetingImgRepository;
    private final NoticeRepository noticeRepository;
    private final ParticipantRepository participantRepository;
    private final RequestParticipantRepository requestParticipantRepository;
    private final MemberImgService memberImgService;

    /**
     * 모임등록
     */
    @Transactional
    @Override
    public Long register(MeetingDTO meetingDTO) throws IOException {

        //활동날짜, 신청 마감날짜 비교
        /*
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

         */

        if(meetingDTO.getMeetDate().isBefore(meetingDTO.getReqDeadline())){
            throw new IllegalStateException("활동일이 신청마감일보다 빠릅니다.");
        }


        //모임저장
        Meeting meeting = dtoToEntity(meetingDTO);
        meetingRepository.save(meeting);


        List<MultipartFile> imageFiles = meetingDTO.getImageFiles();

        if(imageFiles != null && imageFiles.size() > 0){
            List<ResultFileStore> resultFileStores = fileStore.storeFiles(imageFiles);

            //모임사진 저장
            for(ResultFileStore resultFileStore : resultFileStores){

                MeetingImg meetingImg = toMeetingImg(meeting, resultFileStore);

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
    public void modify(MeetingDTO meetingDTO) throws IOException {

        Meeting meeting = meetingRepository.findById(meetingDTO.getMeetingId()).orElseThrow(() ->
                new IllegalStateException("존재하는 않은 모임입니다."));



        //수정한 meetDate, reqDeadline 체크
        if(meetingDTO.getMeetDate().isBefore(meetingDTO.getReqDeadline())){
            throw new IllegalStateException("활동일이 신청마감일보다 빠릅니다.");
        }

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
        // meeting 의 기존 사진 모두 삭제
        List<MeetingImg> meetingImgList = meetingImgRepository.findByMeeting(meeting);

        //서버 컴퓨터에 저장된 사진 삭제
        fileRemove(meetingImgList);

        //meetingImg 삭제
        meetingImgRepository.deleteByMeeting(meeting);

        //새로운 사진 저장
        List<MultipartFile> imageFiles = meetingDTO.getImageFiles();

        if(imageFiles != null && imageFiles.size() > 0){

            //서버 컴퓨터에 사진 저장
            List<ResultFileStore> resultFileStores = fileStore.storeFiles(imageFiles);

            //모임사진 저장
            for(ResultFileStore resultFileStore : resultFileStores){
                MeetingImg meetingImg = toMeetingImg(meeting, resultFileStore);
                meetingImgRepository.save(meetingImg);

            }
        }

    }


    private void fileRemove(List<MeetingImg> meetingImgList) {

        if(meetingImgList != null && meetingImgList.size() > 0 ){

            for(MeetingImg meetingImg: meetingImgList){
                String srcFileName = meetingImg.getFolderPath() + File.separator + meetingImg.getStoreFileName();
                File file = new File(uploadPath, srcFileName);
                file.delete();

                File thumbnail = new File(uploadPath + File.separator +
                        "s_" + File.separator + srcFileName);
                thumbnail.delete();
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

        // participant -> requestParticipant -> 서버컴퓨터 사진삭제 -> meetingImg -> meeting 순으로 삭제
        participantRepository.deleteByMeeting(meeting);

        requestParticipantRepository.deleteByMeeting(meeting);

        //TODO 2021.06.16-실제 테스트 필요-hyeongwoo
        List<MeetingImg> meetingImgList = meetingImgRepository.findByMeeting(meeting);

        //서버 컴퓨터에 저장된 사진 삭제
        fileRemove(meetingImgList);

        //meetingImg 삭제
        meetingImgRepository.deleteByMeeting(meeting);

        meetingRepository.deleteById(meetingId);
    }



    /**
     * 모임리스트 조회
     */
    @Override
    @Transactional(readOnly = true)
    public PageResultDTO<MeetingDTO, Object[]> getMeetingList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("id").descending());

        Page<Object[]> result = meetingRepository.searchPage(pageRequestDTO.getCategory(),
                                                                pageRequestDTO.getKeyword(), pageable);

        Function<Object[], MeetingDTO> fn = (arr -> listEntityToDTO(
                (Meeting)arr[0], //모임 엔티티
                (MemberImg)arr[1], //모임장 프로필 사진
                memberImgService.findOne((Long)arr[2])) //참여회원 프로필 사진
        );

        return new PageResultDTO<>(result, fn);
    }

    /**
     * 모임상세 조회 페이지
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
