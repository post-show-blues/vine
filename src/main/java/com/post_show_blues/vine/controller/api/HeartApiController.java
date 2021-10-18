package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.service.heart.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/heart")
public class HeartApiController {

    private final HeartService heartService;

    @GetMapping("/{meetingId}")
    public ResponseEntity<?> heart(@PathVariable Long meetingId, @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        heartService.heart(principalDetails.getMember().getId(), meetingId);

        return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 성공", null), HttpStatus.OK);
    }

    @DeleteMapping("/{meetingId}")
    public ResponseEntity<?> unheart(@PathVariable Long meetingId, @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        heartService.unheart(principalDetails.getMember().getId(), meetingId);

        return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 취소 성공", null), HttpStatus.OK);
    }

}
