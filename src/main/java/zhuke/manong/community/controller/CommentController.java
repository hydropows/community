package zhuke.manong.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zhuke.manong.community.dto.CommentCreateDTO;
import zhuke.manong.community.dto.CommentDTO;
import zhuke.manong.community.dto.ResultDTO;
import zhuke.manong.community.enums.CommentTypeEnum;
import zhuke.manong.community.exception.CustomizeErrorCode;
import zhuke.manong.community.mapper.CommentMapper;
import zhuke.manong.community.model.Comment;
import zhuke.manong.community.model.User;
import zhuke.manong.community.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/********************************************************************************
 * Purpose:码农社区帖子的回复功能
 * Author: Caijinbang
 * Created Date:2021-11-21
 * Modify Description:
 * 1. 实现码农社区帖子的回复功能 (Caijinbang 2021-11-21)
 * 2. 新增码农社区帖子的二级评论 (Caijinbang 2021-11-28)
 ** *****************************************************************************/
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    /*************************************************************************************************
     * Purpose: 实现码农社区帖子的评论功能（回复）
     * Param: commentCreateDTO 为创建评论的字段
     * Return: ResultDTO 返回 JSON 数据
     *************************************************************************************************/
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        //判断用户还未登录
        if(user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        //用户回复内容为空
        if(commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        //传入回复的帖子 id
        comment.setParentId(commentCreateDTO.getParentId());
        //传入回复内容
        comment.setContent(commentCreateDTO.getContent());
        //传入回复类型（1.一级评论 2.二级评论）
        comment.setType(commentCreateDTO.getType());
        //传入评论创建时间
        comment.setGmtCreate(System.currentTimeMillis());
        //传入评论修改时间（创建评论时修改时间默认为创建时间）
        comment.setGmtModified(System.currentTimeMillis());
        //传入回复者 id
        comment.setCommentator(user.getId());
        //初始化点赞数
        comment.setLikeCount(0L);
        commentService.insert(comment,user);
        return ResultDTO.okOf();
    }

    /*************************************************************************************************
     * Purpose: 实现码农社区帖子中回复评论的二级评论查询
     * Param: id 为 评论的唯一标识
     * Return: ResultDTO<List<CommentDTO>> 为 返回的所有二级评论数据信息
     *************************************************************************************************/
    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
        //获取该评论的所有二级评论
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
