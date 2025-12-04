package com.waleed.expenseTracker.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double amount;
    private String description;
    private LocalDate issuedAt = LocalDate.now();

    // Many transactions belong to one category
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Many transactions belong to one budget period
    @ManyToOne
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;
}
