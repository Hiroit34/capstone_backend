package it.epicode.whatsnextbe.controller;

import it.epicode.whatsnextbe.dto.request.category.CategoryRequest;
import it.epicode.whatsnextbe.dto.request.category.CreateCategoryRequest;
import it.epicode.whatsnextbe.dto.request.category.UpdateCategoryRequest;
import it.epicode.whatsnextbe.dto.response.category.CategoryResponse;
import it.epicode.whatsnextbe.dto.response.category.CategoryWithTaskResponse;
import it.epicode.whatsnextbe.model.Category;
import it.epicode.whatsnextbe.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<CategoryWithTaskResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    // POST
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CreateCategoryRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> modifyCategory(@PathVariable Long id, @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.ok(categoryService.modifyCategory(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
