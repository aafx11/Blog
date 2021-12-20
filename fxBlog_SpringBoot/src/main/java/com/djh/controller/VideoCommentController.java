package com.djh.controller;


import com.djh.common.DTO.VideoCommentDto;
import com.djh.common.DTO.commentDto;
import com.djh.common.lang.Result;
import com.djh.common.vo.VideoCommentVO;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author djh
 * @since 2021-10-06
 */
@RestController
@RequestMapping("/videoComment")
public class VideoCommentController extends BaseController {

    /**
     * @name: 获取视频评论通过视频id
     * @description:
     * @param videoId
     * @return com.djh.common.lang.Result
     */
    @GetMapping("/getVideoCommentByVideoId")
    public Result getVideoCommentByVideoId(Long videoId){

        List<VideoCommentVO> commentVOList = videoCommentService.getCommentTreeByVideoId(videoId);
        return Result.success(commentVOList);
    }

    /**
     * @name: 提交视频评论
     * @description:
     * @param videoCommentDto
     * @return com.djh.common.lang.Result
     */
    @PostMapping("/submitComment")
    public Result submitComment(@RequestBody VideoCommentDto videoCommentDto){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            if (videoCommentService.saveComment(auth,videoCommentDto)){
                return Result.success("评论成功");
            }else {
                return Result.fail("评论失败");
            }
        } else {
            return Result.fail("请先登录");
        }

    }
}
