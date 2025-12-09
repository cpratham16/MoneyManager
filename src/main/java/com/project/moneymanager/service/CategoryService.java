package com.project.moneymanager.service;


import com.project.moneymanager.dto.CategoryDto;
import com.project.moneymanager.entity.CategoryEntity;
import com.project.moneymanager.entity.ProfileEntity;
import com.project.moneymanager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;

    //get categories for current user
    public List<CategoryDto> getCategoriesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfile_Id(profile.getId());
        return categories.stream().map(this::toDto).toList();
    }


    //get categories by type for current user
    public List<CategoryDto> getCategoriesByTypeForCurrentUser(String type){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByTypeAndProfile_Id(type, profile.getId());
        return categories.stream().map(this::toDto).toList();
    }

    //update category

    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity existingCategory = categoryRepository.findByIdAndProfile_Id(categoryId, profile.getId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));


        existingCategory.setName(categoryDto.getName());
        existingCategory.setIcon(categoryDto.getIcon());

        existingCategory = categoryRepository.save(existingCategory);
        return toDto(existingCategory);
    }


    //save Category
  public CategoryDto saveCategory(CategoryDto categoryDto){
    ProfileEntity profile = profileService.getCurrentProfile();
    if(categoryRepository.existsByNameAndProfile_Id(categoryDto.getName(), profile.getId())){
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with the same name already exists");
    }
    CategoryEntity newCategory = toEntity(categoryDto, profile);
    newCategory = categoryRepository.save(newCategory);
    return toDto(newCategory);
}



    //helped method
    private CategoryEntity toEntity(CategoryDto categoryDto, ProfileEntity profile){
        return CategoryEntity.builder()
                .profile(profile)
                .name(categoryDto.getName())
                .icon(categoryDto.getIcon())
                .type(categoryDto.getType())
                .build();
    }


    public CategoryDto toDto(CategoryEntity entity){
        return CategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .type(entity.getType())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .profileId(entity.getProfile()!=null ? entity.getProfile().getId() : null)
                .build();
    }
}
