package org.hdcd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.hdcd.domain.Reply;

@Mapper
public interface ReplyMapper{
 
	public List<Reply> list(int board_no) throws Exception;
    
  	public int count(int board_no) throws Exception;
   
    public void create(Reply reply) throws Exception;
}
