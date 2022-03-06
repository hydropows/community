package zhuke.manong.community.exception;
/********************************************************************************
 * Purpose: 码农社区的异常处理字段封装
 * Author: Caijinbang
 * Created Date:2021-10-30
 * Modify Description:
 * 1. 新增码农社区的异常处理字段封装 (Caijinbang 2021-10-30)
 ** *****************************************************************************/
public class CustomizeException extends RuntimeException{
    //异常信息
    private String message;
    //异常code
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
