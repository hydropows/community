package zhuke.manong.community.dto;

import lombok.Data;

/********************************************************************************
 * Purpose: 码农社区帖子的评论内容的封装类
 * Author: Caijinbang
 * Created Date:2022-1-18
 * Modify Description:
 * 1. 新增创建评论字段的封装 (Caijinbang 2022-1-18)
 ** *****************************************************************************/
@Data
public class CommentCreateDTO {
    //父类id（问题id 或 评论id）
    private Long parentId;
    //评论内容
    private String content;
    //评论类型
    private Integer type;
}
