package zhuke.manong.community.dto;

import lombok.Data;

/********************************************************************************
 * Purpose: 封装热门话题标签的字段信息
 * Author: Caijinbang
 * Created Date:2022-2-26
 * Modify Description:
 * 1. 新增热门话题标签的字段信息 (Caijinbang 2022-2-26)
 ** *****************************************************************************/
@Data
public class HotTagDTO implements Comparable{
    //标签名
    private String name;
    //标签优先级数
    private Integer priority;

    @Override
    public int compareTo(Object o) {
        return this.getPriority() - ((HotTagDTO) o).getPriority();
    }
}
