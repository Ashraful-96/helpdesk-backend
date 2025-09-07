package com.aust.its.controller;

import com.aust.its.dto.CategoryDto;
import com.aust.its.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable long id) {
        return categoryService.getById(id);
    }

    @PostMapping
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.create(categoryDto);
    }

    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable long id, @RequestBody CategoryDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable long id) {
        categoryService.delete(id);
        return "Deleted";
    }
}
