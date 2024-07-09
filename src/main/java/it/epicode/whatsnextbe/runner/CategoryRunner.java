package it.epicode.whatsnextbe.runner;

import it.epicode.whatsnextbe.dto.request.category.CategoryRequest;
import it.epicode.whatsnextbe.dto.request.category.CreateCategoryRequest;
import it.epicode.whatsnextbe.repository.CategoryRepository;
import it.epicode.whatsnextbe.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Component
@Order(2)
public class CategoryRunner implements ApplicationRunner {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (categoryRepository.count() == 0) {
            List<CreateCategoryRequest> categories = Arrays.asList(
                    new CreateCategoryRequest("Progetto 1", "Activities related to sports"),
                    new CreateCategoryRequest("Progetto 2", "Home-related activities"),
                    new CreateCategoryRequest("Progetto 3", "Study and educational activities")
            );
            categories.forEach(category -> {
                categoryService.createCategory(category);
            });
            System.out.println("Categories created");
        } else {
            System.out.println("Categories already exist");
        }


    }
}
