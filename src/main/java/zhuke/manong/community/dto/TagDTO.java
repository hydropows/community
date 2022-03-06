package zhuke.manong.community.dto;

import lombok.Data;

import java.util.List;
/********************************************************************************
 * Purpose: 发布帖子里标签的封装类
 * Author: Caijinbang
 * Created Date:2022-2-14
 * Modify Description:
 * 1. 新增发布帖子里标签的字段信息 (Caijinbang 2022-2-14)
 ** *****************************************************************************/
@Data
public class TagDTO {
    //类别名
    private String categoryName;
    //类别下的标签集合
    private List<String> tags;
}
