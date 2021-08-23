package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.category.CategoryRepository;
import com.post_show_blues.vine.dto.CategoryDTO;
import com.post_show_blues.vine.service.category.CategoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class CategoryServiceImplTest {

    @Autowired
    CategoryService categoryService;
    @Autowired CategoryRepository categoryRepository;

    @Test
    void 카테고리_리스트() throws Exception{
        //given

        Category category1 = Category.builder()
                .name("categoryA")
                .build();

        categoryRepository.save(category1);

        Category category2 = Category.builder()
                .name("categoryB")
                .build();

        categoryRepository.save(category2);

        Category category3 = Category.builder()
                .name("categoryC")
                .build();

        categoryRepository.save(category3);

        //when
        List<CategoryDTO> categoryList = categoryService.getCategoryList();

        //then
        ////데이터수 체크
        Assertions.assertThat(categoryList.size()).isEqualTo(categoryRepository.categoryCount().intValue());
    }

}