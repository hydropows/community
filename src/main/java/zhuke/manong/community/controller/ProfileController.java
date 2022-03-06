package zhuke.manong.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import zhuke.manong.community.dto.PaginationDTO;
import zhuke.manong.community.model.User;
import zhuke.manong.community.service.NotificationService;
import zhuke.manong.community.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
/********************************************************************************
 * Purpose: 跳转至用户个人信息界面
 * Author: Caijinbang
 * Created Date:2021-10-23
 * Modify Description:
 * 1. 跳转至用户个人的所有提问 (Caijinbang 2021-10-23)
 * 2. 跳转至用户未读通知（最新回复） (Caijinbang 2022-2-18)
 * 3. 新增未读通知查询分页 (Caijinbang 2022-2-20)
 ** *****************************************************************************/
@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    /*************************************************************************************************
     * Purpose: 码跳转至用户个人信息界面
     * Param: action 为 需要查询哪个个人信息界面，page 为 页数，size 为 每页展示数。
     * Return: string 返回 用户个人指定信息界面
     *************************************************************************************************/
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name="action")String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size){

        User user = (User)request.getSession().getAttribute("user");
        //判断用户是否登录
        if (user==null){
            return "redirect:/";
        }
        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的帖子");
            PaginationDTO paginationDTO = questionService.list(user.getId(),page,size);
            model.addAttribute("pagination",paginationDTO);
        }else if("replies".equals(action)){
            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            model.addAttribute("section","replies");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName","最新回复");
        }

        return "profile";
    }
}
