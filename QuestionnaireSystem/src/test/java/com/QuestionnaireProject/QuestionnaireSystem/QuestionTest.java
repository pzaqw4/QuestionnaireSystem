package com.QuestionnaireProject.QuestionnaireSystem;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Question;
import com.QuestionnaireProject.QuestionnaireSystem.repository.QuestionDao;

@SpringBootTest(classes = QuestionnaireSystemApplication.class) 
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestionTest {


	@Autowired
	private QuestionDao questionDao;
	
	@Test
	public void saveTest() {
		
		Question item = new Question();	
		
		item.setQuId(1);
		item.setCaption("Survey");
		item.setSelection("123;456;789");
		item.setNullable("off");
		item.setType(1);
		item.setPostId(UUID.fromString("0af5f988-045c-4c13-9263-c988c18daf29"));
		
		questionDao.save(item);		
	}
}
