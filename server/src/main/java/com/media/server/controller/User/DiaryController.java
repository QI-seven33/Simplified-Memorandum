package com.media.server.controller.User;

import com.media.common.context.BaseContext;
import com.media.common.properties.JwtProperties;
import com.media.common.result.Result;
import com.media.dto.DiaryDTO;
import com.media.dto.DiaryUpdateDTO;
import com.media.entity.Diary;
import com.media.server.service.DiaryService;
import com.media.server.service.UserService;
import com.media.vo.DiaryVO;
import com.media.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.media.common.utils.AliOssUtil;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/diary")
@CrossOrigin(origins = "http://localhost:5173")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;


    /**
     * 获取日记列表
     * 用于：左侧栏展示所有日记
     * 需要携带 JWT Token（由请求拦截器验证)
     * @param
     * @return
     */
    @GetMapping("/list")
    public Result<List<DiaryVO>> getDiaryList() {
        Long userId = BaseContext.getCurrentId();
        log.info("查询日记列表，userId为 {}", userId);
        //查询所有日记
        List<Diary> diaryList = diaryService.listByUserId(userId);
        // 4. 构建返回对象
        // 2. 转换为 VO（只返回前端需要的字段）
        List<DiaryVO> DiaryVo = diaryList.stream()
                .map(diary -> DiaryVO.builder()
                        .id(diary.getId())
                        .title(diary.getTitle())
                        .date(diary.getDate())
                        .content(diary.getContent())
                        .build())
                .collect(Collectors.toList());

        // 3. 返回成功结果
        return Result.success(DiaryVo);
    }


    @GetMapping("/detail/{id}")
    public Result<DiaryVO> getDiaryContent(@PathVariable Long id) {
        log.info("查询日记内容，userId为 {}", id);
        //查询日记内容
        DiaryVO diaryVO = diaryService.getDiaryContentById(id);
        // 3. 返回成功结果
        return Result.success(diaryVO);
    }

    @PostMapping("/create")
    public Result<DiaryVO> createNewDiary(@RequestBody DiaryDTO diaryDTO) {
        Long userId = BaseContext.getCurrentId();
        log.info("创建新的日记，用户Id为 {}", userId);

        if (userId == null) {
            return Result.error("未授权");
        }
        //创建日记内容
        DiaryVO diaryVO = diaryService.createDiary(diaryDTO,userId);
        // 3. 返回成功结果
        return Result.success(diaryVO);
    }

    @PutMapping("/update/{id}")
    public Result<DiaryVO> updateDiary(@PathVariable Long id, @RequestBody DiaryUpdateDTO diaryUpdateDTO) {
        Long userId = BaseContext.getCurrentId();
        log.info("正在更新日记，用户Id为 {}", userId);
        if (userId == null) {
            return Result.error("未授权");
        }
        DiaryVO diaryVO = diaryService.updateDiary(id,diaryUpdateDTO,userId);
        return Result.success(diaryVO);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteDiary(@PathVariable Long id) {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error("未授权");
        }
        diaryService.deleteDiary(id,userId);
        return Result.success();
    }




}