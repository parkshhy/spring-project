package org.hdcd.mapper;

import java.util.List;

import org.hdcd.common.domain.PageRequest;
import org.hdcd.domain.Board;

public interface BoardMapper {

	public void create(Board board) throws Exception;

	public Board read(Integer boardNo) throws Exception;

	public void update(Board board) throws Exception;

	public void delete(Integer boardNo) throws Exception;

	public List<Board> list(PageRequest pageRequest) throws Exception;
	
	//검색처리된 게시글 건수를 반환
	public int count(PageRequest pageRequest) throws Exception;

}
