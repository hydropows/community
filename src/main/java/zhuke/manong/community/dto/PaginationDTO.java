package zhuke.manong.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
/********************************************************************************
 * Purpose:封装论坛社区帖子 问题 的字段以及页码的字段
 * Author: Caijinbang
 * Created Date:2021-10-20
 * Modify Description:
 * 1. 添加封装论坛社区主页 帖子问题 的字段 以及页码的字段(Caijinbang 2021-10-20)
 ** *****************************************************************************/
@Data
public class PaginationDTO<T> {
    //存储论坛社区主页问题的集合
    private List<T> data;
    //判断是否有上一页按钮
    private boolean showPrevious;
    //判断是否有首页按钮
    private boolean showFirstPage;
    //判断是否有下一页按钮
    private boolean showNext;
    //判断是否有尾页按钮
    private boolean showEndPage;
    //当前页
    private Integer page;
    //当前页相邻的页数
    private List<Integer> pages = new ArrayList<>();
    //论坛帖子总页数
    private Integer totalPage;

    /*******************************************************************************
     * Purpose: 判断是否展示首页、上一页、下一页、尾页按钮，为了服务器弱化js
     * Param: page 为 index 主页当前的页数，totalPage 为主页总页数
     ********************************************************************************/
    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage = totalPage;
        this.page = page;

        //添加当前页码
        pages.add(page);
        //添加当前页码相邻3位的数字
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }

            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        // 是否展示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        // 是否展示下一页
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }

        // 是否展示第一页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        // 是否展示最后一页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
