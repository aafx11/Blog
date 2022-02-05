package com.djh.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.DTO.commentDto;
import com.djh.common.lang.Result;
import com.djh.common.vo.commentFormVO;
import com.djh.entity.Commentform;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author djh
 * @since 2021-09-11
 */
@RestController
@RequestMapping("/commentform")
public class CommentformController extends BaseController {

    @GetMapping("/getCommentList")
    public Result getCommentList(Long articleId){
        List<commentFormVO> commentList = commentformService.getCommentTreeByArticleId(articleId);
        return Result.success(commentList);
    }


    @PostMapping("/submitComment")
    public Result submitComment(@RequestBody commentDto commentDto){
        System.out.println("获取的评论"+commentDto);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
           if (commentformService.saveComment(auth,commentDto)){
               return Result.success("评论成功");
           }else {
               return Result.fail("评论失败");
           }
        } else {
            return Result.fail("请先登录");
        }

    }

    // 获取最新评论
    @GetMapping("/getLatestComments")
    public Result getLatestComments(){
        List<commentFormVO> latestCommentLis = commentformService.getLatestCommentLis();
        return Result.success(latestCommentLis);
    }

    // 获取所有评论
    @PreAuthorize("hasAnyAuthority('information:comment')")
    @GetMapping("/getCommentListByPage")
    public Result getCommentListByPage(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size){

        Page<commentFormVO> commentListByPage = commentformService.getCommentListByPage(new Page<>(current, size));

        return Result.success(commentListByPage);
    }

    // 修改文章评论的状态
    @PreAuthorize("hasAnyAuthority('comment:updateState')")
    @PostMapping("/updateCommentState")
    public Result updateCommentState(@RequestBody Long commentId){

        Boolean aBoolean = commentformService.updateCommentState(commentId);
        if (aBoolean){
            return Result.success("修改成功");

        }else {
            return Result.fail("修改失败");

        }
    }
}
