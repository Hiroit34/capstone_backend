package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.dto.request.category.CategoryRequest;
import it.epicode.whatsnextbe.dto.request.category.CreateCategoryRequest;
import it.epicode.whatsnextbe.dto.request.category.UpdateCategoryRequest;
import it.epicode.whatsnextbe.dto.response.category.CategoryResponse;
import it.epicode.whatsnextbe.dto.response.category.CategoryWithTaskResponse;
import it.epicode.whatsnextbe.error.ResourceNotFoundException;
import it.epicode.whatsnextbe.mapper.CategoryMapper;
import it.epicode.whatsnextbe.model.Category;
import it.epicode.whatsnextbe.model.Task;
import it.epicode.whatsnextbe.repository.CategoryRepository;
import it.epicode.whatsnextbe.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    public List<CategoryWithTaskResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryMapper::mapToCategoryWithTaskResponse).collect(Collectors.toList());
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

    public CategoryResponse createCategory(CreateCategoryRequest request) {
        Category entity = new Category();
        BeanUtils.copyProperties(request, entity);
        CategoryResponse response = new CategoryResponse();
        BeanUtils.copyProperties(entity, response);
        categoryRepository.save(entity);
        return response;
    }

    public CategoryResponse modifyCategory(Long id, UpdateCategoryRequest request) {
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

    @Transactional
    public void deleteCategory(Long categoryId) {
        // Trova la categoria
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // Aggiorna tutte le task associate a questa categoria
        List<Task> tasks = taskRepository.findByCategoryId(categoryId);
        for (Task task : tasks) {
            task.setCategory(null);
            taskRepository.save(task);
        }

        // Ora puoi eliminare la categoria
        categoryRepository.deleteById(categoryId);
    }

}
