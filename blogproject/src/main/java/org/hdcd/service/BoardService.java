package org.hdcd.service;

import java.util.List;

import org.hdcd.common.domain.PageRequest;
import org.hdcd.domain.Board;

public interface BoardService {

	public void register(Board board) throws Exception;

	public Board read(Integer boardNo) throws Exception;

	public void modify(Board board) throws Exception;

	public void remove(Integer boardNo) throws Exception;

	//페이지요청 정보를 매개변수로 받아 페이징 처리를 한 게시글 목록을 반환한다.
	public List<Board> list(PageRequest pageRequest) throws Exception;
	
	//게시글 반환,검색처리된 게시글 건수를 반환한다.
	public int count(PageRequest pageRequest) throws Exception;

	
	
	
}
