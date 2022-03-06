package zhuke.manong.community.dto;

import lombok.Data;
import zhuke.manong.community.model.User;

/********************************************************************************
 * Purpose: 码农社区帖子的评论字段封装
 * Author: Caijinbang
 * Created Date:2022-1-18
 * Modify Description:
 * 1. 新增码农社区帖子的评论字段封装 (Caijinbang 2022-1-18)
 ** *****************************************************************************/
@Data
public class CommentDTO {
    //评论的唯一标识
    private Long id;
    //父类id（问题id 或 评论id）
    private Long parentId;
    //评论类型
    private Integer type;
    //评论人
    private Long commentator;
    //评论创建时间
    private Long gmtCreate;
    //评论修改时间
    private Long gmtModified;
    //评论点赞数
    private Long likeCount;
    //评论的回复数
    private Integer commentCount;
    //评论内容
    private String content;
    private User user;
}
