package com.clicKart.user.dto;



import java.time.LocalDateTime;

import com.clicKart.common.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private Role role;

    private Boolean enabled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}