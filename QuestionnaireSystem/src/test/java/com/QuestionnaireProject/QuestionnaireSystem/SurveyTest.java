package com.QuestionnaireProject.QuestionnaireSystem;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Survey;
import com.QuestionnaireProject.QuestionnaireSystem.repository.SurveyDao;
import com.QuestionnaireProject.QuestionnaireSystem.service.SurveyService;

@SpringBootTest(classes = QuestionnaireSystemApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) 
public class SurveyTest {

	@Autowired
	private SurveyDao surveyDao;
	
	@Autowired
	private SurveyService surveyService;

	@Test
	public void saveTest() {
		Survey survey = new Survey();		
		survey.setTitle("Test 4");
		survey.setBody("body 4");
		
		
		surveyDao.save(survey);
	}
	
	@Test
	public void searchTest() {		
		Page<Survey> result = surveyService.searchSurveyByTitleMatch(0, "???հݨ?");
		for (Survey survey : result) {
			System.out.println(survey.getTitle());
		}
	}
	@Test
	public void Test() {	

	}
}
