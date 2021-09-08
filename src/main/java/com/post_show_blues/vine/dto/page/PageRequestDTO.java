package com.post_show_blues.vine.dto.page;

import com.post_show_blues.vine.domain.category.Category;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

    private int page;

    private int size;

    @Builder.Default
    private List<Long> categoryIdList;

    @Builder.Default
    private String keyword;

    @Builder.Default
    private List<String> sort = List.of("id","DESC");

    public PageRequestDTO(){
        this.page = 1;
        this.size = 36;
        this.sort = List.of("id", "DESC");
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(this.page-1, this.size, sort);
    }



}
