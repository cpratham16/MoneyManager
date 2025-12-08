package com.project.moneymanager.dto;

import java.time.LocalDateTime;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {
    
private Long id;
private String fullName;
private String email;
private String password;
private String profileImageUrl;
private LocalDateTime createdAt;
private LocalDateTime updatedAt;
}
