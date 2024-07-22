package com.example.market.controller;

import com.example.market.dto.CategoryDto;
import com.example.market.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CategoryController {

    private final CategoryService categoryService;

    // 생성
    @GetMapping("/categories")
    public String getCategoryForm(Model model) {
        model.addAttribute("categoryDto", CategoryDto.of());
        return "category/categoryForm";
    }

    @PostMapping("/categories")
    public String createCategory(@ModelAttribute("categoryDto") CategoryDto request) {
        categoryService.createCategory(request);
        return "redirect:/categories";
    }

    // 카테고리 목록
    @GetMapping("/categories/list")
    public String getCategories(Model model, @RequestParam(value = "categoryId", required = false) Long categoryId) {
        List<CategoryDto> categories = categoryService.getCategoryList();
        model.addAttribute("categoryList", categories);
        return "category/categoryList";
    }

    // TODO  카테고리 삭제
}