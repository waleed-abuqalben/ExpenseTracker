package com.waleed.expenseTracker.controller;

import com.waleed.expenseTracker.annotation.enumValue.EnumValue;
import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.model.dto.CategoryDto;
import com.waleed.expenseTracker.model.entity.Category;
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

    @GetMapping("user")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> findAll(
            @AuthenticationPrincipal SystemUserDetails user) {
            return ResponseEntity.ok(
                    ApiResponse.of(categoryService.findAll(user.getId())));
    }
    @GetMapping("all")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> findAll() {
        return ResponseEntity.ok(
                ApiResponse.of(categoryService.findAll()));
    }

    @GetMapping("{type}")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> findAllByType(
            @EnumValue(enumClass = CategoryType.class, message = CategoryType.INVALID_MESSAGE)
            @PathVariable  String type,
            @AuthenticationPrincipal SystemUserDetails user) {
        return ResponseEntity.ok(
                ApiResponse.of(categoryService.findAllByType(type, user.getId())));
    }

    @PostMapping("category")
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request,
            @AuthenticationPrincipal SystemUserDetails userDetails) {

        CategoryDto created = categoryService.create(request, userDetails.getId());
        return ResponseEntity.status(CREATED)
                .body(ApiResponse.of("Category Created Successfully", created));
    }

    @PatchMapping("category/{id}/update-name")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategoryName(
            @PathVariable long id,
            @Valid @RequestBody UpdateCategoryNameRequest request,
            @AuthenticationPrincipal SystemUserDetails userDetails)
    {
        CategoryDto updated = categoryService.updateName(id, request.updatedName(), userDetails.getId());
        return ResponseEntity.ok(ApiResponse.of("Category Updated Successfully", updated));
    }


}
