package zhuke.manong.community.cache;

import org.apache.commons.lang3.StringUtils;
import zhuke.manong.community.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/********************************************************************************
 * Purpose: 标签缓存类
 * Author: Caijinbang
 * Created Date:2022-2-14
 * Modify Description:
 * 1. 新增获取的标签缓存方法 (Caijinbang 2022-2-14)
 * 2. 新增前端标签是否合规 (Caijinbang 2022-2-15)
 ** *****************************************************************************/
public class TagCache {

    /*******************************************************************************
     * Purpose: 获取的标签缓存
     ********************************************************************************/
    public static List<TagDTO> get() {
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("javascript", "php", "css", "html", "html5", "java", "node.js", "python", "c++", "c", "golang", "objective-c", "typescript", "shell", "swift", "c#", "sass", "ruby", "bash", "less", "asp.net", "lua", "scala", "coffeescript", "actionscript", "rust", "erlang", "perl"));
        tagDTOS.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("laravel", "spring", "express", "django", "flask", "yii", "ruby-on-rails", "tornado", "koa", "struts"));
        tagDTOS.add(framework);


        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("linux", "nginx", "docker", "apache", "ubuntu", "centos", "缓存 tomcat", "负载均衡", "unix", "hadoop", "windows-server"));
        tagDTOS.add(server);

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql", "redis", "mongodb", "sql", "oracle", "nosql memcached", "sqlserver", "postgresql", "sqlite"));
        tagDTOS.add(db);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("git", "github", "visual-studio-code", "vim", "sublime-text", "xcode intellij-idea", "eclipse", "maven", "ide", "svn", "visual-studio", "atom emacs", "textmate", "hg"));
        tagDTOS.add(tool);

        TagDTO math = new TagDTO();
        math.setCategoryName("数据结构与算法");
        math.setTags(Arrays.asList("算法", "数据结构", "链表", "贪心算法", "动态规划", "排序算法", "leetcode", "最小二乘法", "散列表", "推荐算法", "宽度优先", "广度优先", "深度优先", "迭代加深","图搜索算法","近邻算法","剪枝","哈希算法"));
        tagDTOS.add(math);

        TagDTO net = new TagDTO();
        net.setCategoryName("网络");
        net.setTags(Arrays.asList("http", "p2p", "udp", "ssl", "https", "wireshark", "websocket", "网络安全", "tcpdump", "网络协议", "tcp/ip", "rpc"));
        tagDTOS.add(net);

        return tagDTOS;
    }

    /*******************************************************************************
     * Purpose: 检验前端发布界面标签是否合规
     * Param: tags 为 标签输入框的内容
     * Return: String 返回 true or false
     ********************************************************************************/
    public static String filterInvalid(String tags) {
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();

        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> StringUtils.isBlank(t) || !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}
