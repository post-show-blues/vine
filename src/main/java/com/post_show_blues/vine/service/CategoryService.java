package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.dto.CategoryDTO;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getCategoryList();

    default CategoryDTO entityTODTO(Category category){

        CategoryDTO categoryDTO = CategoryDTO.builder()
                .categoryId(category.getId())
                .name(category.getName())
                .build();

        return categoryDTO;
    }
}
