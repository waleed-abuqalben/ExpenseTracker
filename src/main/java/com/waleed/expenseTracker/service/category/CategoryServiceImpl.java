package com.waleed.expenseTracker.service.category;

import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.exception.AppException;
import com.waleed.expenseTracker.model.dto.CategoryDto;
import com.waleed.expenseTracker.model.entity.Category;
import com.waleed.expenseTracker.model.entity.User;
import com.waleed.expenseTracker.model.request.category.CategoryCreateRequest;
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
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<CategoryDto> findAll() {return toDto(categoryRepository.findAll());}

    @Override
    public List<CategoryDto> findAll(Long userId) {return toDto(categoryRepository.findByUserId(userId));}

    @Override
    public List<CategoryDto> findAllByType(String type, Long userId) {
        return toDto(categoryRepository.findByTypeAndUserId(CategoryType.valueOf(type), userId));
    }

    @Override
    public Category findByIdAndUserId(Long categoryId, Long userId) {
        Optional<Category> category = categoryRepository.findByIdAndUserId(categoryId, userId);
        return category.orElseThrow(() ->
                new AppException(String.format("Category: %d not found for user: %d", categoryId, userId)));
    }


    @Override
    @Transactional
    public CategoryDto create(CategoryCreateRequest request, Long userId) {
        log.info("About to Create Category: {} For User: {}", request, userId);
        CategoryType type = CategoryType.valueOf(request.type());
        Optional<Category> category =
              categoryRepository.findByNameAndTypeAndUserId(request.name(), type, userId);

        if (category.isPresent())
            throw new AppException(String.format("Category: %s of type: %s already exists", request.name(), type));

        User user = userService.getUserById(userId);
        Category categoryEntity = new Category();
        categoryEntity.setName(request.name());
        categoryEntity.setType(type);
        categoryEntity.setUser(user);
        CategoryDto created = toDto(categoryRepository.save(categoryEntity));
        log.info("Category: {} Created Successfully", created);
        return created;
    }

    @Override
    @Transactional
    public CategoryDto updateName(Long categoryId, String newName, Long userId) {
        Category category = findByIdAndUserId(categoryId, userId);
        category.setName(newName);
        Category updated = categoryRepository.save(category);
        return toDto(updated);
    }

    public List<CategoryDto> toDto(List<Category> categoryList) {
        return categoryList.stream().map(c -> toDto(c)).toList();
    }
    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType().name())
                .userId(category.getUser().getId())
                .build();
    }
}
