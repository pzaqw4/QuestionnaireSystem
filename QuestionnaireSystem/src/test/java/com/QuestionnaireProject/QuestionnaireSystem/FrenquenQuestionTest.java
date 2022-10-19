package com.QuestionnaireProject.QuestionnaireSystem;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.QuestionnaireProject.QuestionnaireSystem.entity.FrenquenQuestion;
import com.QuestionnaireProject.QuestionnaireSystem.repository.FrenquenQuestionDao;

@SpringBootTest(classes = QuestionnaireSystemApplication.class) // �����@SpringBootApplication�D�t�m���O�ӱҰ�Spring Boot���ε{������
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // ���F�i�H�ϥ�@BeforeAll �M @AfterAll
public class FrenquenQuestionTest {

	@Autowired
	private FrenquenQuestionDao fqDao;
	
	@Test
	public void saveTest() {
		FrenquenQuestion item = new FrenquenQuestion();

		item.setCaption("Test");
		item.setSelection("123;�A�n;����");
		item.setNullable(0);
		item.setType(0);
				
		fqDao.save(item);		
	}
}
