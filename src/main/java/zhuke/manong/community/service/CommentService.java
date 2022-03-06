package zhuke.manong.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zhuke.manong.community.dto.CommentDTO;
import zhuke.manong.community.enums.CommentTypeEnum;
import zhuke.manong.community.enums.NotificationStatusEnum;
import zhuke.manong.community.enums.NotificationTypeEnum;
import zhuke.manong.community.exception.CustomizeErrorCode;
import zhuke.manong.community.exception.CustomizeException;
import zhuke.manong.community.mapper.*;
import zhuke.manong.community.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
/********************************************************************************
 * Purpose: 码农社区的评论的业务逻辑
 * Author: Caijinbang
 * Created Date:2021-11-21
 * Modify Description:
 * 1. 新增码农社区插入回复评论的业务逻辑 (Caijinbang 2021-11-21)
 * 2. 新增码农社区细分评论类型 (Caijinbang 2021-12-18)
 * 3. 新增实现回复列表功能的业务逻辑 (Caijinbang 2022-1-18)
 * 4. 新增未读通知提醒 (Caijinbang 2022-2-20)
 ** *****************************************************************************/
@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    /********************************************************************************
     * Purpose: 码农社区的评论的业务逻辑
     * Param: comment 为 评论的字段信息，commentator 为 评论回复的用户字段信息
     * Modify Description:
     * 1. 新增码农社区插入回复评论的业务逻辑 (Caijinbang 2021-11-21)
     * 2. 新增码农社区细分评论类型 (Caijinbang 2021-12-18)
     * 3. 新增创建未读通知的业务逻辑 (Caijinbang 2022-2-20)
     ** *****************************************************************************/
    @Transactional
    public void insert(Comment comment,User commentator) {
        //未选中任何帖子问题或评论进行回复
        if(comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        //评论类型错误或不存在
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        //判断是否为回复评论
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            // 获取回复评论的对象
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            // 回复的评论不存在
            if(dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            // 获取评论对应帖子的 id
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            // 帖子问题不存在
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //插入评论
            commentMapper.insert(comment);

            // 增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            // 创建通知
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());

        }else{
            //回复帖子问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            comment.setCommentCount(0);
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);

            // 创建通知
            createNotify(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
    }

    /*************************************************************************************************
     * Purpose: 创建未读通知
     *************************************************************************************************/
    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        if (receiver == comment.getCommentator()) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }


    /********************************************************************************
     * Purpose: 实现回复列表功能的业务逻辑
     * Param: id 为 评论的唯一标识 或 帖子问题的唯一标识
     *        type 为查询的类型（COMMENT：查询指定评论的二级评论   QUESTION：查询帖子问题的所有评论）
     * Return: List<CommentDTO> 为 返回的评论集合
     ** *****************************************************************************/
    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        //通过传入的帖子问题或评论id查询对应的所有评论信息
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        //查询结果降序排列
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if (comments.size() == 0) {
            return new ArrayList<>();
        }

        // 获取去重的评论人（Java8 Stream）
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentators);

        // 获取评论人并转换为 Map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        //（Java8 Stream）
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        // 转换 comment 为 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
