package com.QuestionnaireProject.QuestionnaireSystem.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.QuestionnaireProject.QuestionnaireSystem.entity.PersonInfo;
import com.QuestionnaireProject.QuestionnaireSystem.entity.Survey;
import com.QuestionnaireProject.QuestionnaireSystem.service.FeedbackService;
import com.QuestionnaireProject.QuestionnaireSystem.service.QuestionService;
import com.QuestionnaireProject.QuestionnaireSystem.service.SurveyService;

@Controller
public class BeIndexController {
	@Autowired
	private SurveyService surveyService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private FeedbackService feedbackService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@GetMapping(value = { "/backendindex" })
	public String backendGet(Model model,
			@RequestParam(name = "pageNum", required = false, defaultValue = "0") int pageNum // �����Ʀr
			, RedirectAttributes redirectAttrs, HttpSession session) {
		// ���P�_�O�_���n�J
		PersonInfo person = (PersonInfo) session.getAttribute("person");
		if (person == null) {
			redirectAttrs.addFlashAttribute("alertMessage", "�Х��n�J�޲z���b��!!");
			return "redirect:/index";
		}
		// ���o�����n��ܪ��ݨ�
		Page<Survey> surveys = surveyService.getSurveyPageList(pageNum);
		// �N�ݨ���Ƹ˶i�e�x
		model.addAttribute("surveys", surveys);
		// �M����x����session
		session.removeAttribute("survey");
		session.removeAttribute("modifySurvey");
		session.removeAttribute("questions");

		return "backendindex";
	}

	@GetMapping(value = { "/backendindex" }, params = "delete")
	public String deleteByPostId(Model model, RedirectAttributes redirectAttrs,
			@RequestParam(name = "postId", required = false) UUID[] postid) {
		// �Y���������@������
		if (postid == null) {
			redirectAttrs.addFlashAttribute("alertMessage", "�R������!");
			return "redirect:/backendindex";
		}
		// ���O�R���ݨ��A�H�ΦP�@�g���������D�H�Φ^��
		try {
			surveyService.deleteSurvey(postid);
			questionService.deleteQuestions(postid);
			feedbackService.deleteFeedbacks(postid);
		} catch (Exception e) {
			logger.error(e.getMessage());
			redirectAttrs.addFlashAttribute("alertMessage", "�R������!");
			return "redirect:/backendindex";
		}

		redirectAttrs.addFlashAttribute("alertMessage", "�R�����\!!");
		return "redirect:/backendindex";
	}

	@GetMapping(value = { "/backendindex" }, params = "search")
	public String backendSearchList(Model model, RedirectAttributes redirectAttrs,
			@RequestParam(name = "pageNum", required = false, defaultValue = "0") int pageNum,
			@RequestParam(name = "title", required = false, defaultValue = "") String title,
			@RequestParam(name = "startTime", required = false, defaultValue = "") String startTime,
			@RequestParam(name = "endTime", required = false, defaultValue = "") String endTime) throws ParseException {
		// �ˬd�ݨ��O�_�L��
		surveyService.timeCheck();

		// �ˬd��J�O�_����
		if (title.isEmpty() && startTime.isEmpty() && endTime.isEmpty()) {
			redirectAttrs.addFlashAttribute("alertMessage", "�п�J�d������r�άO�ݨ��ɶ��϶� !!");
			return "redirect:/backendindex";
		}
		// �ˬd�ɶ��榡
		if (!surveyService.timeFormatCheck(startTime).isEmpty() || !surveyService.timeFormatCheck(endTime).isEmpty()) {
			redirectAttrs.addFlashAttribute("alertMessage", "�п�J���T�ɶ��榡 !!");
			return "redirect:/backendindex";
		}

		Page<Survey> surveys = null;
		// �u����J���D
		if (startTime.isEmpty() && endTime.isEmpty()) {
			// �ŦX����r���ݨ�
			surveys = surveyService.searchSurveyByTitleMatch(pageNum, title);
			// �T�ӳ���
		} else if (!title.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty()) {
			redirectAttrs.addFlashAttribute("alertMessage", "����r�P�ɶ��Ф��}�j�M !!");
			return "redirect:/backendindex";
			// �u����}�l�H�ε���
		} else if (!startTime.isEmpty() && !endTime.isEmpty()) {
			// �૬��Date
			Date start = surveyService.timeParse(startTime);
			// �૬��Date
			Date end = surveyService.timeParse(endTime);

			// �N��Ʈw���o�������ɶ�+�@��
			Date result = surveyService.endTimePlus1(end);
			// ���o�ŦX���ݨ�
			surveys = surveyService.searchSurveyByAllTime(pageNum, start, result);
		} else {
			redirectAttrs.addFlashAttribute("alertMessage", "�п�J�d������r�άO�ݨ��ɶ��϶� !!");
			return "redirect:/backendindex";
		}

		model.addAttribute("searchSurveys", surveys);

		return "backendindex";
	}
}