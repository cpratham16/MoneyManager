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

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class IncomeService {


    private final IncomeRepository incomeRepository;
    private final CategoryService categoryService;
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;;


    //Retrieve income for current month/based on the start and end date
    public List<IncomeDto> getCurrentMonthIncomeForProfile(){
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        List<IncomeEntity> incomes = incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return incomes.stream().map(this::toDto).toList();
    }




    //Add new income to database
    public IncomeDto addIncome(IncomeDto dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category not found with id: " + dto.getCategoryId()));

        IncomeEntity newIncome = toEntity(dto, profile, category);
        newIncome = (IncomeEntity) incomeRepository.save(newIncome);
        return toDto(newIncome);
    }

    //Delete income by id for current profile
    public void deleteIncomeById(Long incomeId){
        ProfileEntity profile = profileService.getCurrentProfile();
        IncomeEntity income = incomeRepository.findById(incomeId)
                .orElseThrow(()-> new RuntimeException("Income not found with id: " + incomeId));
        if(!income.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("Income does not belong to the current profile");
        }
        incomeRepository.deleteById(incomeId);
    }


    //Get latest 5 incomes for current profile
    public List<IncomeDto> getLatest5IncomesForProfile(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> incomes = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return incomes.stream().map(this::toDto).toList();
    }

    //Get total income amount for current profile
    public java.math.BigDecimal getTotalIncomeAmountForProfile(){
        ProfileEntity profile = profileService.getCurrentProfile();
        java.math.BigDecimal totalAmount = incomeRepository.findTotalAmountByProfileId(profile.getId());
        return totalAmount != null ? totalAmount : java.math.BigDecimal.ZERO;
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
