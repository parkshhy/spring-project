package org.hdcd.service;

import java.util.List;

import org.hdcd.domain.Reply;

public interface ReplyService {
	 
	//헤더만 정의
    public List<Reply> list(int board_no) throws Exception; //댓글 리스트
    
    public int count(int board_no) throws Exception;        //댓글 수
    
    public void create(Reply dto) throws Exception;    		//댓글 작성
    
}

