package com.waleed.expenseTracker.service.category;

import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.exception.AppException;
import com.waleed.expenseTracker.model.entity.Category;
import com.waleed.expenseTracker.model.entity.User;
import com.waleed.expenseTracker.model.mappers.CategoryMapper;
import com.waleed.expenseTracker.model.request.category.CreateCategoryRequest;
import com.waleed.expenseTracker.repository.CategoryRepository;
import com.waleed.expenseTracker.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repo;
    private final UserService userService;
    private final CategoryMapper mapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public List<Category> findAll() {return repo.findAll();}

    @Override
    public List<Category> findAll(Long userId) {return repo.findByUserId(userId);}

    @Override
    public List<Category> findAllByType(String type, Long userId) {
        return repo.findByTypeAndUserId(CategoryType.valueOf(type), userId);
    }

    @Override
    public Category findById(Long categoryId, Long userId) {
        Optional<Category> category = repo.findByIdAndUserId(categoryId, userId);
        return category
                .orElseThrow(() -> new AppException(String.format("Category: %d not found for user: %d", categoryId, userId)));
    }


    @Override
    @Transactional
    public Category create(CreateCategoryRequest request, Long userId) {
        log.info("About to Create Category: {} For User: {}", request, userId);
        CategoryType type = CategoryType.valueOf(request.type());
        Optional<Category> category =
              repo.findByNameAndTypeAndUserId(request.name(), type, userId);

        if (category.isPresent())
            throw new AppException(String.format("Category: %s of type: %s already exists", request.name(), type));

        User user = userService.getUserById(userId);
        Category created = repo.save(category(request, type, user));
        log.info("Category Created Successfully {}", mapper.toDto(created));
        return created;
    }



    @Override
    @Transactional
    public Category updateName(Long categoryId, String newName, Long userId) {
        log.info("About to Update Category: {} For User: {}", categoryId, userId);
        Category category = findById(categoryId, userId);
        category.setName(newName);
        Category updated = repo.save(category);
        log.info("Category Updated Successfully {}", mapper.toDto(updated));
        return updated;
    }


    private static Category category(CreateCategoryRequest request, CategoryType type, User user) {
        Category categoryEntity = new Category();
        categoryEntity.setName(request.name());
        categoryEntity.setType(type);
        categoryEntity.setUser(user);
        return categoryEntity;
    }
}
