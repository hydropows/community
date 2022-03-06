package zhuke.manong.community.dto;

import lombok.Data;
import zhuke.manong.community.model.User;
/********************************************************************************
 * Purpose:封装论坛社区主页 帖子问题 的字段
 * Author: Caijinbang
 * Created Date:2021-10-18
 * Modify Description:
 * 1. 添加论坛社区主页 帖子问题 的字段 (Caijinbang 2021-10-18)
 ** *****************************************************************************/
public class QuestionDTO {
    //帖子id
    private Long id;
    //帖子标题
    private String title;
    //帖子描述
    private String description;
    //帖子标签
    private String tag;
    //创建时间
    private Long gmtCreate;
    //修改时间
    private Long gmtModified;
    //创建者id
    private Long creator;
    //浏览数
    private Integer viewCount;
    //评论数
    private Integer commentCount;
    //点赞数
    private Integer likeCount;
    //用户对象
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
