package zhuke.manong.community.dto;

import lombok.Data;
/********************************************************************************
 * Purpose:图片上传后数据的返回字段信息
 * Author: Caijinbang
 * Created Date:2022-2-23
 * Modify Description:
 * 1. 新增图片上传后数据的返回字段信息 (Caijinbang 2022-2-23)
 ** *****************************************************************************/
@Data
public class FileDTO {
    //成功或失败字段
    private int success;
    //返回信息
    private String message;
    //图片地址
    private String url;
}
