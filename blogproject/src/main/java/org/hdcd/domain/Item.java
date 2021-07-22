package org.hdcd.domain;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Item implements Serializable{
	
	private static final long serialVersionUID = -7681732344060866231L;

	private int itemId;
	
	private String itemName;
	
	private int price;
	
	private String description;
	
	private MultipartFile picture;
	
	private String pictureUrl;
	
	private MultipartFile preview;
	
	private String previewUrl;
	
	
	
}
