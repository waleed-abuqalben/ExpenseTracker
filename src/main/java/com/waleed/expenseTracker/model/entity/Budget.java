package com.waleed.expenseTracker.model.entity;

import com.waleed.expenseTracker.enums.BudgetStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.waleed.expenseTracker.enums.BudgetStatus.ACTIVE;

@Entity
@Table(
        name = "budgets",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "month", "year"})
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int month;
    private int year;

    @Enumerated(EnumType.STRING)
    private BudgetStatus status = ACTIVE;

    private Double totalIncome;
    private Double totalExpense;
    private Double netBalance;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // One budget period can have many transactions
    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();
}
