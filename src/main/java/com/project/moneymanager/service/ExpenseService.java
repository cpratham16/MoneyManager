package com.project.moneymanager.service;


import com.project.moneymanager.dto.ExpenseDto;
import com.project.moneymanager.entity.CategoryEntity;
import com.project.moneymanager.entity.ExpenseEntity;
import com.project.moneymanager.entity.ProfileEntity;
import com.project.moneymanager.repository.CategoryRepository;
import com.project.moneymanager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;;

    //Add new expense to database
    public ExpenseDto addExpense(ExpenseDto dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(()-> new RuntimeException("Category not found with id: " + dto.getCategoryId()));

        ExpenseEntity newExpense = toEntity(dto, profile, category);
        newExpense = (ExpenseEntity) expenseRepository.save(newExpense);
        return toDto(newExpense);
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
}
