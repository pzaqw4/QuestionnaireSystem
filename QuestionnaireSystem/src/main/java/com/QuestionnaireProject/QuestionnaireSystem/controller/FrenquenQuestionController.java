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
		// ���P�_�O�_���n�J
		PersonInfo person = (PersonInfo) session.getAttribute("person");
		if (person == null) {
			redirectAttrs.addFlashAttribute("alertMessage", "�Х��n�J�޲z���b��!!");
			return "redirect:/index";
		}
		// Dao��Ҧ����`�ΰ��D
		List<FrenquenQuestion> list = frenquenQuestionDao.findAll();
		// �s�Jmodel
		model.addAttribute("frenquenQuestions", list);

		return "frenquenquestion";
	}

	@PostMapping(value = "/frenquenquestion", params = "create")
	public String frenquenQuestionCreate(Model model, @RequestParam(name = "caption") String caption,
			@RequestParam(name = "selection") String selection, @RequestParam(name = "type") int type,
			@RequestParam(name = "nullable", defaultValue = "0") Integer nullable, RedirectAttributes redirectAttrs) {
		// �ˬd�Ѽ�
		String errorMessage = frenquenQuestionService.paramCheck(caption, selection);
		// �p�G�����~�T������
		if (!errorMessage.isEmpty()) {
			// alert���ܰT���A�í��s�^�ǭ���
			redirectAttrs.addFlashAttribute("alertMessage", errorMessage);
			return "redirect:/frenquenquestion";
		}
		// �p�G�S���D���ܡA�s�W�`�����D
		FrenquenQuestion frenquenQuestion = new FrenquenQuestion();
		try {
			// �N�ѼƼg�J��Ʈw��줤
			frenquenQuestion.setCaption(caption);
			frenquenQuestion.setSelection(selection);
			frenquenQuestion.setType(type);
			frenquenQuestion.setNullable(nullable);
			frenquenQuestionDao.save(frenquenQuestion);
		} catch (Exception e) {
			// ���~���ܡA�X�{���ܰT���A�í��s�ɤJ
			logger.error(e.getMessage());
			redirectAttrs.addFlashAttribute("alertMessage", "���`�A�Э��s����!!!");
			return "redirect:/frenquenquestion";
		}

		redirectAttrs.addFlashAttribute("alertMessage", "�`�ΰ��D�s�W���\!!!");
		return "redirect:/frenquenquestion";
	}

	@PostMapping(value = "/frenquenquestion", params = "delete")
	public String frenquenQuestionDelete(Model model, @RequestParam(name = "id", required = false) int[] id,
			RedirectAttributes redirectAttrs) {
		// �Y�䤣��w�R�����`�ΰ��Did����
		if (id == null) {
			redirectAttrs.addFlashAttribute("alertMessage", "�R������!");
			return "redirect:/frenquenquestion";
		}
		// �i����h��
		frenquenQuestionService.deleteFrenquenQuestion(id);
		redirectAttrs.addFlashAttribute("alertMessage", "�R�����\!");
		return "redirect:/frenquenquestion";
	}

	@GetMapping(value = { "/frenquenquestion/{id}" })
	public String frenquenQuestionEditGet(Model model, @PathVariable("id") int id) {
		// ����U�s�����Ĳ�o�A�N�a�JURL��id��@�ѼơA�z�LDao���������`�����D
		FrenquenQuestion frenquenQuestion = frenquenQuestionDao.findById(id).get();
		// ��ܩҦ��`�����D�ܦC��
		List<FrenquenQuestion> list = frenquenQuestionDao.findAll();
		// �a�J�e��
		model.addAttribute("frenquenQuestions", list);
		model.addAttribute("frenquenQuestion", frenquenQuestion);
		return "frenquenquestion";
	}

	@PostMapping(value = { "/frenquenquestion/{id}" })
	public String frenquenQuestionEdit(Model model, @PathVariable("id") int id,
			@RequestParam(name = "caption") String caption, @RequestParam(name = "selection") String selection,
			@RequestParam(name = "type") int type,
			@RequestParam(name = "nullable", defaultValue = "0") Integer nullable, RedirectAttributes redirectAttrs) {
		// �p�GURL��id���ܡA���s��Ҧ�
		// ���oURL��id�A�z�LDao���������`�ΰ��D
		FrenquenQuestion frenquenQuestion = frenquenQuestionDao.findById(id).get();
		try {
			// �N�ץ��ȩ�J�쥻���`�ΰ��D��
			frenquenQuestion.setCaption(caption);
			frenquenQuestion.setSelection(selection);
			frenquenQuestion.setType(type);
			frenquenQuestion.setNullable(nullable);
			frenquenQuestionDao.save(frenquenQuestion);
		} catch (Exception e) {
			// ���~���ܡA�X�{���ܰT���A�í��s�ɤJ
			logger.error(e.getMessage());
			redirectAttrs.addFlashAttribute("alertMessage", "���`�A�Э��s����!!!");
			return "redirect:/frenquenquestion";
		}
		// ���ܰT���A���s�ɤJ����
		redirectAttrs.addFlashAttribute("alertMessage", "�`�ΰ��D�s�覨�\!!!");
		return "redirect:/frenquenquestion";
	}
}
