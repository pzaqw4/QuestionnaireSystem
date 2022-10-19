package com.QuestionnaireProject.QuestionnaireSystem.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Answer;
import com.QuestionnaireProject.QuestionnaireSystem.entity.Question;
import com.QuestionnaireProject.QuestionnaireSystem.entity.StatisticsInfo;
import com.QuestionnaireProject.QuestionnaireSystem.entity.Survey;
import com.QuestionnaireProject.QuestionnaireSystem.repository.QuestionDao;
import com.QuestionnaireProject.QuestionnaireSystem.repository.SurveyDao;
import com.QuestionnaireProject.QuestionnaireSystem.service.FeedbackService;
import com.google.gson.Gson;


@Controller
public class StatisticsController {
	
	@Autowired
	private SurveyDao surveyDao; 
	
	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private FeedbackService feedbackService;

	@GetMapping(value = { "/statistics"})
	public String statistics(Model model
			,@RequestParam(name="postId",required = false)UUID postId) {
		Survey survey = surveyDao.findById(postId).get();
		model.addAttribute("survey", survey);
		return "statistics";
	}
	
	@ResponseBody
	@GetMapping(value = { "/loadstatistics/{postId}"})
	public String loadStatistics(Model model
			,@PathVariable("postId")UUID postId) {
		Gson gson = new Gson();
				
		List<Question> questionlist = questionDao.findByPostId(postId);  //從postId找到所有的問題
		List<Answer> answerList = feedbackService.deserializeAnswer(postId);  //反序列化完問卷回答的答案清單
		List<StatisticsInfo> statisticsInfoList = feedbackService.getStatistics(questionlist, answerList);
		return gson.toJson(statisticsInfoList); 
	}
}
