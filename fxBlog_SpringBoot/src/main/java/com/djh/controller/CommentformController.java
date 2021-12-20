package com.djh.controller;


import com.djh.common.DTO.commentDto;
import com.djh.common.lang.Result;
import com.djh.common.vo.commentFormVO;
import com.djh.entity.Commentform;
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
}
