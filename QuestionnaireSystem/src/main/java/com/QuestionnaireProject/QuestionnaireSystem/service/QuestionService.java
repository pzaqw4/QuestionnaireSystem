package com.QuestionnaireProject.QuestionnaireSystem.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Question;
import com.QuestionnaireProject.QuestionnaireSystem.repository.QuestionDao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class QuestionService {
	@Autowired
	private QuestionDao questionDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public void deleteQuestions(UUID[] postid) {

		try {
			for (UUID uuid : postid) {
				List<Question> list = questionDao.findByPostId(uuid);
				for (Question question : list) {
					questionDao.delete(question);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public boolean checkQuestionListRequired(List<Question> list) {
		boolean result = false;
		for (Question question : list) {
			if (question.getNullable().equals("on")) {
				result = true;
				return result;
			}
		}
		return result;
	}

	public String questionToJson(UUID postId) {
		Gson gson = new Gson();
		// �T�OGson�ন�۩w�q���O
		Type getType = new TypeToken<List<Question>>() {
		}.getType();
		// ���o�ݨ������Ҧ����D
		List<Question> questionList = questionDao.findByPostId(postId);
		// �Y���D�s�b���ܡA�NList�ᤩ���T���O��ǦC��
		// �Ѧ�:https://codertw.com/%E7%A8%8B%E5%BC%8F%E8%AA%9E%E8%A8%80/316690/
		// �ܨҡG���X�]Collection�^���d��
		if (!questionList.isEmpty()) {
			return gson.toJson(questionList, getType);
		}
		return null;
	}
}
