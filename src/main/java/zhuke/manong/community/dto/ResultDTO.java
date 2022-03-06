package zhuke.manong.community.dto;

import lombok.Data;
import zhuke.manong.community.exception.CustomizeErrorCode;
import zhuke.manong.community.exception.CustomizeException;
/********************************************************************************
 * Purpose: 回复评论后返回的封装类
 * Author: Caijinbang
 * Created Date:2021-11-21
 * Modify Description:
 * 1. 回复评论后返回的封装类 (Caijinbang 2021-11-21)
 ** *****************************************************************************/
@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T Data;

    //用于获取评论的报错信息
    public static ResultDTO errorOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(), e.getMessage());
    }

    public static ResultDTO okOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static <T> ResultDTO okOf(T t) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }
}
