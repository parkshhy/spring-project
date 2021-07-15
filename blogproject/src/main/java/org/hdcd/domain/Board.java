package org.hdcd.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Board implements Serializable {

	private static final long serialVersionUID = -5223351127139237936L;
	
	
	private int boardNo;
	private String title;
	private String content;
	private String writer;
	private Date regDate;
	
	@Override
	public String toString() {
		return "Board [boardNo=" + boardNo + ", title=" + title + ", content=" + content + ", writer=" + writer + ", regdate=" + regDate + "]";
	}

}
