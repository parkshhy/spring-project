package org.hdcd.service;

import java.util.List;

import org.hdcd.common.domain.CodeLabelValue;
import org.hdcd.mapper.CodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeServiceImpl implements CodeService {

	@Autowired
	private CodeMapper mapper;

	@Override
	public List<CodeLabelValue> getCodeClassList() throws Exception {
		return mapper.getCodeClassList();
	}

	@Override
	public List<CodeLabelValue> getCodeList(String classCode) throws Exception {
		return mapper.getCodeList(classCode);
	}

}
