package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.dto.request.category.CategoryRequest;
import it.epicode.whatsnextbe.dto.response.category.CategoryResponse;
import it.epicode.whatsnextbe.model.Category;
import it.epicode.whatsnextbe.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    //serve GET, GET BY ID, POST, PUT, DELETE

    @Autowired
    private CategoryRepository categoryRepository;

    //GET ALL
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    //GET BY ID
    public CategoryResponse getCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category with id " + id + " not found");
        }
        Category enity = categoryRepository.findById(id).get();
        CategoryResponse response = new CategoryResponse();
        BeanUtils.copyProperties(enity, response);
        return response;
    }

    //POST
    public CategoryResponse createCategory(CategoryRequest request) {
        Category entity = new Category();
        BeanUtils.copyProperties(request, entity);
        CategoryResponse response = new CategoryResponse();
        BeanUtils.copyProperties(entity, response);
        categoryRepository.save(entity);
        return response;
    }

    //PUT
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

    //DELETE
    public String deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category with id " + id + " not found");
        }
        categoryRepository.deleteById(id);
        return "Category with id " + id + " deleted";
    }

}
