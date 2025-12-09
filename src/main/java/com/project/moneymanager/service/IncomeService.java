package com.project.moneymanager.service;

import com.project.moneymanager.dto.IncomeDto;
import com.project.moneymanager.entity.CategoryEntity;
import com.project.moneymanager.entity.ExpenseEntity;
import com.project.moneymanager.entity.IncomeEntity;
import com.project.moneymanager.entity.ProfileEntity;
import com.project.moneymanager.repository.CategoryRepository;
import com.project.moneymanager.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class IncomeService {


    private final IncomeRepository incomeRepository;
    private final CategoryService categoryService;
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;;

    //Add new income to database
    public IncomeDto addIncome(IncomeDto dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category not found with id: " + dto.getCategoryId()));

        IncomeEntity newIncome = toEntity(dto, profile, category);
        newIncome = (IncomeEntity) incomeRepository.save(newIncome);
        return toDto(newIncome);
    }




    //helper method
    private IncomeEntity toEntity(IncomeDto dto, ProfileEntity profile, CategoryEntity category){
        return IncomeEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profile)
                .category(category)
                .build();
    }

    private IncomeDto toDto(IncomeEntity entity){
        return IncomeDto.builder()
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
