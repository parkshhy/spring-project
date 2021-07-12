package org.hdcd.service;

import java.util.List;

import org.hdcd.common.domain.CodeLabelValue;

public interface CodeService {

	public List<CodeLabelValue> getCodeClassList() throws Exception;

	public List<CodeLabelValue> getCodeList(String classCode) throws Exception;

}
