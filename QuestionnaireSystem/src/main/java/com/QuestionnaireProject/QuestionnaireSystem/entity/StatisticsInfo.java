package com.QuestionnaireProject.QuestionnaireSystem.entity;

import java.util.Set;

public class StatisticsInfo {
	
	private String title;
	
	private Set<SelectionInfo> selectionInfo;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<SelectionInfo> getSelectionInfo() {
		return selectionInfo;
	}

	public void setSelectionInfo(Set<SelectionInfo> selectionInfo) {
		this.selectionInfo = selectionInfo;
	}	
}
