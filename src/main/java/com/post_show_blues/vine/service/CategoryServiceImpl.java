package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.category.CategoryRepository;
import com.post_show_blues.vine.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService{

    @Autowired CategoryRepository categoryRepository;

    /**
     * 카테고리리스트(DTO)
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoryList() {
        List<Category> result = categoryRepository.findAll();

        List<CategoryDTO> categoryDTOList = result.stream().map(category -> {
            return entityTODTO(category);
        }).collect(Collectors.toList());

        return categoryDTOList;
    }
}
