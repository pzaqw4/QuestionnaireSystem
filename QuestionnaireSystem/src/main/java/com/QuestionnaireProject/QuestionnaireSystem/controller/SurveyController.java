package com.QuestionnaireProject.QuestionnaireSystem.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Feedback;
import com.QuestionnaireProject.QuestionnaireSystem.entity.Question;
import com.QuestionnaireProject.QuestionnaireSystem.entity.Survey;
import com.QuestionnaireProject.QuestionnaireSystem.repository.QuestionDao;
import com.QuestionnaireProject.QuestionnaireSystem.repository.SurveyDao;
import com.QuestionnaireProject.QuestionnaireSystem.service.QuestionService;

@Controller
public class SurveyController {

	@Autowired
	private SurveyDao surveyDao;

	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private QuestionService questionService;

	@GetMapping(value = { "/survey" })
	public String survey(Model model, HttpSession session
			,RedirectAttributes redirectAttrs
			// ���oURL����postId
			, @RequestParam(name = "postId", required = false) UUID postId) {
		if(postId == null) {
			redirectAttrs.addFlashAttribute("alertMessage", "�п�ܰݨ�!!");
			return "redirect:/index";
		}
		// �z�LpostId��ݨ�
		Survey survey = surveyDao.findById(postId).get();
		// �z�LpostId��ݨ����Ҧ����D
		List<Question> list = questionDao.findByPostId(postId);
		// �ɤJ�e��
		model.addAttribute("survey", survey);
		model.addAttribute("questionInfo", list);
		// ���F�ǵ�post��k��
		session.setAttribute("surveyInfo", survey); 
		return "survey";
	}

	@PostMapping("/survey")
	public String feedbackPost(Model model, HttpSession session, @ModelAttribute Feedback feedback,
			@RequestParam("username") String name, @RequestParam("userphone") String phone,
			@RequestParam("useremail") String email, @RequestParam("userage") int age) {
		// ���opostId�A���Fquery string��
		Survey survey = (Survey) session.getAttribute("surveyInfo");
		UUID postId = survey.getPostId();
		// ��g��Ƽg�Jsession��
		feedback.setUserName(name);
		feedback.setUserPhone(phone);
		feedback.setUserEmail(email);
		feedback.setUserAge(age);
		feedback.setPostId(postId);
		session.setAttribute("feedback", feedback);

		// query string�a�JpostId
		return "redirect:/surveyconfirm?postId=" + postId;
	}

	@ResponseBody
	@GetMapping(value = { "/loadQuestions/{postId}" })
	public String loadQuestions(Model model, @PathVariable("postId") UUID postId) {
		return questionService.questionToJson(postId);
	}
}
