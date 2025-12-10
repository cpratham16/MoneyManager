package com.project.moneymanager.controller;


import com.project.moneymanager.dto.ExpenseDto;
import com.project.moneymanager.dto.IncomeDto;
import com.project.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDto> addIncome(@RequestBody IncomeDto dto){
        IncomeDto createdIncome = incomeService.addIncome(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIncome);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDto>> getMonthIncomes(){
        List<IncomeDto> incomes = incomeService.getCurrentMonthIncomeForProfile();
        return ResponseEntity.ok(incomes);
    }

    @DeleteMapping("/{incomeId}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long incomeId){
        incomeService.deleteIncomeById(incomeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
