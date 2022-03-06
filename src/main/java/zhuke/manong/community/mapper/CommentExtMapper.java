package zhuke.manong.community.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import zhuke.manong.community.model.Comment;
@Repository
@Mapper
public interface CommentExtMapper {
    //增加评论数
    int incCommentCount(Comment comment);
}
