package zhuke.manong.community.dto;

import lombok.Data;
/********************************************************************************
 * Purpose:返回至前端通知信息数据的封装类
 * Author: Caijinbang
 * Created Date:2022-2-18
 * Modify Description:
 * 1. 封装前端通知信息数据的字段 (Caijinbang 2022-2-18)
 ** *****************************************************************************/
@Data
public class NotificationDTO {
    //通知id
    private Long id;
    //通知创建时间
    private Long gmtCreate;
    //通知状态（未读 or 已读）
    private Integer status;
    //评论回复者 id
    private Long notifier;
    //评论回复者 名字
    private String notifierName;
    //评论回复的帖子标题
    private String outerTitle;
    //评论回复的帖子id
    private Long outerid;
    //评论回复类型名 (0:问题  1:评论)
    private String typeName;
    //评论回复类型（0 or 1）
    private Integer type;
}
