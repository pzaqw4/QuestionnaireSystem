package com.QuestionnaireProject.QuestionnaireSystem.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.QuestionnaireProject.QuestionnaireSystem.entity.FrenquenQuestion;
import com.QuestionnaireProject.QuestionnaireSystem.entity.PersonInfo;
import com.QuestionnaireProject.QuestionnaireSystem.repository.FrenquenQuestionDao;
import com.QuestionnaireProject.QuestionnaireSystem.service.FrenquenQuestionService;

@Controller
public class FrenquenQuestionController {

	@Autowired
	private FrenquenQuestionDao frenquenQuestionDao;

	@Autowired
	private FrenquenQuestionService frenquenQuestionService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@GetMapping(value = { "/frenquenquestion" })
	public String frenquenQuestionGet(Model model, RedirectAttributes redirectAttrs, HttpSession session) {
		// 先判斷是否有登入
		PersonInfo person = (PersonInfo) session.getAttribute("person");
		if (person == null) {
			redirectAttrs.addFlashAttribute("alertMessage", "請先登入管理員帳號!!");
			return "redirect:/index";
		}
		// Dao找所有的常用問題
		List<FrenquenQuestion> list = frenquenQuestionDao.findAll();
		// 存入model
		model.addAttribute("frenquenQuestions", list);

		return "frenquenquestion";
	}

	@PostMapping(value = "/frenquenquestion", params = "create")
	public String frenquenQuestionCreate(Model model, @RequestParam(name = "caption") String caption,
			@RequestParam(name = "selection") String selection, @RequestParam(name = "type") int type,
			@RequestParam(name = "nullable", defaultValue = "0") Integer nullable, RedirectAttributes redirectAttrs) {
		// 檢查參數
		String errorMessage = frenquenQuestionService.paramCheck(caption, selection);
		// 如果有錯誤訊息的話
		if (!errorMessage.isEmpty()) {
			// alert提示訊息，並重新回傳頁面
			redirectAttrs.addFlashAttribute("alertMessage", errorMessage);
			return "redirect:/frenquenquestion";
		}
		// 如果沒問題的話，新增常見問題
		FrenquenQuestion frenquenQuestion = new FrenquenQuestion();
		try {
			// 將參數寫入資料庫欄位中
			frenquenQuestion.setCaption(caption);
			frenquenQuestion.setSelection(selection);
			frenquenQuestion.setType(type);
			frenquenQuestion.setNullable(nullable);
			frenquenQuestionDao.save(frenquenQuestion);
		} catch (Exception e) {
			// 錯誤的話，出現提示訊息，並重新導入
			logger.error(e.getMessage());
			redirectAttrs.addFlashAttribute("alertMessage", "異常，請重新嘗試!!!");
			return "redirect:/frenquenquestion";
		}

		redirectAttrs.addFlashAttribute("alertMessage", "常用問題新增成功!!!");
		return "redirect:/frenquenquestion";
	}

	@PostMapping(value = "/frenquenquestion", params = "delete")
	public String frenquenQuestionDelete(Model model, @RequestParam(name = "id", required = false) int[] id,
			RedirectAttributes redirectAttrs) {
		// 若找不到預刪除的常用問題id的話
		if (id == null) {
			redirectAttrs.addFlashAttribute("alertMessage", "刪除失敗!");
			return "redirect:/frenquenquestion";
		}
		// 可選取多個
		frenquenQuestionService.deleteFrenquenQuestion(id);
		redirectAttrs.addFlashAttribute("alertMessage", "刪除成功!");
		return "redirect:/frenquenquestion";
	}

	@GetMapping(value = { "/frenquenquestion/{id}" })
	public String frenquenQuestionEditGet(Model model, @PathVariable("id") int id) {
		// 當按下編輯鍵後觸發，將帶入URL的id當作參數，透過Dao找到對應的常見問題
		FrenquenQuestion frenquenQuestion = frenquenQuestionDao.findById(id).get();
		// 顯示所有常見問題至列表
		List<FrenquenQuestion> list = frenquenQuestionDao.findAll();
		// 帶入前端
		model.addAttribute("frenquenQuestions", list);
		model.addAttribute("frenquenQuestion", frenquenQuestion);
		return "frenquenquestion";
	}

	@PostMapping(value = { "/frenquenquestion/{id}" })
	public String frenquenQuestionEdit(Model model, @PathVariable("id") int id,
			@RequestParam(name = "caption") String caption, @RequestParam(name = "selection") String selection,
			@RequestParam(name = "type") int type,
			@RequestParam(name = "nullable", defaultValue = "0") Integer nullable, RedirectAttributes redirectAttrs) {
		// 如果URL有id的話，為編輯模式
		// 取得URL的id，透過Dao找到對應的常用問題
		FrenquenQuestion frenquenQuestion = frenquenQuestionDao.findById(id).get();
		try {
			// 將修正值放入原本的常用問題中
			frenquenQuestion.setCaption(caption);
			frenquenQuestion.setSelection(selection);
			frenquenQuestion.setType(type);
			frenquenQuestion.setNullable(nullable);
			frenquenQuestionDao.save(frenquenQuestion);
		} catch (Exception e) {
			// 錯誤的話，出現提示訊息，並重新導入
			logger.error(e.getMessage());
			redirectAttrs.addFlashAttribute("alertMessage", "異常，請重新嘗試!!!");
			return "redirect:/frenquenquestion";
		}
		// 提示訊息，重新導入頁面
		redirectAttrs.addFlashAttribute("alertMessage", "常用問題編輯成功!!!");
		return "redirect:/frenquenquestion";
	}
}
