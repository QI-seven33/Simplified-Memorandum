package com.media.server.service.impl;

import com.media.common.constant.MessageConstant;
import com.media.common.exception.BaseException;
import com.media.common.result.Result;
import com.media.dto.DiaryDTO;
import com.media.dto.DiaryUpdateDTO;
import com.media.entity.Diary;
import com.media.server.mapper.DiaryMapper;
import com.media.server.service.DiaryService;
import com.media.vo.DiaryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.MessageConstraintException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.View;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;
    @Autowired
    private View error;

    public List<Diary> listByUserId(Long userId) {
        List<Diary> diaryList = diaryMapper.listByUserId(userId);
        return diaryList;
    }

    /**
     * 根据id查询日记内容
     * @param id
     * @return
     */
    public DiaryVO getDiaryContentById(Long id) {
        DiaryVO diaryVO = diaryMapper.getDiaryContentById(id);

        return diaryVO;
    }

    /**
     * 创建新的日记
     *
     * @param diaryDTO
     * @param userId
     * @return
     */
    @Transactional
    public DiaryVO createDiary(DiaryDTO diaryDTO, Long userId) {

         Diary diary = Diary.builder()
                .userId(userId.intValue())
                .title(diaryDTO.getTitle())
                .content(diaryDTO.getContent())
                .date(diaryDTO.getDate())
                .createdAt(LocalDateTime.now())  // ✅ 设置创建时间
                .updatedAt(LocalDateTime.now())  // ✅ 设置更新时间
                .build();
        diaryMapper.createDiary(diary);
        return DiaryVO.builder()
                .id(diary.getId())          // ✅ 使用 MyBatis 自动生成的 ID
                .title(diary.getTitle())
                .content(diary.getContent())
                .date(LocalDate.parse(diary.getDate().toString()))
                .build();
    }

    @Transactional
    public DiaryVO updateDiary(Long id, DiaryUpdateDTO diaryUpdateDTO, Long userId) {

        // 1. 构建 Diary 对象（只包含需要更新的字段）
        Diary diary = Diary.builder()
                .id(id)
                .userId(Math.toIntExact(userId))
                .title(diaryUpdateDTO.getTitle())
                .content(diaryUpdateDTO.getContent())
                .updatedAt(LocalDateTime.now())
                .build();
        int affectedRows = diaryMapper.updateByIdAndId(diary);

        // 3. 判断是否更新成功
        if (affectedRows == 0) {
            throw new RuntimeException("日记不存在或无权限访问");
        }
        Diary updatedDiary = diaryMapper.selectByIdAndUserId(id,userId);

        // 5. 返回 VO
        return DiaryVO.builder()
                .id(updatedDiary.getId())
                .title(updatedDiary.getTitle())
                .content(updatedDiary.getContent())
                .date(LocalDate.parse(updatedDiary.getDate().toString()))
                .build();
    }

    @Transactional
    public void deleteDiary(Long id, Long userId) {
        //判断当前日记是否都能够删除
        Diary Diary = diaryMapper.selectByIdAndUserId(id,userId);
        if (Diary.getId() == null) {
            throw new BaseException(MessageConstant.DIARY_STATUS_EXCEPTION);
        }
        //删除日记
        diaryMapper.deleteByIdAndUserId(id,userId);
    }


}
