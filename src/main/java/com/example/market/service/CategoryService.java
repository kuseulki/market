package com.example.market.service;

import com.example.market.domain.Category;
import com.example.market.domain.Item;
import com.example.market.dto.CategoryDto;
import com.example.market.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**     카테고리 등록    -  저장  */
    public void createCategory(CategoryDto dto) {
        categoryRepository.save(dto.toEntity());
    }

    /**     상품별 목록 조회   */
    public List<Item> findItemsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + categoryId));
        List<Item> items = category.getItems();
        return items != null ? items : Collections.emptyList();
    }

    /**  카테고리 목록 조회  */
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoryList() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryDto::from)
                .collect(Collectors.toList());
    }

    /**     카테고리 삭제 */
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + categoryId));
        categoryRepository.delete(category);
    }

    /** 카테고리 ID로 카테고리 엔티티 조회    */
    public Category getCategoryEntity(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + categoryId));
    }

    /** 카테고리 이름으로 카테고리 엔티티 조회   */
    public String findCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with name: " + name));
        return category.getName();
    }
}
