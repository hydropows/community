package zhuke.manong.community.dto;

import lombok.Data;
/********************************************************************************
 * Purpose:封装问题搜索后的信息字段
 * Author: Caijinbang
 * Created Date:2022-2-24
 * Modify Description:
 * 1. 新增问题搜索后的信息字段 (Caijinbang 2022-2-24)
 ** *****************************************************************************/
@Data
public class QuestionQueryDTO {
    //查询内容
    private String search;
    private String sort;
    private Long time;
    //查询标签
    private String tag;
    //页数
    private Integer page;
    //每页大小
    private Integer size;
}
