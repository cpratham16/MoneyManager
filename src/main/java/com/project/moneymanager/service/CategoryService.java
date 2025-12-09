package com.project.moneymanager.service;


import com.project.moneymanager.dto.CategoryDto;
import com.project.moneymanager.entity.CategoryEntity;
import com.project.moneymanager.entity.ProfileEntity;
import com.project.moneymanager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;

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
