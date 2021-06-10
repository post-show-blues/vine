package com.post_show_blues.vine.domain.meeting;

import com.post_show_blues.vine.domain.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchMeetingRepository {

    Page<Object[]> searchPage(Category category, String keyword, Pageable pageable);

}
