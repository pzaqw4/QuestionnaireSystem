package com.QuestionnaireProject.QuestionnaireSystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.QuestionnaireProject.QuestionnaireSystem.entity.PersonInfo;
import com.QuestionnaireProject.QuestionnaireSystem.repository.PersonInfoDao;

@SpringBootTest(classes = QuestionnaireSystemApplication.class) // 為找到@SpringBootApplication主配置類別來啟動Spring Boot應用程式環境
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonTest {

	@Autowired
	private PersonInfoDao personDao;
	
	@Test
	public void saveTest() {
		PersonInfo item = new PersonInfo();
		item.setName("02");
		item.setEmail("02@gmail.com");
		item.setPhone("1234567802");
		item.setAccount("admin");
		item.setPassword("12345");

		personDao.save(item);
	}
}
