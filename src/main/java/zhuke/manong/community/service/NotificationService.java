package zhuke.manong.community.service;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhuke.manong.community.dto.NotificationDTO;
import zhuke.manong.community.dto.PaginationDTO;
import zhuke.manong.community.enums.NotificationStatusEnum;
import zhuke.manong.community.enums.NotificationTypeEnum;
import zhuke.manong.community.exception.CustomizeErrorCode;
import zhuke.manong.community.exception.CustomizeException;
import zhuke.manong.community.mapper.NotificationMapper;
import zhuke.manong.community.model.Notification;
import zhuke.manong.community.model.NotificationExample;
import zhuke.manong.community.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/********************************************************************************
 * Purpose:实现未读通知功能的业务逻辑
 * Author: Caijinbang
 * Created Date:2022-2-19
 * Modify Description:
 * 1. 新增未读通知查询分页的业务逻辑 (Caijinbang 2022-2-19)
 * 2. 新增查询未读通知的总数量的业务逻辑 (Caijinbang 2022-2-21)
 ** *****************************************************************************/
@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    /********************************************************************************
     * Purpose: 实现未读通知查询分页
     * Param: userId 为 用户的唯一标识，page 为 用户个人主页当前的页数，size 为主页每页展示数
     * Return: PaginationDTO 为 返回的帖子集合
     ** *****************************************************************************/
    public PaginationDTO list(Long userId, Integer page, Integer size) {

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();

        Integer totalPage;
        //根据用户id查询最新回复总数（用于分页操作）
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);

        //计算总页数
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //特殊处理当前页数的异常
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        //传入总页数以及当前页数，判断是否展示首页、上一页、下一页、尾页按钮
        paginationDTO.setPagination(totalPage, page);

        //计算页数的初始值
        //size*(page-1)
        Integer offset = size * (page - 1);

        //根据用户id查询最新回复的所有数据信息
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        //查找结果根据创建时间降序排列
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        //特殊处理最新回复数为0
        if (notifications.size() == 0) {
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        //将查询到的结果复制到前端展示的封装类中
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    /********************************************************************************
     * Purpose: 查询未读通知的总数量
     * Param: userId 为 用户的唯一标识
     * Return: Long 为 返回的最想回复总数
     ** *****************************************************************************/
    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    /********************************************************************************
     * Purpose: 跳转阅读指定未读通知
     * Param: id 为 用户的唯一标识 ， user 为 用户字段信息
     * Return: NotificationDTO 为 返回的通知信息数据
     ** *****************************************************************************/
    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        //通知不存在
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        //通知者与用户不是同一人
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        //将通知设置为已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        //更新通知信息
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
