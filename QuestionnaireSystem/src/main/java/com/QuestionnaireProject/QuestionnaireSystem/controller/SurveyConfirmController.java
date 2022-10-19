package com.QuestionnaireProject.QuestionnaireSystem.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Answer;
import com.QuestionnaireProject.QuestionnaireSystem.entity.Feedback;
import com.QuestionnaireProject.QuestionnaireSystem.entity.Question;
import com.QuestionnaireProject.QuestionnaireSystem.entity.Survey;
import com.QuestionnaireProject.QuestionnaireSystem.repository.FeedbackDao;
import com.QuestionnaireProject.QuestionnaireSystem.repository.QuestionDao;
import com.QuestionnaireProject.QuestionnaireSystem.repository.SurveyDao;
import com.QuestionnaireProject.QuestionnaireSystem.service.QuestionService;
import com.google.gson.Gson;

@Controller
public class SurveyConfirmController {
	@Autowired
	private SurveyDao surveyDao;

	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private FeedbackDao feedbackDao;

	@Autowired
	private QuestionService questionService;

	@GetMapping(value = { "/surveyconfirm" })
	public String surveyConfirm(Model model, @RequestParam(name = "postId", required = false) UUID postId // 透過URL取得postId
			, HttpSession session, HttpServletRequest request) {
		// 透過postId找到對應的問卷
		Survey survey = surveyDao.findById(postId).get(); 
		// 找所有postId的問題
		List<Question> list = questionDao.findByPostId(postId); 
		
		// 前端印出問卷資料
		model.addAttribute("survey", survey); 
		// 前端印出所有問題資料
		model.addAttribute("questionList", list); 
		return "surveyconfirm";
	}

	@ResponseBody
	@GetMapping(value = { "/getQuestions/{postId}" })
	public String getQuestions(Model model, @PathVariable("postId") UUID postId) {
		return questionService.questionToJson(postId);
	}

	@PostMapping(value = { "/surveyconfirm" })
	public String saveFeedback(Model model, HttpSession session, RedirectAttributes redirectAttrs,
			HttpServletRequest request) throws UnsupportedEncodingException {
		Gson gson = new Gson();
		Feedback sessionFeedback = (Feedback) session.getAttribute("feedback"); // 取得回答問卷資料
		UUID postId = sessionFeedback.getPostId();
		List<Question> questionlist = questionDao.findByPostId(postId);
		List<Question> list = new ArrayList<>();
		for (Question question : questionlist) {
			if (question.getNullable().equals("on")) {
				list.add(question);
			}
		}
		Cookie[] cookies = request.getCookies(); // 取得所有cookie
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("ansVal")) { // 如果有符合名字的cookie
					String answer = URLDecoder.decode(cookie.getValue(), "UTF-8"); // 解碼
					List<Answer> targetList = new ArrayList<Answer>();
					Answer[] ans;
					ans = gson.fromJson(answer, Answer[].class);
					for (Answer a : ans) {
						targetList.add(a);
					}
					Integer targetId = null;
					for (Question ques : list) {
						targetId = ques.getQuId();
						boolean correct = false;
						for (Answer item : targetList) {
							if (list.size() > targetList.size()) {
								break;
							}
							if ((targetId + "").equals(item.getKey())) {
								correct = true;
								break;
							}
						}
						if (!correct) {
							redirectAttrs.addFlashAttribute("alertMessage", "請檢查必填後再送出");
							return "redirect:/survey?postId=" + postId;
						}
					}
					sessionFeedback.setAnswer(answer); // 加入答案
					feedbackDao.save(sessionFeedback); // 儲存
				}
			}
		}
		redirectAttrs.addFlashAttribute("alertMessage", "作答成功!! 感謝您的填寫");
		return "redirect:/index";
	}
}
