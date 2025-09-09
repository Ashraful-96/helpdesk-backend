package com.aust.its.controller;

import com.aust.its.annotation.swaggerapidoc.categorycontroller.*;
import com.aust.its.dto.CategoryDto;
import com.aust.its.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category APIs", description = "Category related APIs")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @CategoryListApiDoc
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAll());
    }


    @CategoryByIdApiDoc
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }


    @CategoryCreateApiDoc
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.create(categoryDto));
    }


    @CategoryUpdateApiDoc
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable long id, @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.update(id, categoryDto));
    }


    @CategoryDeleteApiDoc
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id) {
        categoryService.delete(id);
        return ResponseEntity.ok("category deleted");
    }
}
