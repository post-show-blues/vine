package com.post_show_blues.vine.service.member;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.dto.member.MemberImgDTO;
import com.post_show_blues.vine.dto.member.MemberUpdateResultDTO;
import com.post_show_blues.vine.dto.member.MyProfileUpdateRequestDTO;
import com.post_show_blues.vine.dto.member.MyProfileUpdateResultDTO;
import com.post_show_blues.vine.file.FileStore;
import com.post_show_blues.vine.file.ResultFileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class MemberUpdateService {
    private final MemberRepository memberRepository;
    private final MemberImgRepository memberImgRepository;
    private final FileStore fileStore;

    @Transactional
    public MyProfileUpdateResultDTO profile(Long id) throws IOException {
        // 1. 영속화
        List<Object[]> memberAndMemberImgs = memberRepository.findMemberAndMemberImgById(id);
        if (memberAndMemberImgs.size() == 0) {
            throw new IllegalArgumentException("찾을 수 없는 id입니다");
        }

        Member member = (Member) memberAndMemberImgs.get(0)[0];
        MemberImg memberImg = (MemberImg) memberAndMemberImgs.get(0)[1];

        MyProfileUpdateResultDTO myProfileUpdateResultDTO = new MyProfileUpdateResultDTO();

        //사진이 있는 경우
        if (memberImg != null) {
            memberImgResult(memberImg, myProfileUpdateResultDTO);
        }

        memberTextResult(member, myProfileUpdateResultDTO);

        return myProfileUpdateResultDTO;
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public Object[] memberUpdate(Long id, MyProfileUpdateRequestDTO myProfileUpdateRequestDTO) throws IOException {
        // 1. 영속화
        Member memberEntity = memberRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("찾을 수 없는 id입니다")
        );

        memberTextUpdate(memberEntity, myProfileUpdateRequestDTO);
        memberImgUpdate(memberEntity, Optional.ofNullable(myProfileUpdateRequestDTO.getFile()));

        MemberImg memberImg = memberImgRepository.findByMember(memberEntity).orElse(null);

        MemberUpdateResultDTO memberUpdateResultDTO = new MemberUpdateResultDTO(memberEntity, memberImg);

        return new Object[]{memberEntity, memberUpdateResultDTO};
    }

    private void memberTextResult(Member member, MyProfileUpdateResultDTO myProfileUpdateResultDTO) {
        myProfileUpdateResultDTO.setEmail(member.getEmail());

        if (!member.getFacebookurl().isEmpty())
            myProfileUpdateResultDTO.setFacebookurl(member.getFacebookurl());

        if (!member.getInstaurl().isEmpty())
            myProfileUpdateResultDTO.setInstaurl(member.getInstaurl());

        myProfileUpdateResultDTO.setNickname(member.getNickname());
        myProfileUpdateResultDTO.setText(member.getText());
    }

    private void memberImgResult(MemberImg memberImg, MyProfileUpdateResultDTO myProfileUpdateResultDTO) {
        myProfileUpdateResultDTO.setFile(MemberImgDTO.builder()
                .storeFileName(memberImg.getStoreFileName())
                .folderPath(memberImg.getFolderPath()).build());
    }


    private void memberTextUpdate(Member member, MyProfileUpdateRequestDTO myProfileUpdateRequestDTO) {
        member.setNickname(myProfileUpdateRequestDTO.getNickname());
        member.setEmail(myProfileUpdateRequestDTO.getEmail());
        member.setInstaurl(myProfileUpdateRequestDTO.getInstaurl());
        member.setFacebookurl(myProfileUpdateRequestDTO.getFacebookurl());
        member.setText(myProfileUpdateRequestDTO.getText());
    }

    private void memberImgUpdate(Member member, Optional<MultipartFile> memberImgUploadDto) throws IOException {
        Optional<MemberImg> memberImgEntity = memberImgRepository.findByMember(member);

        //dto 사진 o, db 사진 o
        //db, 파일 사진 삭제 + db, 파일 사진 생성
        if (memberImgUploadDto.isPresent() && memberImgEntity.isPresent()) {
            removeFileAndDB(member, memberImgEntity.get());
            saveFileAndDB(member, memberImgUploadDto.get());
        }

        //dto 사진 o, db 사진 x
        //db, 파일 사진 생성
        else if (memberImgUploadDto.isPresent() && memberImgEntity.isEmpty()) {
            saveFileAndDB(member, memberImgUploadDto.get());
        }

        //dto 사진 x, db 사진 o
        //db, 파일 사진 삭제
        else if (memberImgUploadDto.isEmpty() && memberImgEntity.isPresent()) {
            removeFileAndDB(member, memberImgEntity.get());
        }

        //dto 사진 x, db 사진 x
        //Do Nothing
    }

    private void saveFileAndDB(Member member, MultipartFile file) throws IOException {
        ResultFileStore resultFileStore = fileStore.storeProfileFile(file);
        memberImgRepository.save(
                MemberImg.builder()
                        .storeFileName(resultFileStore.getStoreFileName())
                        .folderPath(resultFileStore.getFolderPath())
                        .member(member)
                        .build()
        );
    }

    private void removeFileAndDB(Member member, MemberImg memberImg) {
        memberImgRepository.deleteByMember(member);
        fileRemove(memberImg);
    }

    private void fileRemove(MemberImg memberImg) {
        String folderPath = memberImg.getFolderPath();
        String storeFileName = memberImg.getStoreFileName();

        File file = new File(fileStore.getFullPath(folderPath, storeFileName));
        file.delete();
    }
}
