package com.post_show_blues.vine.service.meeting;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.follow.FollowRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import com.post_show_blues.vine.domain.meetingimg.MeetingImgRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import com.post_show_blues.vine.domain.requestParticipant.RequestParticipantRepository;
import com.post_show_blues.vine.dto.meeting.DetailMeetingDTO;
import com.post_show_blues.vine.dto.meeting.MeetingDTO;
import com.post_show_blues.vine.dto.meeting.MeetingResDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.dto.participant.ParticipantDTO;
import com.post_show_blues.vine.file.FileStore;
import com.post_show_blues.vine.file.ResultFileStore;
import com.post_show_blues.vine.service.meetingImg.MeetingImgService;
import com.post_show_blues.vine.service.participant.ParticipantService;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private final MeetingImgService meetingImgService;
    private final FollowRepository followRepository;
    private final ParticipantService participantService;

    /**
     * 모임등록
     */
    @Transactional
    @Override
    public Long register(MeetingDTO meetingDTO) throws IOException {

        //활동날짜, 신청 마감날짜 비교
        if(meetingDTO.getMeetDate().isBefore(meetingDTO.getReqDeadline())){
            throw new IllegalStateException("활동일이 신청마감일보다 빠릅니다.");
        }


        //모임저장
        Meeting meeting = dtoToEntity(meetingDTO);
        Meeting savedMeeting = meetingRepository.save(meeting);


        List<MultipartFile> imageFiles = meetingDTO.getImageFiles();

        if(imageFiles != null && imageFiles.size() > 0){
            List<ResultFileStore> resultFileStores = fileStore.storeFiles(imageFiles);

            //모임사진 저장
            for(ResultFileStore resultFileStore : resultFileStores){

                MeetingImg meetingImg = toMeetingImg(meeting, resultFileStore);

                meetingImgRepository.save(meetingImg);

            }
        }

        //[0] : Member, [1] : MemberImg
        List<Object[]> result = followRepository.findFollowerMembers(meeting.getMember().getId());

        List<Member> followerList = result.stream().map(objects -> {
            Member member = (Member) objects[0];
            return member;
        }).collect(Collectors.toList());


        if(followerList != null && followerList.size() > 0){


            String nicknameOfMaster = meetingRepository.getNicknameOfMaster(meeting.getId());

            for (Member member : followerList){

                Notice notion = Notice.builder()
                        .memberId(member.getId())
                        .text(nicknameOfMaster + "님이 새로운 모임을 열었습니다.")
                        .link("/meetings/" + meeting.getId())
                        .build();

                noticeRepository.save(notion);
            }
        }
        return meeting.getId();
    }


    /**
     * 모임수정
     */
    @Transactional
    @Override
    public void modify(MeetingDTO meetingDTO) throws IOException {

        Meeting meeting = meetingRepository.findById(meetingDTO.getMeetingId()).orElseThrow(() ->
                new IllegalStateException("존재하는 않은 모임입니다."));

        //수정한 meetDate, reqDeadline 체크
        if(meetingDTO.getMeetDate().isBefore(meetingDTO.getReqDeadline())){
            throw new IllegalStateException("활동일이 신청마감일보다 빠릅니다.");
        }

        //변경
        meeting.changeCategory(meetingDTO.getCategory());
        meeting.changeMember(Member.builder().id(meetingDTO.getMasterId()).build());
        meeting.changeTitle(meetingDTO.getTitle());
        meeting.changeText(meetingDTO.getText());
        meeting.changePlace(meetingDTO.getPlace());
        meeting.changeMaxNumber(meetingDTO.getMaxNumber());
        meeting.changeMeetDate(meetingDTO.getMeetDate());
        meeting.changeReqDeadline(meetingDTO.getReqDeadline());
        meeting.changeDDay();
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
                String folderPath = meetingImg.getFolderPath();
                String storeFileName = meetingImg.getStoreFileName();

                File file = new File(fileStore.getFullPath(folderPath, storeFileName));
                file.delete();
            }
        }

    }

    /**
     * 모임삭제
     */
    @Transactional
    @Override
    public void remove(Long meetingId) {
        Optional<Meeting> result = meetingRepository.findById(meetingId);
        Meeting meeting = result.get();

        // participant -> requestParticipant -> 서버컴퓨터 사진삭제
        // -> meetingImg -> meeting 순으로 삭제 (meeting 삭제 시 cascade로 댓글도 삭제)
        participantRepository.deleteByMeeting(meeting);

        requestParticipantRepository.deleteByMeeting(meeting);

        //TODO 2021.06.16-실제 테스트 필요-hyeongwoo
        List<MeetingImg> meetingImgList = meetingImgRepository.findByMeeting(meeting);

        //서버 컴퓨터에 저장된 사진 삭제
        fileRemove(meetingImgList);

        //meetingImg 삭제
        meetingImgRepository.deleteByMeeting(meeting);

        //meeting, comment 삭제
        meetingRepository.deleteById(meetingId);
    }



    /**
     * 전체 모임리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public PageResultDTO<MeetingResDTO, Object[]> getAllMeetingList(PageRequestDTO pageRequestDTO, Long principalId) {

        Pageable pageable;

        if(pageRequestDTO.getSort().get(1).equals("ASC")){

            pageable = pageRequestDTO.getPageable(Sort.by(pageRequestDTO.getSort().get(0)).ascending());
        }else{
            pageable = pageRequestDTO.getPageable(Sort.by(pageRequestDTO.getSort().get(0)).descending());
        }

        Page<Object[]> result = meetingRepository.searchPage(pageRequestDTO.getCategoryList(),
                                                            pageRequestDTO.getKeyword(),
                                                            null, pageable);

        Function<Object[], MeetingResDTO> fn = (arr -> listEntityToDTO(
                (Meeting)arr[0], //모임 엔티티
                meetingImgService.findOne((Long)arr[1]), //모임 사진
                (Member)arr[2], //방장 엔티티
                (MemberImg)arr[3], //모임장 프로필 사진
                principalId) //현재 유저 id
        );

        return new PageResultDTO<>(result, fn);
    }

    /**
     * 팔로우가 방장인 모임리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public PageResultDTO<MeetingResDTO, Object[]> getFollowMeetingList(PageRequestDTO pageRequestDTO, Long principalId) {

        Pageable pageable;

        if(pageRequestDTO.getSort().get(1).equals("ASC")){

            pageable = pageRequestDTO.getPageable(Sort.by(pageRequestDTO.getSort().get(0)).ascending());
        }else{
            pageable = pageRequestDTO.getPageable(Sort.by(pageRequestDTO.getSort().get(0)).descending());
        }

        Page<Object[]> result = meetingRepository.searchPage(null, null, principalId, pageable);

        Function<Object[], MeetingResDTO> fn = (arr -> listEntityToDTO(
                (Meeting)arr[0], //모임 엔티티
                meetingImgService.findOne((Long)arr[1]), //모임 사진
                (Member)arr[2], //방장 엔티티
                (MemberImg)arr[3], //모임장 프로필 사진
                principalId) //현재 유저 id
        );

        return new PageResultDTO<>(result, fn);
    }

    /**
     * 북마크 모임리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public PageResultDTO<MeetingResDTO, Object[]> getBookmarkMeetingList(PageRequestDTO pageRequestDTO, Long principalId) {

        Pageable pageable;

        if(pageRequestDTO.getSort().get(1).equals("ASC")){

            pageable = pageRequestDTO.getPageable(Sort.by(pageRequestDTO.getSort().get(0)).ascending());
        }else{
            pageable = pageRequestDTO.getPageable(Sort.by(pageRequestDTO.getSort().get(0)).descending());
        }

        Page<Object[]> result = meetingRepository.bookmarkPage(principalId, pageable);

        Function<Object[], MeetingResDTO> fn = (arr -> listEntityToDTO(
                (Meeting)arr[0], //모임 엔티티
                meetingImgService.findOne((Long)arr[1]), //모임 사진
                (Member)arr[2], //방장 엔티티
                (MemberImg)arr[3], //모임장 프로필 사진
                principalId) //현재 유저 id
        );

        return new PageResultDTO<>(result, fn);
    }

    /**
     * 모임상세 조회 페이지
     */
    @Transactional(readOnly = true)
    @Override
    public DetailMeetingDTO getMeeting(Long meetingId, Long participantId) {
        List<Object[]> result = meetingRepository.getMeetingWithAll(meetingId);

        Meeting meeting =(Meeting)result.get(0)[0];

        List<MeetingImg> meetingImgList = new ArrayList<>();

        //모임 사진 유무 체크
        if(result.get(0)[1] != null){
            result.forEach(arr ->{
                meetingImgList.add( (MeetingImg) arr[1] );
            });
        }

        Member master = (Member) result.get(0)[2];

        MemberImg masterImg = (MemberImg) result.get(0)[3];


        //참여자 리스트
        List<ParticipantDTO> participantDTOList = participantService.getParticipantList(meeting.getId());

        return readEntitiesToDTO(meeting, meetingImgList, master, masterImg,
                participantDTOList, participantId);
    }

    /**
     * 모임조회
     */
    @Transactional(readOnly = true)
    @Override
    public Meeting findOne(Long meetingId) {
        Optional<Meeting> result = meetingRepository.findById(meetingId);

        Meeting meeting = result.get();
        return meeting;
    }


    /**
     * 디데이 갱신
     */
    @Transactional
    @Override
    public void updatedDay() {

        List<Meeting> meetingList = meetingRepository.findAll();

        meetingList.stream().forEach(meeting -> meeting.updateDDay());

    }
}
