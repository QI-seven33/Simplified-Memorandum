package com.media.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Diary implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private Integer userId;
    private String title;
    private String content;
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



}
