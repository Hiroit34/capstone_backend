package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.dto.request.category.CategoryRequest;
import it.epicode.whatsnextbe.dto.response.category.CategoryResponse;
import it.epicode.whatsnextbe.model.Category;
import it.epicode.whatsnextbe.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public CategoryResponse getCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category with id " + id + " not found");
        }
        Category enity = categoryRepository.findById(id).get();
        CategoryResponse response = new CategoryResponse();
        BeanUtils.copyProperties(enity, response);
        return response;
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        Category entity = new Category();
        BeanUtils.copyProperties(request, entity);
        CategoryResponse response = new CategoryResponse();
        BeanUtils.copyProperties(entity, response);
        categoryRepository.save(entity);
        return response;
    }

    public CategoryResponse modifyCategory(Long id, CategoryRequest request) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category with id " + id + " not found");
        }
        Category entity = categoryRepository.findById(id).get();
        BeanUtils.copyProperties(request, entity);
        categoryRepository.save(entity);
        CategoryResponse response = new CategoryResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    public String deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category with id " + id + " not found");
        }
        categoryRepository.deleteById(id);
        return "Category with id " + id + " deleted";
    }

}
