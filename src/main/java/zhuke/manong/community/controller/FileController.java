package zhuke.manong.community.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zhuke.manong.community.dto.FileDTO;

/********************************************************************************
 * Purpose:图片上传
 * Author: Caijinbang
 * Created Date:2022-2-23
 * Modify Description:
 * 1. 实现图片上传 (Caijinbang 2022-2-23)
 ** *****************************************************************************/
@Controller
@Slf4j
public class FileController {

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(){
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/default-picture.png");
        return fileDTO;
    }
}
