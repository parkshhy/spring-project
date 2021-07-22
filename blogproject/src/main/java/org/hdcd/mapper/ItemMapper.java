package org.hdcd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.hdcd.domain.Item;

@Mapper
public interface ItemMapper {

	public void create(Item item) throws Exception;
	
	public List<Item> list() throws Exception;

	public Item read(int itemId) throws Exception;
	
	public void update(Item item) throws Exception;

	public void delete(int itemId) throws Exception;

	public String getPreview(int itemId) throws Exception;
}
