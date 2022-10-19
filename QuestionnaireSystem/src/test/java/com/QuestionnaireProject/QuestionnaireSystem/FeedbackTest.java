package com.QuestionnaireProject.QuestionnaireSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Answer;
import com.QuestionnaireProject.QuestionnaireSystem.entity.Feedback;
import com.QuestionnaireProject.QuestionnaireSystem.repository.FeedbackDao;
import com.google.gson.Gson;

@SpringBootTest(classes = QuestionnaireSystemApplication.class) // 為找到@SpringBootApplication主配置類別來啟動Spring Boot應用程式環境
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 為了可以使用@BeforeAll 和 @AfterAll
public class FeedbackTest {

	@Autowired
	private FeedbackDao fbDao;
	
	@Test
	public void saveTest() {
		Feedback item = new Feedback();

		item.setUserName("YOASOBI 3");
		item.setUserEmail("JP3@gmail.com");
		item.setUserPhone("1234567890");
		item.setUserAge(30);
		item.setAnswer("8/8");
		item.setPostId(UUID.fromString("78e87915-21be-4b80-80e6-20a16013091e"));
		
		fbDao.save(item);		
	}
	
	@Test
	public void deserializeTest() {
		Gson gson = new Gson();
		List<Answer> targetObject = new ArrayList<Answer>();
		Answer[] ans;
		List<Feedback> feedbackList = fbDao.findByPostId(UUID.fromString("cd1a4b2b-d202-43d8-b193-107ea84a4f9e"));
		for (Feedback feedback : feedbackList) {
			ans = gson.fromJson(feedback.getAnswer(), Answer[].class);
			for (Answer a : ans) {
				targetObject.add(a);
			}
		}
			
		for (Answer answer : targetObject) {
			System.out.println(answer.getKey());
			System.out.println(answer.getValue());
			System.out.println("-------------");
		}
	}
}
