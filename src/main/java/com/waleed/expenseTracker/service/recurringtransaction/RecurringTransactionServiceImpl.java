package com.waleed.expenseTracker.service.recurringtransaction;

import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.exception.AppException;
import com.waleed.expenseTracker.model.entity.Category;
import com.waleed.expenseTracker.model.entity.RecurringTransaction;
import com.waleed.expenseTracker.model.entity.User;
import com.waleed.expenseTracker.model.mappers.RecurringTransactionMapper;
import com.waleed.expenseTracker.model.request.recurringtransaction.CreateRecurringTransactionRequest;
import com.waleed.expenseTracker.model.request.recurringtransaction.UpdateRecurringTransactionRequest;
import com.waleed.expenseTracker.repository.RecurringTransactionRepository;
import com.waleed.expenseTracker.service.category.CategoryService;
import com.waleed.expenseTracker.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecurringTransactionServiceImpl implements RecurringTransactionService {
    private final RecurringTransactionRepository repo;
    private final CategoryService categoryService;
    private final UserService userService;
    private final RecurringTransactionMapper mapper;

    @Override
    public List<RecurringTransaction> findAll(Long userId, CategoryType type) {
        return type == null
                ? repo.findByUserId(userId)
                : repo.findByUserIdAndCategoryType(userId, type);
    }

    @Override
    public RecurringTransaction findById(Long id, Long userId) {
        return repo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new AppException(String.format("RecurringTransaction: %d not found for user: %d", id, userId)));
    }

    @Override
    @Transactional
    public RecurringTransaction create(CreateRecurringTransactionRequest request, Long userId) {
        log.info("About to Create RecurringTransaction: {} For User: {}", request, userId);
        Category category = categoryService.findById(request.categoryId(), userId);
        User user = userService.getUserById(userId);

        RecurringTransaction created = repo.save(RecurringTransaction.builder()
                .name(request.name())
                .amount(request.amount())
                .description(request.description())
                .active(true)
                .category(category)
                .user(user)
                .build());

        log.info("RecurringTransaction Created Successfully {}", mapper.toDto(created));
        return created;
    }

    @Override
    @Transactional
    public RecurringTransaction update(Long id, UpdateRecurringTransactionRequest request, Long userId) {
        log.info("About to Update RecurringTransaction: {} For User: {}", id, userId);
        Category category = categoryService.findById(request.categoryId(), userId);
        RecurringTransaction existing = findById(id, userId);

        existing.setName(request.name());
        existing.setAmount(request.amount());
        existing.setDescription(request.description());
        existing.setActive(request.active());
        existing.setCategory(category);

        RecurringTransaction updated = repo.save(existing);
        log.info("RecurringTransaction Updated Successfully {}", mapper.toDto(updated));
        return updated;
    }

    @Override
    @Transactional
    public RecurringTransaction updateActivation(Long id, boolean active, Long userId) {
        log.info("About to set RecurringTransaction: {} active={} For User: {}", id, active, userId);
        RecurringTransaction existing = findById(id, userId);
        existing.setActive(active);
        RecurringTransaction updated = repo.save(existing);
        log.info("RecurringTransaction Activation Updated Successfully {}", mapper.toDto(updated));
        return updated;
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        log.info("About to Delete RecurringTransaction: {} For User: {}", id, userId);
        RecurringTransaction existing = findById(id, userId);
        repo.delete(existing);
        log.info("RecurringTransaction Deleted Successfully {}", id);
    }
}
