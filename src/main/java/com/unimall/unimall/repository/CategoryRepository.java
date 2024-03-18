package com.unimall.unimall.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.unimall.unimall.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
   
    @Query("Select c from Category c")
    List<Category> getAll();
    
    
}
