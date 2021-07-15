package org.hdcd.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Member implements Serializable{

	private static final long serialVersionUID = 5965448709809155461L;

	private int userNo;
	
	private String userId;
	
	private String userPw;
	
	private String userName;
	
	private String job;
	
	private int coin;
	
	private boolean enabled;
	
	private Date regDate;
	
	private Date updDate;

	private List<MemberAuth> authList;


}
