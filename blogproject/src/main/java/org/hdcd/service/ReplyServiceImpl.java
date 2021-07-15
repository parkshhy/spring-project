package org.hdcd.service;

import java.util.List;

import org.hdcd.domain.Reply;
import org.hdcd.mapper.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//서비스가 DB의 트랜잭션 단위가 된다. 
@Service //service bean
public class ReplyServiceImpl implements ReplyService {
    
    
    @Autowired
    private ReplyMapper mapper;    //mapper 호출하기위해서 의존성을 주입
    							   //mapper 를 자동 주입 받는다.
 
    
    //댓글 목록
    @Override
    public List<Reply> list(int board_no) throws Exception {
        return mapper.list(board_no);
    }
    
    //댓글 수
    @Override
    public int count(int board_no) throws Exception {
        return 0;
    }
    
    
    //댓글 쓰기    
    @Override
    public void create(Reply dto) throws Exception {
    	mapper.create(dto);
    }
    
}

