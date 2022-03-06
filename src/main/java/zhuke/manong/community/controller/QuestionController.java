package zhuke.manong.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import zhuke.manong.community.dto.CommentDTO;
import zhuke.manong.community.dto.QuestionDTO;
import zhuke.manong.community.enums.CommentTypeEnum;
import zhuke.manong.community.service.CommentService;
import zhuke.manong.community.service.QuestionService;

import java.util.List;

/********************************************************************************
 * Purpose: 码农社区帖子问题的控制层类
 * Author: Caijinbang
 * Created Date:2021-10-28
 * Modify Description:
 * 1. 点击论坛首页的帖子后跳转到其具体信息的界面 (Caijinbang 2021-10-28)
 * 2. 增加实现阅读数功能 (Caijinbang 2021-11-14)
 * 3. 新增实现回复列表功能 (Caijinbang 2022-1-18)·
 * 4. 新增实现帖子页面相关帖子的展示 (Caijinbang 2022-2-10)
 ** *****************************************************************************/
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    /*******************************************************************************
     * Purpose: 跳转码农社区帖子问题的详细信息界面
     * Param: id 为 帖子唯一标识
     * Return: string 返回 帖子问题的详细信息界面
     ********************************************************************************/
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                                                      Model model){
        //通过论坛帖子 Id 查找对应所有信息
        QuestionDTO questionDTO = questionService.getById(id);
        //查找相关帖子问题
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        //获取该帖子的所有评论
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);

        //累加阅读数
        questionService.incView(id);

        //帖子内容
        model.addAttribute("question",questionDTO);
        //评论
        model.addAttribute("comments", comments);
        //相关问题
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
