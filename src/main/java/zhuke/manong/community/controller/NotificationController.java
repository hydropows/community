package zhuke.manong.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import zhuke.manong.community.dto.NotificationDTO;
import zhuke.manong.community.enums.NotificationTypeEnum;
import zhuke.manong.community.model.User;
import zhuke.manong.community.service.NotificationService;

import javax.servlet.http.HttpServletRequest;
/********************************************************************************
 * Purpose: 阅读最新回复帖子信息
 * Author: Caijinbang
 * Created Date:2022-2-22
 * Modify Description:
 * 1. 实现跳转用户个人信息界面的最新回复帖子信息 (Caijinbang 2022-2-22)
 ** *****************************************************************************/
@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    /*************************************************************************************************
     * Purpose: 阅读最新回复帖子信息
     * Param: id 为 阅读的帖子的唯一标识
     * Return: string 返回 帖子详细页面
     *************************************************************************************************/
    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id) {
        //从 session 中获取用户个人信息
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        //更新未读通知为已读
        NotificationDTO notificationDTO = notificationService.read(id, user);

        //跳转至最新回复帖子界面
        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()
                || NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()) {
            return "redirect:/question/" + notificationDTO.getOuterid();
        } else {
            return "redirect:/";
        }
    }
}
