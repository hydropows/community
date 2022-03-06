package zhuke.manong.community.cache;

import lombok.Data;
import org.springframework.stereotype.Component;
import zhuke.manong.community.dto.HotTagDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/********************************************************************************
 * Purpose: 热门话题标签优先排序
 * Author: Caijinbang
 * Created Date:2022-2-26
 * Modify Description:
 * 1. 新增热门话题标签优先排序 (Caijinbang 2022-2-26)
 ** *****************************************************************************/
@Component
@Data
public class HotTagCache {
    private List<String> hots = new ArrayList<>();

    /*************************************************************************************************
     * Purpose: 热门话题标签缓存优先排序
     * Param: Map<String, Integer> tags 为 标签优先级数
     *************************************************************************************************/
    public void updateTags(Map<String, Integer> tags) {
        //优先队列的长度
        int max = 10;
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(max);

        tags.forEach((name, priority) -> {
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setName(name);
            hotTagDTO.setPriority(priority);
            //如果优先队列没满
            if (priorityQueue.size() < max) {
                priorityQueue.add(hotTagDTO);
            }
            //如果优先队列已满
            else {
                //获取热度最小的标签
                HotTagDTO minHot = priorityQueue.peek();
                //判断新增的热门标签热度是否大于弹出的热门标签
                if (hotTagDTO.compareTo(minHot) > 0) {
                    //弹出热度最小的标签
                    priorityQueue.poll();
                    //添加新的热度标签
                    priorityQueue.add(hotTagDTO);
                }
            }
        });

        //热门标签名的排序
        List<String> sortedTags = new ArrayList<>();

        HotTagDTO poll = priorityQueue.poll();
        while (poll != null) {
            sortedTags.add(0, poll.getName());
            poll = priorityQueue.poll();
        }
        hots = sortedTags;
    }
}
