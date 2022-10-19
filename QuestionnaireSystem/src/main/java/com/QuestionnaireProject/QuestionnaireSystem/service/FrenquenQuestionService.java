package com.QuestionnaireProject.QuestionnaireSystem.service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuestionnaireProject.QuestionnaireSystem.repository.FrenquenQuestionDao;

@Service
public class FrenquenQuestionService {
	
	@Autowired
	private FrenquenQuestionDao frenquenQuestionDao; 

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public void deleteFrenquenQuestion(int[] id) {		
		try {
			for (int i : id) {
				frenquenQuestionDao.deleteById(i);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}		
	}
	
	public String paramCheck(String caption,String selection) {
		if(caption.isEmpty()) {
			return "請輸入標題!!";
		}else if(selection.isEmpty()) {
			return "請輸入選項!!";
		}else
			return "";		
	}	
}
