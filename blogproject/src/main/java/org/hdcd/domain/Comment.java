package org.hdcd.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class Comment implements Serializable {

	private static final long serialVersionUID = 8090144428726787011L;
	
	private int cno;
    private int board_no;
    private String content;
    private String writer;
    private Data reg_date;

}
