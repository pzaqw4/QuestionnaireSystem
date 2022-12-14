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
			@RequestParam(name = "pageNum", required = false, defaultValue = "0") int pageNum // 分頁數字
			, RedirectAttributes redirectAttrs, HttpSession session) {
		// 先判斷是否有登入
		PersonInfo person = (PersonInfo) session.getAttribute("person");
		if (person == null) {
			redirectAttrs.addFlashAttribute("alertMessage", "請先登入管理員帳號!!");
			return "redirect:/index";
		}
		// 取得分頁要顯示的問卷
		Page<Survey> surveys = surveyService.getSurveyPageList(pageNum);
		// 將問卷資料裝進前台
		model.addAttribute("surveys", surveys);
		// 清除後台相關session
		session.removeAttribute("survey");
		session.removeAttribute("modifySurvey");
		session.removeAttribute("questions");

		return "backendindex";
	}

	@GetMapping(value = { "/backendindex" }, params = "delete")
	public String deleteByPostId(Model model, RedirectAttributes redirectAttrs,
			@RequestParam(name = "postId", required = false) UUID[] postid) {
		// 若未選取任何一筆的話
		if (postid == null) {
			redirectAttrs.addFlashAttribute("alertMessage", "刪除失敗!");
			return "redirect:/backendindex";
		}
		// 分別刪除問卷，以及同一篇相關的問題以及回答
		try {
			surveyService.deleteSurvey(postid);
			questionService.deleteQuestions(postid);
			feedbackService.deleteFeedbacks(postid);
		} catch (Exception e) {
			logger.error(e.getMessage());
			redirectAttrs.addFlashAttribute("alertMessage", "刪除失敗!");
			return "redirect:/backendindex";
		}

		redirectAttrs.addFlashAttribute("alertMessage", "刪除成功!!");
		return "redirect:/backendindex";
	}

	@GetMapping(value = { "/backendindex" }, params = "search")
	public String backendSearchList(Model model, RedirectAttributes redirectAttrs,
			@RequestParam(name = "pageNum", required = false, defaultValue = "0") int pageNum,
			@RequestParam(name = "title", required = false, defaultValue = "") String title,
			@RequestParam(name = "startTime", required = false, defaultValue = "") String startTime,
			@RequestParam(name = "endTime", required = false, defaultValue = "") String endTime) throws ParseException {
		// 檢查問卷是否過期
		surveyService.timeCheck();

		// 檢查輸入是否有值
		if (title.isEmpty() && startTime.isEmpty() && endTime.isEmpty()) {
			redirectAttrs.addFlashAttribute("alertMessage", "請輸入查詢關鍵字或是問卷時間區間 !!");
			return "redirect:/backendindex";
		}
		// 檢查時間格式
		if (!surveyService.timeFormatCheck(startTime).isEmpty() || !surveyService.timeFormatCheck(endTime).isEmpty()) {
			redirectAttrs.addFlashAttribute("alertMessage", "請輸入正確時間格式 !!");
			return "redirect:/backendindex";
		}

		Page<Survey> surveys = null;
		// 只有輸入標題
		if (startTime.isEmpty() && endTime.isEmpty()) {
			// 符合關鍵字的問卷
			surveys = surveyService.searchSurveyByTitleMatch(pageNum, title);
			// 三個都填
		} else if (!title.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty()) {
			redirectAttrs.addFlashAttribute("alertMessage", "關鍵字與時間請分開搜尋 !!");
			return "redirect:/backendindex";
			// 只有填開始以及結束
		} else if (!startTime.isEmpty() && !endTime.isEmpty()) {
			// 轉型成Date
			Date start = surveyService.timeParse(startTime);
			// 轉型成Date
			Date end = surveyService.timeParse(endTime);

			// 將資料庫取得的結束時間+一天
			Date result = surveyService.endTimePlus1(end);
			// 取得符合的問卷
			surveys = surveyService.searchSurveyByAllTime(pageNum, start, result);
		} else {
			redirectAttrs.addFlashAttribute("alertMessage", "請輸入查詢關鍵字或是問卷時間區間 !!");
			return "redirect:/backendindex";
		}

		model.addAttribute("searchSurveys", surveys);

		return "backendindex";
	}
}
