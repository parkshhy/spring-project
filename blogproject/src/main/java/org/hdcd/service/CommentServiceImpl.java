package org.hdcd.service;

import java.util.List;

import org.hdcd.domain.Comment;
import org.hdcd.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
    CommentMapper mapper;
    
    public List<Comment> commentListService(int board_no) throws Exception{
        
        return mapper.commentList(board_no);
    }
    
    public int commentInsertService(Comment comment) throws Exception{
        
        return mapper.commentInsert(comment);
    }
    
    public int commentUpdateService(Comment comment) throws Exception{
        
        return mapper.commentUpdate(comment);
    }
    
    public int commentDeleteService(int cno) throws Exception{
        
        return mapper.commentDelete(cno);
    }
}
