package zhuke.manong.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zhuke.manong.community.cache.HotTagCache;
import zhuke.manong.community.dto.PaginationDTO;
import zhuke.manong.community.service.QuestionService;

import java.util.List;

/********************************************************************************
 * Purpose:跳转至论坛主页并显示论坛内容
 * Author: Caijinbang
 * Created Date:2021-10-10
 * Modify Description:
 * 1. 跳转至论坛主页并显示论坛内容 (Caijinbang 2021-10-10)
 * 2. 增加论坛内容分页 (Caijinbang 2021-10-20)
 * 3. 新增搜索功能 (Caijinbang 2022-2-24)
 * 4. 新增热门标签功能 (Caijinbang 2022-2-26)
 * 5. 新增通过标签查询对应帖子功能 (Caijinbang 2022-2-27)
 ** *****************************************************************************/
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

    /*******************************************************************************
     * Purpose: 通过传参对论坛的帖子进行分页展示
     * Param: model 传回 index 主页的对象，page 为 index 主页的页数，size 为主页每页展示数
     *        search 为 搜索内容，tag 为 搜索的标签
     * Return: string 返回 index 论坛主页
     ********************************************************************************/
    @RequestMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag) {
        //获取论坛首页的对象字段
        PaginationDTO pagination = questionService.list(search,tag,page,size);
        List<String> tags = hotTagCache.getHots();
        model.addAttribute("pagination",pagination);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", tags);
        model.addAttribute("search", search);
        if(StringUtils.isNotBlank(search)){
            model.addAttribute("sectionName","搜索结果 "+search);
        }else if(StringUtils.isNotBlank(tag)){
            model.addAttribute("sectionName",tag);
        }else{
            model.addAttribute("sectionName","发现");
        }
        return "index";
    }
}
