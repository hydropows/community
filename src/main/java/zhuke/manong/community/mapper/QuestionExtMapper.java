package zhuke.manong.community.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import zhuke.manong.community.dto.QuestionQueryDTO;
import zhuke.manong.community.model.Question;

import java.util.List;
@Repository
@Mapper
public interface QuestionExtMapper {
    //累加阅读数
    int incView(Question record);
    //累加评论数
    int incCommentCount(Question record);
    //查找相关帖子
    List<Question> selectRelated(Question question);
    //计算查找结果的帖子总数量
    Integer countBySearch(QuestionQueryDTO questionQueryDTO);
    //查找结果的所有帖子
    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);

//    List<Question> selectSticky();
}
