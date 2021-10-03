package com.post_show_blues.vine.service.bookmark;


import com.post_show_blues.vine.domain.bookmark.Bookmark;

public interface BookmarkService {

    Bookmark bookmark(Long meetingId, Long principalId);

}
