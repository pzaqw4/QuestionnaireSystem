package com.QuestionnaireProject.QuestionnaireSystem.entity;

import java.util.Map;

public class StatisticsInfoVol2 {

	private String title;
	
	private Map<String,Integer> selectionMap;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<String, Integer> getSelectionMap() {
		return selectionMap;
	}

	public void setSelectionMap(Map<String, Integer> selectionMap) {
		this.selectionMap = selectionMap;
	}
}
