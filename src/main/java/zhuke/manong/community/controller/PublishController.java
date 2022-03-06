package zhuke.manong.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zhuke.manong.community.cache.TagCache;
import zhuke.manong.community.dto.QuestionDTO;
import zhuke.manong.community.model.Question;
import zhuke.manong.community.model.User;
import zhuke.manong.community.service.QuestionService;

import javax.servlet.http.HttpServletRequest;

/********************************************************************************
 * Purpose: 码农社区发布的控制层类
 * Author: Caijinbang
 * Created Date:2021-10-16
 * Modify Description:
 * 1. 码农社区 发布 界面的跳转 登录 (Caijinbang 2021-10-16)
 ** *****************************************************************************/
@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    /*******************************************************************************
     * Purpose: 用户编辑自己的帖子，将帖子上的数据回显到发布界面中
     * Param: id 帖子编号
     * Return: String 返回 index 论坛主页
     ********************************************************************************/
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    /*************************************************************************************************
     * Purpose: 码农社区 首页 点击发布文章后跳转 论坛发帖页面
     * Return: String 返回 论坛发帖页面
     *************************************************************************************************/
    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    /*******************************************************************************
     * Purpose: 通过传参对帖子进行发布
     * Param: title 帖子标题，description 帖子描述，tag 帖子标签，id 帖子编号
     ********************************************************************************/
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Long id,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.get());
        //标签为空
        if (title == null || title==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        //描述为空
        if (description == null || description==""){
            model.addAttribute("error","帖子描述补充不能为空");
            return "publish";
        }
        //标签为空
        if (tag == null || tag==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        //检验标签是否符合规定
        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签:" + invalid);
            return "publish";
        }

        //获取用户 session
        User user = (User)request.getSession().getAttribute("user");
        //用户未登录
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        //新建 Question 对象，用于插入或更新数据库里帖子问题信息
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        //判断帖子问题是否存在
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
