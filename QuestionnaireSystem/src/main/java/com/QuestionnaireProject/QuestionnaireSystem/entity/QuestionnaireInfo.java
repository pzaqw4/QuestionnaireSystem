package com.QuestionnaireProject.QuestionnaireSystem.entity;

import java.util.List;

public class QuestionnaireInfo {
	private List<Question> questions;
	
	private List<Answer> answers;

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}	
}
