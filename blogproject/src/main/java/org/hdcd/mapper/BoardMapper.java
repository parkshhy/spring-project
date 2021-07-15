package org.hdcd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.hdcd.common.domain.PageRequest;
import org.hdcd.domain.Board;

@Mapper
public interface BoardMapper {

	public void create(Board board) throws Exception;

	public Board read(Integer boardNo) throws Exception;

	public void update(Board board) throws Exception;

	public void delete(Integer boardNo) throws Exception;

	//페이징요청 정보를 매개변수로 받아 페이징 처리를 한 게시글 목록을 반환한다.
	public List<Board> list(PageRequest pageRequest) throws Exception;
	
	//검색처리된 게시글 건수를 반환
	public int count(PageRequest pageRequest) throws Exception;

}
