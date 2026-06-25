package com.waleed.expenseTracker.service.recurringtransaction;

import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.exception.AppException;
import com.waleed.expenseTracker.model.entity.Category;
import com.waleed.expenseTracker.model.entity.RecurringTransaction;
import com.waleed.expenseTracker.model.entity.User;
import com.waleed.expenseTracker.model.mappers.RecurringTransactionMapperImpl;
import com.waleed.expenseTracker.model.request.recurringtransaction.CreateRecurringTransactionRequest;
import com.waleed.expenseTracker.model.request.recurringtransaction.UpdateRecurringTransactionRequest;
import com.waleed.expenseTracker.repository.RecurringTransactionRepository;
import com.waleed.expenseTracker.service.category.CategoryService;
import com.waleed.expenseTracker.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecurringTransactionServiceImplTest {

    @Mock
    private RecurringTransactionRepository repo;
    @Mock
    private CategoryService categoryService;
    @Mock
    private UserService userService;

    private RecurringTransactionServiceImpl service;

    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        service = new RecurringTransactionServiceImpl(repo, categoryService, userService, new RecurringTransactionMapperImpl());

        user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setEmail("john@test.com");

        category = Category.builder().id(10L).name("Rent").type(CategoryType.EXPENSE).user(user).build();
    }

    @Test
    void create_savesTemplateAsActiveByDefault() {
        when(categoryService.findById(category.getId(), user.getId())).thenReturn(category);
        when(userService.getUserById(user.getId())).thenReturn(user);
        when(repo.save(any(RecurringTransaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecurringTransaction created = service.create(
                new CreateRecurringTransactionRequest("Rent", 1200.0, "Monthly rent", category.getId()),
                user.getId());

        assertEquals("Rent", created.getName());
        assertEquals(1200.0, created.getAmount());
        assertTrue(created.isActive());
        assertEquals(category, created.getCategory());
        assertEquals(user, created.getUser());
        verify(repo).save(any(RecurringTransaction.class));
    }

    @Test
    void create_propagatesAppException_whenCategoryNotFoundForUser() {
        when(categoryService.findById(category.getId(), user.getId()))
                .thenThrow(new AppException("Category not found"));

        assertThrows(AppException.class, () -> service.create(
                new CreateRecurringTransactionRequest("Rent", 1200.0, "Monthly rent", category.getId()),
                user.getId()));

        verify(repo, never()).save(any());
    }

    @Test
    void findById_throwsAppException_whenNotFound() {
        when(repo.findByIdAndUserId(99L, user.getId())).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> service.findById(99L, user.getId()));
    }

    @Test
    void findAll_withNoType_returnsAllForUser() {
        RecurringTransaction tx = RecurringTransaction.builder().id(1L).name("Rent").amount(1200.0).active(true)
                .category(category).user(user).build();
        when(repo.findByUserId(user.getId())).thenReturn(List.of(tx));

        List<RecurringTransaction> result = service.findAll(user.getId(), null);

        assertEquals(1, result.size());
        assertEquals(tx, result.get(0));
        verify(repo).findByUserId(user.getId());
        verify(repo, never()).findByUserIdAndCategoryType(any(), any());
    }

    @Test
    void findAll_withType_filtersBy_categoryType() {
        RecurringTransaction tx = RecurringTransaction.builder().id(1L).name("Salary").amount(3000.0).active(true)
                .category(Category.builder().id(20L).name("Salary").type(CategoryType.INCOME).user(user).build())
                .user(user).build();
        when(repo.findByUserIdAndCategoryType(user.getId(), CategoryType.INCOME)).thenReturn(List.of(tx));

        List<RecurringTransaction> result = service.findAll(user.getId(), CategoryType.INCOME);

        assertEquals(1, result.size());
        assertEquals(CategoryType.INCOME, result.get(0).getCategory().getType());
        verify(repo).findByUserIdAndCategoryType(user.getId(), CategoryType.INCOME);
        verify(repo, never()).findByUserId(any());
    }

    @Test
    void update_replacesAllFieldsIncludingActive() {
        RecurringTransaction existing = RecurringTransaction.builder().id(1L).name("Rent").amount(1200.0)
                .description("Monthly rent").active(true).category(category).user(user).build();
        Category newCategory = Category.builder().id(11L).name("Utilities").type(CategoryType.EXPENSE).user(user).build();

        when(categoryService.findById(newCategory.getId(), user.getId())).thenReturn(newCategory);
        when(repo.findByIdAndUserId(existing.getId(), user.getId())).thenReturn(Optional.of(existing));
        when(repo.save(any(RecurringTransaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecurringTransaction updated = service.update(existing.getId(),
                new UpdateRecurringTransactionRequest("Updated Rent", 1300.0, "Increased rent", newCategory.getId(), false),
                user.getId());

        assertEquals("Updated Rent", updated.getName());
        assertEquals(1300.0, updated.getAmount());
        assertFalse(updated.isActive());
        assertEquals(newCategory, updated.getCategory());
    }

    @Test
    void updateActivation_togglesActiveWithoutChangingOtherFields() {
        RecurringTransaction existing = RecurringTransaction.builder().id(1L).name("Rent").amount(1200.0)
                .description("Monthly rent").active(true).category(category).user(user).build();

        when(repo.findByIdAndUserId(existing.getId(), user.getId())).thenReturn(Optional.of(existing));
        when(repo.save(any(RecurringTransaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecurringTransaction deactivated = service.updateActivation(existing.getId(), false, user.getId());

        assertFalse(deactivated.isActive());
        assertEquals("Rent", deactivated.getName());
        assertEquals(1200.0, deactivated.getAmount());
    }

    @Test
    void delete_removesViaRepository() {
        RecurringTransaction existing = RecurringTransaction.builder().id(1L).name("Rent").amount(1200.0)
                .active(true).category(category).user(user).build();
        when(repo.findByIdAndUserId(existing.getId(), user.getId())).thenReturn(Optional.of(existing));

        service.delete(existing.getId(), user.getId());

        verify(repo).delete(existing);
    }

    @Test
    void delete_throwsAppException_whenNotFound() {
        when(repo.findByIdAndUserId(99L, user.getId())).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> service.delete(99L, user.getId()));
        verify(repo, never()).delete(any());
    }
}
