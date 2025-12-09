package com.project.moneymanager.controller;


import com.project.moneymanager.dto.ExpenseDto;
import com.project.moneymanager.service.ExpenseService;
import com.project.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDto> addExpense(@RequestBody ExpenseDto dto){
        ExpenseDto createdExpense = expenseService.addExpense(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
    }
}
