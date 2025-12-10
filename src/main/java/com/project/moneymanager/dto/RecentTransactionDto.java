package com.project.moneymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RecentTransactionDto {

    private Long id;
    private Long profileId;
    private String icon;
    private String name;
    private BigDecimal amount;
    private String type; // "INCOME" or "EXPENSE"
    private LocalDate date;
    private LocalDateTime createdAt;
    private  LocalDateTime updatedAt;


}
