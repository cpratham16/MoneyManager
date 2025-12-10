package com.project.moneymanager.service;


import com.project.moneymanager.dto.ExpenseDto;
import com.project.moneymanager.entity.CategoryEntity;
import com.project.moneymanager.entity.ExpenseEntity;
import com.project.moneymanager.entity.ProfileEntity;
import com.project.moneymanager.repository.CategoryRepository;
import com.project.moneymanager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;


    //Retrieve expenses for current month/based on the start and end date
    public List<ExpenseDto> getCurrentMonthExpensesForProfile(){
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        List<ExpenseEntity> expenses = expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return expenses.stream().map(this::toDto).toList();
    }


    //Delete expense by id for current profile
    public void deleteExpenseById(Long expenseId){
        ProfileEntity profile = profileService.getCurrentProfile();
        ExpenseEntity expense = expenseRepository.findById(expenseId)
                .orElseThrow(()-> new RuntimeException("Expense not found with id: " + expenseId));
        if(!expense.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("Expense does not belong to the current profile");
    }
        expenseRepository.deleteById(expenseId);
    }

    //Get latest 5 expenses for current profile
    public List<ExpenseDto> getLatest5ExpensesForProfile(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> expenses = expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return expenses.stream().map(this::toDto).toList();
    }

    //Get total expense amount for current profile
    public BigDecimal getTotalExpenseAmountForProfile(){
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal totalAmount = expenseRepository.findTotalAmountByProfileId(profile.getId());
        return totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }



    //Add new expense to database
    public ExpenseDto addExpense(ExpenseDto dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(()-> new RuntimeException("Category not found with id: " + dto.getCategoryId()));

        ExpenseEntity newExpense = toEntity(dto, profile, category);
        newExpense = expenseRepository.save(newExpense);
        return toDto(newExpense);
    }


    //filter expenses based on date range and keyword
    public List<ExpenseDto> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> expenses = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
                profile.getId(),
                startDate,
                endDate,
                keyword,
                sort
        );
        return expenses.stream().map(this::toDto).toList();
    }



    //helper method
    private ExpenseEntity toEntity(ExpenseDto dto, ProfileEntity profile, CategoryEntity category){
        return ExpenseEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profile)
                .category(category)
                .build();
    }

    private ExpenseDto toDto(ExpenseEntity entity){
        return ExpenseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .amount(entity.getAmount())
                .date(entity.getDate())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : "N/A")
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    //Notification
    public List<ExpenseDto> getExpensesByDate(Long profileId, LocalDate date) {
        List<ExpenseEntity> list= expenseRepository.findByProfileIdAndDate(profileId, date);
        return list.stream().map(this::toDto).toList();
    }

}
