package com.media.server.mapper;

import com.media.entity.Diary;
import com.media.vo.DiaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DiaryMapper {


    List<Diary> listByUserId(Long userId);


    DiaryVO getDiaryContentById(Long id);


    void createDiary(Diary diary);





    int updateByIdAndId(Diary diary);


    Diary selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

}