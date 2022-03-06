package zhuke.manong.community.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhuke.manong.community.dto.PaginationDTO;
import zhuke.manong.community.dto.QuestionDTO;
import zhuke.manong.community.dto.QuestionQueryDTO;
import zhuke.manong.community.exception.CustomizeErrorCode;
import zhuke.manong.community.exception.CustomizeException;
import zhuke.manong.community.mapper.QuestionExtMapper;
import zhuke.manong.community.mapper.QuestionMapper;
import zhuke.manong.community.mapper.UserMapper;
import zhuke.manong.community.model.Question;
import zhuke.manong.community.model.QuestionExample;
import zhuke.manong.community.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/********************************************************************************
 * Author: Caijinbang
 * Created Date:2021-10-19
 * Modify Description:
 * 1. 新增论坛主页 分页的业务逻辑 (Caijinbang 2021-10-19)
 * 2. 新增用户个人查看自己的提问 分页的业务逻辑 (Caijinbang 2021-10-23)
 * 3. 新增通过论坛帖子 Id 查找对应所有信息的业务逻辑 (Caijinbang 2021-10-25)
 * 4. 修改、更新所有有关 SQL 语句的操作 (Caijinbang 2021-10-29)
 * 5. 新增判断论坛帖子是否已经创建过了的业务逻辑 (Caijinbang 2021-10-30)
 * 6. createOrUpdate 方法 新增 初始化阅读数、点赞数、评论数 (Caijinbang 2021-12-18)
 * 7. 新增实现帖子页面相关帖子展示的业务逻辑 (Caijinbang 2022-2-10)
 * 8. 添加实现搜索功能的业务逻辑 (Caijinbang 2022-2-24)
 ** *****************************************************************************/
@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    /*******************************************************************************
     * Purpose: 论坛主页 分页的业务逻辑
     * Param: search 为搜索内容 , page 为 index 主页当前的页数，size 为主页每页展示数
     * Return: PaginationDTO 返回 index 论坛主页帖子所有内容
     ********************************************************************************/
    public PaginationDTO list( String search, String tag,Integer page, Integer size) {

        //将搜索内容的空格剔除
        if(StringUtils.isNotBlank(search)){
            String[] tags = StringUtils.split(search," ");
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }

        PaginationDTO paginationDTO = new PaginationDTO();
        //论坛帖子总数
        Integer totalPage;
        //获取论坛帖子总数
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);

        //判断标签是否为空
        if (StringUtils.isNotBlank(tag)) {
            tag = tag.replace("+", "").replace("*", "").replace("?", "");
            questionQueryDTO.setTag(tag);
        }

        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);

        //计算论坛的总页数
        if (totalCount % size == 0){
            totalPage=totalCount/size;
        }else{
            totalPage=totalCount/size+1;
        }

        //特殊处理当前页数的异常
        if (page<1){
            page=1;
        }
        if (page>totalPage){
            page=totalPage;
        }

        //传入总页数以及当前页数，判断是否展示首页、上一页、下一页、尾页按钮
        paginationDTO.setPagination(totalPage,page);

        //计算页数的初始值
//        Integer offset = size * (page-1);
        Integer offset = page < 1 ? 0 : size * (page - 1);

        //查询找到所有的问题帖子
        QuestionExample questionExample = new QuestionExample();
        //根据创建时间进行降序排序
        questionExample.setOrderByClause("gmt_create desc");
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        //存储含有提问者头像地址的帖子问题字段的集合
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //遍历所有的帖子问题
        for (Question question:questions){
            //通过帖子问题字段里的提问者Id查找到对应的提问者所有信息
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            //新建 论坛社区主页帖子问题对象
            QuestionDTO questionDTO = new QuestionDTO();
            //复制原来帖子问题集合的所有信息
            BeanUtils.copyProperties(question,questionDTO);
            //在论坛社区主页帖子问题对象中添加对应用户的信息
            questionDTO.setUser(user);
            //存储到 含有提问者头像地址的帖子问题字段的集合
            questionDTOList.add(questionDTO);
        }
        //赋值帖子集合
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    /*******************************************************************************
     * Purpose: 论坛用户中我的提问 分页的业务逻辑
     * Param: userId 为用户的id， page 为 index 主页当前的页数，size 为主页每页展示数
     * Return: PaginationDTO 返回 页帖子所有内容
     ********************************************************************************/
    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalPage;
        //查找指定用户的帖子问题总数
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);

        //计算指定用户帖子问题的总页数
        if (totalCount % size == 0){
            totalPage=totalCount/size;
        }else{
            totalPage=totalCount/size+1;
        }

        //处理当前页数异常
        if (page<1){
            page=1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        //传入总页数以及当前页数，判断是否展示首页、上一页、下一页、尾页按钮
        paginationDTO.setPagination(totalPage,page);
        //计算页数的初始值
        Integer offset = size * (page-1);

        //分页操作
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question:questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    /*******************************************************************************
     * Purpose: 通过论坛帖子 Id 查找对应所有信息
     * Param: id 为帖子的 唯一标识
     * Return: QuestionDTO 为坛社区主页 帖子问题 的字段
     ******************************************************************************/
    public QuestionDTO getById(Long id) {
        //通过帖子 id 查询对应帖子详细信息
        Question question = questionMapper.selectByPrimaryKey(id);
        //查找帖子异常处理
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        //通过用户 id 查找用户详情信息
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    /*******************************************************************************
     * Purpose: 判断论坛帖子是否已经创建过了
     * Param: question 为帖子信息
     ********************************************************************************/
    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //创建，初始化字段信息
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(System.currentTimeMillis());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            //插入操作
            questionMapper.insert(question);
        }else{
            //更新
            Question updateQuestion = new Question();
            //更新帖子修改时间
            updateQuestion.setGmtModified(System.currentTimeMillis());
            //更新帖子标题
            updateQuestion.setTitle(question.getTitle());
            //更新帖子内容描述
            updateQuestion.setDescription(question.getDescription());
            //更新帖子标签
            updateQuestion.setTag(question.getTag());
            //更新操作
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            //更新异常处理
            if(updated != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    /*******************************************************************************
     * Purpose: 实现点击阅读帖子，累加阅读数
     * Param: id 为帖子的 唯一标识
     ********************************************************************************/
    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        //累加阅读数
        questionExtMapper.incView(question);
    }


    /*******************************************************************************
     * Purpose: 实现帖子页面相关帖子展示的业务逻辑
     * Param: queryDTO 为帖子的详细信息字段
     * Return: List<QuestionDTO> 返回 相关帖子集合的信息字段
     ********************************************************************************/
    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        //帖子标签为空
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }

        //将多个标签分隔开
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        //将多个标签拼接成正则表达式
        String regexpTag = Arrays
                .stream(tags)
                .filter(StringUtils::isNotBlank)
                .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);
        //通过正则表达式匹配所有相关帖子
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
