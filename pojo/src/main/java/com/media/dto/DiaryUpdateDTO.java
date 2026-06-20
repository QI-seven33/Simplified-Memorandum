package com.media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class DiaryUpdateDTO implements Serializable {


    private String title;
    private String content;
    private LocalDate date;
}
