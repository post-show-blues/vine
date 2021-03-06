package com.post_show_blues.vine.service.meeting;

import com.post_show_blues.vine.domain.follow.FollowRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import com.post_show_blues.vine.domain.meetingimg.MeetingImgRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import com.post_show_blues.vine.domain.requestParticipant.RequestParticipantRepository;
import com.post_show_blues.vine.dto.meeting.DetailMeetingDTO;
import com.post_show_blues.vine.dto.meeting.MeetingDTO;
import com.post_show_blues.vine.dto.meeting.MeetingResDTO;
import com.post_show_blues.vine.dto.notice.NoticeDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.dto.participant.ParticipantDTO;
import com.post_show_blues.vine.file.FileStore;
import com.post_show_blues.vine.file.ResultFileStore;
import com.post_show_blues.vine.handler.exception.CustomException;
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
     * ????????????
     */
    @Transactional
    @Override
    public Long register(MeetingDTO meetingDTO, Long principalId) throws IOException {

        //????????????, ?????? ???????????? ??????
        if(meetingDTO.getMeetDate().isBefore(meetingDTO.getReqDeadline())){
            throw new CustomException("???????????? ????????????????????? ????????????.");
        }


        //????????????
        Meeting meeting = dtoToEntity(meetingDTO, principalId);
        meetingRepository.save(meeting);


        List<MultipartFile> imageFiles = meetingDTO.getImageFiles();

        if(imageFiles != null && imageFiles.size() > 0){
            List<ResultFileStore> resultFileStores = fileStore.storeFiles(imageFiles);

            //???????????? ??????
            for(ResultFileStore resultFileStore : resultFileStores){

                MeetingImg meetingImg = toMeetingImg(meeting, resultFileStore);

                meetingImgRepository.save(meetingImg);

            }
        }

        //participant ??? ?????? ??????
        Participant participant = Participant.builder()
                .meeting(meeting)
                .member(Member.builder().id(principalId).build())
                .build();

        participantRepository.save(participant);

        //?????????????????? ?????? ??????
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
                        .text(nicknameOfMaster + "?????? ????????? ????????? ???????????????.")
                        .link("/meetings/" + meeting.getId())
                        .build();

                noticeRepository.save(notion);
            }
        }
        return meeting.getId();
    }


    /**
     * ????????????
     */
    @Transactional
    @Override
    public void modify(MeetingDTO meetingDTO, Long principalId) throws IOException {

        Meeting meeting = meetingRepository.findById(meetingDTO.getMeetingId()).orElseThrow(() ->
                new CustomException("???????????? ?????? ???????????????."));

        //????????? meetDate, reqDeadline ??????
        if(meetingDTO.getMeetDate().isBefore(meetingDTO.getReqDeadline())){
            throw new CustomException("???????????? ????????????????????? ????????????.");
        }

        //??????
        meeting.changeCategory(meetingDTO.getCategory());
        meeting.changeTitle(meetingDTO.getTitle());
        meeting.changeText(meetingDTO.getText());
        meeting.changePlace(meetingDTO.getPlace());
        meeting.changeMaxNumber(meetingDTO.getMaxNumber());
        meeting.changeMeetDate(meetingDTO.getMeetDate());
        meeting.changeReqDeadline(meetingDTO.getReqDeadline());
        meeting.changeDDay();
        meeting.changeChatLink(meetingDTO.getChatLink());


        /* ??????????????? img ?????? */
        // meeting ??? ?????? ?????? ?????? ??????
        List<MeetingImg> meetingImgList = meetingImgRepository.findByMeeting(meeting);

        //?????? ???????????? ????????? ?????? ??????
        fileRemove(meetingImgList);

        //meetingImg ??????
        meetingImgRepository.deleteByMeeting(meeting);

        //????????? ?????? ??????
        List<MultipartFile> imageFiles = meetingDTO.getImageFiles();

        if(imageFiles != null && imageFiles.size() > 0){

            //?????? ???????????? ?????? ??????
            List<ResultFileStore> resultFileStores = fileStore.storeFiles(imageFiles);

            //???????????? ??????
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
     * ????????????
     */
    @Transactional
    @Override
    public void remove(Long meetingId) {

        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() ->
                new CustomException("???????????? ?????? ???????????????."));

        //????????? ?????? ?????? ?????? ?????????
        List<Participant> participantList = participantRepository.findByMeeting(meeting);

        // participant -> requestParticipant -> ??????????????? ????????????
        // -> meetingImg -> meeting ????????? ?????? (meeting ?????? ??? cascade??? ????????? ??????)
        participantRepository.deleteByMeeting(meeting);

        requestParticipantRepository.deleteByMeeting(meeting);

        List<MeetingImg> meetingImgList = meetingImgRepository.findByMeeting(meeting);

        //?????? ???????????? ????????? ?????? ??????
        fileRemove(meetingImgList);

        //meetingImg ??????
        meetingImgRepository.deleteByMeeting(meeting);

        //meeting, comment ??????
        meetingRepository.deleteById(meetingId);

        //?????? ?????????????????? ?????? ??????
        System.out.println("=================");
        System.out.println(meeting.getText());
        participantList.forEach(par -> {
            Notice notice = Notice.builder()
                    .memberId(par.getMember().getId())
                    .text(meeting.getText() + " ????????? ?????????????????????.")
                    .build();

            noticeRepository.save(notice);
        });

    }



    /**
     * ?????? ??????????????? ??????
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
                (Meeting)arr[0], //?????? ?????????
                meetingImgService.findOne((Long)arr[1]), //?????? ??????  //?????? ?????? ??????
                (Member)arr[2], //?????? ?????????
                (MemberImg)arr[3],//????????? ????????? ??????
                (Integer)arr[4],//?????? ???
                (Integer)arr[5],//?????? ???
                principalId) //?????? ?????? id
        );

        return new PageResultDTO<>(result, fn);
    }

    /**
     * ???????????? ????????? ??????????????? ??????
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
                (Meeting)arr[0], //?????? ?????????
                meetingImgService.findOne((Long)arr[1]), //?????? ??????
                (Member)arr[2], //?????? ?????????
                (MemberImg)arr[3],//????????? ????????? ??????
                (Integer)arr[4],//?????? ???
                (Integer)arr[5],//?????? ???
                principalId) //?????? ?????? id
        );

        return new PageResultDTO<>(result, fn);
    }

    /**
     * ????????? ??????????????? ??????
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
                (Meeting)arr[0], //?????? ?????????
                meetingImgService.findOne((Long)arr[1]), //?????? ??????
                (Member)arr[2], //?????? ?????????
                (MemberImg)arr[3],//????????? ????????? ??????
                (Integer)arr[4],//?????? ???
                (Integer)arr[5],//?????? ???
                principalId) //?????? ?????? id
        );

        return new PageResultDTO<>(result, fn);
    }

    /**
     * ???????????? ?????? ?????????
     */
    @Transactional(readOnly = true)
    @Override
    public DetailMeetingDTO getMeeting(Long meetingId, Long participantId) {

        meetingRepository.findById(meetingId).orElseThrow(() ->
                new CustomException("???????????? ?????? ???????????????."));

        List<Object[]> result = meetingRepository.getMeetingWithAll(meetingId);

        Meeting meeting =(Meeting)result.get(0)[0];

        List<MeetingImg> meetingImgList = new ArrayList<>();

        //?????? ?????? ?????? ??????
        if(result.get(0)[1] != null){
            result.forEach(arr ->{
                meetingImgList.add( (MeetingImg) arr[1] );
            });
        }

        Integer commentCount = (Integer) result.get(0)[2];

        Integer heartCount = (Integer) result.get(0)[3];


        //????????? ?????????
        List<ParticipantDTO> participantDTOList = participantService.getParticipantList(meeting.getId());

        return readEntitiesToDTO(meeting, meetingImgList, commentCount, heartCount,
                participantDTOList, participantId);
    }

    /**
     * ?????? ?????? ??????
     */
    @Transactional(readOnly = true)
    @Override
    public Meeting findOne(Long meetingId) {
        Optional<Meeting> result = meetingRepository.findById(meetingId);

        Meeting meeting = result.get();
        return meeting;
    }


    /**
     * ????????? ??????
     */
    @Transactional
    @Override
    public void updatedDay() {

        //?????? ????????? ?????? ????????? (???????????? ?????? ??????x)
        List<Meeting> meetingList = meetingRepository.getUpdateMeetingDdayList();

        meetingList.stream().forEach(meeting -> meeting.updateDDay());

    }
}
