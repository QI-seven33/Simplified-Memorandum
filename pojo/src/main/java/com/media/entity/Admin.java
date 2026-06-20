package com.media.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String password;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

}
