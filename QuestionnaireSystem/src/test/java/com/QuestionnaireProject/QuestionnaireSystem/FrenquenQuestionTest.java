package com.QuestionnaireProject.QuestionnaireSystem;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.QuestionnaireProject.QuestionnaireSystem.entity.FrenquenQuestion;
import com.QuestionnaireProject.QuestionnaireSystem.repository.FrenquenQuestionDao;

@SpringBootTest(classes = QuestionnaireSystemApplication.class) // 為找到@SpringBootApplication主配置類別來啟動Spring Boot應用程式環境
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 為了可以使用@BeforeAll 和 @AfterAll
public class FrenquenQuestionTest {

	@Autowired
	private FrenquenQuestionDao fqDao;
	
	@Test
	public void saveTest() {
		FrenquenQuestion item = new FrenquenQuestion();

		item.setCaption("Test");
		item.setSelection("123;你好;測試");
		item.setNullable(0);
		item.setType(0);
				
		fqDao.save(item);		
	}
}
