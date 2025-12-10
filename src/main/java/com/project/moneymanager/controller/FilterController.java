package com.project.moneymanager.controller;


import com.project.moneymanager.dto.ExpenseDto;
import com.project.moneymanager.dto.FilterDto;
import com.project.moneymanager.dto.IncomeDto;
import com.project.moneymanager.service.ExpenseService;
import com.project.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filters")
public class FilterController {

    private final IncomeService incomeService;
    private  final ExpenseService expenseService;
@PostMapping
public ResponseEntity<?>filterTransactions(@RequestBody FilterDto filter){
    // Implement filtering logic here

    LocalDate startDate = filter.getStartDate()!=null ? filter.getStartDate() : LocalDate.MIN;
    LocalDate endDate = filter.getEndDate()!=null ? filter.getEndDate() : LocalDate.now();
    String keyword = filter.getKeyword()!=null ? filter.getKeyword() : "";
    String sortField = filter.getSortField()!=null ? filter.getSortField() : "date";
    Sort.Direction direction ="DESC".equalsIgnoreCase(filter.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort sort = Sort.by(direction,sortField);
    if("income".equalsIgnoreCase(filter.getType())){
        // Filter incomes based on criteria
        List<IncomeDto> incomes =incomeService.filterIncomes(startDate,endDate,keyword,sort);
        return ResponseEntity.ok(incomes);

    }else if("expense".equalsIgnoreCase(filter.getType())) {
        // Filter expenses based on criteria
        List<ExpenseDto> expenses = expenseService.filterExpenses(startDate, endDate, keyword,sort);
        return ResponseEntity.ok(expenses);
    }
    else {
        return ResponseEntity.badRequest().body("Invalid transaction type");
    }
}}
