package com.media.server.service;


import com.media.dto.DiaryDTO;
import com.media.dto.DiaryUpdateDTO;
import com.media.entity.Diary;
import com.media.vo.DiaryVO;

import java.util.List;

public interface DiaryService {

    //查询所有日记
    List<Diary> listByUserId(Long userId);

    //查询日记内容
    DiaryVO getDiaryContentById(Long id);

    //创建新的日记
    DiaryVO createDiary(DiaryDTO diaryDTO, Long userId);

    //更新日记
    DiaryVO updateDiary(Long id, DiaryUpdateDTO diaryUpdateDTO, Long userId);

    //删除日记
    void deleteDiary(Long id, Long userId);
}
