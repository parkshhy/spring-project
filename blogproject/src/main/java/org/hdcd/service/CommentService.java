package org.hdcd.service;

import java.util.List;

import org.hdcd.domain.Comment;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
 
	public List<Comment> commentListService(int board_no) throws Exception;
    
    public int commentInsertService(Comment comment) throws Exception;
    
    public int commentUpdateService(Comment comment) throws Exception;
    
    public int commentDeleteService(int cno) throws Exception;
}

