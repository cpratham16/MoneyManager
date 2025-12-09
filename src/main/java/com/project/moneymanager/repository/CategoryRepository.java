package com.project.moneymanager.repository;

import com.project.moneymanager.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {

    //select * from tbl_category where profile_id=?
    List<CategoryEntity> findByProfile_Id(Long profileId);

    //select * from tbl_category where id=? and profile_id=?
    Optional<CategoryEntity> findByIdAndProfile_Id(Long id, Long profileId);

    //select * from tbl_category where type=? and profile_id=?
    List<CategoryEntity> findByTypeAndProfile_Id(String type, Long profileId);

    //select exists from tbl_category where name=? and profile_id=?
    Boolean existsByNameAndProfile_Id(String name, Long profileId);
}
