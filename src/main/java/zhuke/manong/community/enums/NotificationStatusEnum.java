package zhuke.manong.community.enums;
/********************************************************************************
 * Purpose:最新回复通知类型枚举
 * Author: Caijinbang
 * Created Date:2022-2-20
 * Modify Description:
 * 1. 新增最新回复通知类型 (Caijinbang 2022-2-20)
 ** *****************************************************************************/
public enum NotificationStatusEnum {
    UNREAD(0), READ(1);
    private int status;

    public int getStatus() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
