package com.QuestionnaireProject.QuestionnaireSystem.controller;

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Survey;
import com.QuestionnaireProject.QuestionnaireSystem.service.SurveyService;

@Controller
public class IndexController {

	@Autowired
	private SurveyService surveyService;

	@GetMapping(value = { "/", "/index" })
	public String indexGet(Model model, HttpSession session,
			@RequestParam(name = "pageNum", required = false, defaultValue = "0") int pageNum) {
		// 檢查問卷是否過期
		surveyService.timeCheck();
		// 取得分頁要顯示的問卷
		Page<Survey> surveys = surveyService.getSurveyPageList(pageNum);

		// 刪除不必要的session
		session.removeAttribute("feedback");
		session.removeAttribute("surveyInfo");
		// 將問卷資料裝進前台
		model.addAttribute("surveys", surveys);
		
		return "index";
	}

	@GetMapping(value = { "/search" })
	public String searchList(Model model, RedirectAttributes redirectAttrs,
			@RequestParam(name = "pageNum", required = false, defaultValue = "0") int pageNum,
			@RequestParam(name = "title", required = false, defaultValue = "") String title,
			@RequestParam(name = "startTime", required = false, defaultValue = "") String startTime,
			@RequestParam(name = "endTime", required = false, defaultValue = "") String endTime) throws ParseException {
		// 檢查問卷是否過期
		surveyService.timeCheck();

		// 檢查輸入是否有值
		if (title.isEmpty() && startTime.isEmpty() && endTime.isEmpty()) {
			redirectAttrs.addFlashAttribute("alertMessage", "請輸入查詢關鍵字或是問卷時間區間 !!");
			return "redirect:/index";
		}
		// 檢查時間格式
		if (!surveyService.timeFormatCheck(startTime).isEmpty() || !surveyService.timeFormatCheck(endTime).isEmpty()) {
			redirectAttrs.addFlashAttribute("alertMessage", "請輸入正確時間格式 !!");
			return "redirect:/index";
		}

		Page<Survey> surveys = null;
		// 只有輸入標題
		if (startTime.isEmpty() && endTime.isEmpty()) {
			// 符合關鍵字的問卷
			surveys = surveyService.searchSurveyByTitleMatch(pageNum, title);
		// 三個都填
		} else if (!title.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty()) {
			redirectAttrs.addFlashAttribute("alertMessage", "關鍵字與時間請分開搜尋 !!");
			return "redirect:/index";
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
			return "redirect:/index";
		}

		model.addAttribute("searchSurveys", surveys);

		return "index";
	}

}
