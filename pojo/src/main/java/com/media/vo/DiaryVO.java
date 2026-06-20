package com.media.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class DiaryVO implements Serializable {

    private Long id;
    private Integer userId;
    private String title;
    private String content;
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
