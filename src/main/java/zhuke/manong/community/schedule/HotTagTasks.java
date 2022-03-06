package zhuke.manong.community.schedule;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zhuke.manong.community.cache.HotTagCache;
import zhuke.manong.community.mapper.QuestionMapper;
import zhuke.manong.community.model.Question;
import zhuke.manong.community.model.QuestionExample;

import java.util.*;

/********************************************************************************
 * Purpose: 热门话题标签计时器
 * Author: Caijinbang
 * Created Date:2022-2-26
 * Modify Description:
 * 1. 新增热门话题标签计时器功能 (Caijinbang 2022-2-26)
 ** *****************************************************************************/
@Component
@Slf4j
public class HotTagTasks {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private HotTagCache hotTagCache;

    /*******************************************************************************
     * Purpose: 热门话题标签计时器
     * Param: model 传回 index 主页的对象，page 为 index 主页的页数，size 为主页每页展示数
     *        search 为 搜索内容，tag 为 搜索的标签
     * Return: string 返回 index 论坛主页
     ********************************************************************************/
    //每三小时更新一次
    @Scheduled(fixedRate = 1000 * 60 * 60 * 3)
    public void hotTagSchedule() {
        //热门话题初始页
        int offset = 0;
        //热门标签数
        int limit = 20;
        log.info("hotTagSchedule start {}", new Date());
        List<Question> list = new ArrayList<>();

        Map<String, Integer> priorities = new HashMap<>();
        //动态更新热门标签优先级数
        while (offset == 0 || list.size() == limit) {
            //热门话题进行分页
            list = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, limit));
            for (Question question : list) {
                String[] tags = StringUtils.split(question.getTag(), ",");
                for (String tag : tags) {
                    Integer priority = priorities.get(tag);
                    if (priority != null) {
                        //优先级算法：问题数*5+评论数*2+阅读数
                        priorities.put(tag, priority + 5 + question.getCommentCount() * 2 + question.getViewCount());
                    } else {
                        //优先级算法：问题数*5+评论数*2+阅读数
                        priorities.put(tag, 5 + question.getCommentCount() * 2 + question.getViewCount());
                    }
                }
            }
            offset += limit;
        }
        //根据优先级数排序所有热门标签
        hotTagCache.updateTags(priorities);
        log.info("hotTagSchedule stop {}", new Date());
    }
}
