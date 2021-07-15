package org.hdcd.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class MemberAuth implements Serializable{

	private static final long serialVersionUID = -9200898589691826811L;
	
	private int userNo;
	
	private String auth;


}
