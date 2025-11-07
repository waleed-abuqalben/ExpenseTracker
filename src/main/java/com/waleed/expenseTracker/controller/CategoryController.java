package com.waleed.expenseTracker.controller;

import com.waleed.expenseTracker.annotation.enumValue.EnumValue;
import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.model.dto.CategoryDto;
import com.waleed.expenseTracker.model.entity.Category;
import com.waleed.expenseTracker.model.mappers.CategoryMapper;
import com.waleed.expenseTracker.model.request.category.CreateCategoryRequest;
import com.waleed.expenseTracker.model.request.category.UpdateCategoryNameRequest;
import com.waleed.expenseTracker.model.response.ApiResponse;
import com.waleed.expenseTracker.security.user.SystemUserDetails;
import com.waleed.expenseTracker.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper mapper;

    @GetMapping("user")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> findAll(
            @AuthenticationPrincipal SystemUserDetails user) {
            return ResponseEntity.ok(
                    ApiResponse.of(toDto(categoryService.findAll(user.getId()))));
    }
    @GetMapping("all")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> findAll() {
        return ResponseEntity.ok(
                ApiResponse.of(toDto(categoryService.findAll())));
    }

    @GetMapping("{type}")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> findAllByType(
            @EnumValue(enumClass = CategoryType.class, message = CategoryType.INVALID_MESSAGE)
            @PathVariable  String type,
            @AuthenticationPrincipal SystemUserDetails user) {
        return ResponseEntity.ok(
                ApiResponse.of(toDto(categoryService.findAllByType(type, user.getId()))));
    }

    @PostMapping("category")
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request,
            @AuthenticationPrincipal SystemUserDetails userDetails) {

        Category created = categoryService.create(request, userDetails.getId());
        return ResponseEntity.status(CREATED)
                .body(ApiResponse.of("Category Created Successfully", toDto(created)));
    }

    @PatchMapping("category/{id}/name")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategoryName(
            @PathVariable long id,
            @Valid @RequestBody UpdateCategoryNameRequest request,
            @AuthenticationPrincipal SystemUserDetails userDetails)
    {
        Category updated = categoryService.updateName(id, request.name(), userDetails.getId());
        return ResponseEntity.ok(ApiResponse.of("Category Updated Successfully", toDto(updated)));
    }

    private CategoryDto toDto(Category category) {
        return mapper.toDto(category);
    }
    private List<CategoryDto> toDto(List<Category> categories) {
        return mapper.toDtoList(categories);
    }
}
