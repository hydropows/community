package zhuke.manong.community.enums;
/********************************************************************************
 * Purpose: 封装回复评论的类型（1.一级评论 2.二级评论）
 * Author: Caijinbang
 * Created Date:2021-11-21
 * Modify Description:
 * 1. 封装回复评论的类型（1.一级评论 2.二级评论） (Caijinbang 2021-11-21)
 ** *****************************************************************************/
public enum CommentTypeEnum {
    //1. 回复帖子问题的评论
    QUESTION(1),
    //2. 回复评论的二级评论
    COMMENT(2);
    private Integer type;

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.getType() == type) {
                return true;
            }
        }

        return false;
    }
}
