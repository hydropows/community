package zhuke.manong.community.enums;
/********************************************************************************
 * Purpose: 码农社区未读通知的类型枚举
 * Author: Caijinbang
 * Created Date:2022-2-20
 * Modify Description:
 * 1. 新增码码农社区未读通知的类型枚举 (Caijinbang 2022-2-20)
 ** *****************************************************************************/
public enum NotificationTypeEnum {
    REPLY_QUESTION(1, "回复了帖子"),
    REPLY_COMMENT(2, "回复了评论");
    private int type;
    private String name;


    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationTypeEnum(int status, String name) {
        this.type = status;
        this.name = name;
    }

    public static String nameOfType(int type) {
        for (NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
            if (notificationTypeEnum.getType() == type) {
                return notificationTypeEnum.getName();
            }
        }
        return "";
    }
}
