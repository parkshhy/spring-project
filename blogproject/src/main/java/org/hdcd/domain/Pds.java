package org.hdcd.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class Pds implements Serializable{

	private static final long serialVersionUID = -5044142253436690326L;

	private int itemId;
	
	private String itemName;
	
	private String description;
	
	private String[] files;
	
	private int viewCnt;
	

	
}
