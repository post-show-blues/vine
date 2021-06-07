package com.post_show_blues.vine.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select count(c.id) from Category c")
    Long categoryCount();
}
