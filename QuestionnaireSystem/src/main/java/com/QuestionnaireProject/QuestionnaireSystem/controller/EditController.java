package com.QuestionnaireProject.QuestionnaireSystem.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
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
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Answer;
import com.QuestionnaireProject.QuestionnaireSystem.entity.Feedback;
import com.QuestionnaireProject.QuestionnaireSystem.entity.FrenquenQuestion;
import com.QuestionnaireProject.QuestionnaireSystem.entity.PersonInfo;
import com.QuestionnaireProject.QuestionnaireSystem.entity.Question;
import com.QuestionnaireProject.QuestionnaireSystem.entity.QuestionnaireInfo;
import com.QuestionnaireProject.QuestionnaireSystem.entity.StatisticsInfoVol2;
import com.QuestionnaireProject.QuestionnaireSystem.entity.Survey;
import com.QuestionnaireProject.QuestionnaireSystem.repository.FeedbackDao;
import com.QuestionnaireProject.QuestionnaireSystem.repository.FrenquenQuestionDao;
import com.QuestionnaireProject.QuestionnaireSystem.repository.QuestionDao;
import com.QuestionnaireProject.QuestionnaireSystem.repository.SurveyDao;
import com.QuestionnaireProject.QuestionnaireSystem.service.FeedbackService;
import com.QuestionnaireProject.QuestionnaireSystem.service.QuestionService;
import com.QuestionnaireProject.QuestionnaireSystem.service.SurveyService;
import com.google.gson.Gson;

@Controller
public class EditController {

	@Autowired
	private SurveyService surveyService;

	@Autowired
	private SurveyDao surveyDao;

	@Autowired
	public QuestionDao questionDao;

	@Autowired
	private QuestionService questionService;

	@Autowired
	public FrenquenQuestionDao frenquenQuestionDao;

	@Autowired
	private FeedbackDao feedbackDao;

	@Autowired
	private FeedbackService feedbackService;

	@GetMapping(value = { "/edit" })
	public String edit(Model model, @RequestParam(name = "postId", required = false) UUID postId // ���oURL����postId
			, RedirectAttributes redirectAttrs, HttpSession session) {
		// ���P�_�O�_���n�J
		PersonInfo person = (PersonInfo) session.getAttribute("person");
		if (person == null) {
			redirectAttrs.addFlashAttribute("alertMessage", "�Х��n�J�޲z���b��!!");
			return "redirect:/index";
		}
		// �P�_�O�_���s��A�Y���s��Ҧ�����
		if (postId != null) {
			// �z�LpostId���������ݨ�
			Survey survey = surveyDao.findById(postId).get();
			// ��ݨ����D�إH�Φ^��
			List<Question> queslist = questionDao.findByPostId(postId);
			List<Feedback> feedbacks = feedbackDao.findByPostId(postId);
			// �N�ݨ���ƥ��e�ݥH��session��
			model.addAttribute("modifySurvey", survey);
			// session�O���FPostMapping������opostId��
			session.setAttribute("modifySurvey", survey);
			// �P�w�O�_���s�ɦV�A�Y�O�Ĥ@���i�J���ܡA�ͦ����D��session
			// �Y���O�����~��ϥέ쥻��questions session
			if (session.getAttribute("questions") == null) {
				session.setAttribute("questions", queslist); // �Ndao��쪺�ȸ˨�List�A�A�˨�session��
			}
			// �ˬd�ݨ��O�_�w�g���H�@���F�A�p�G�����ܡA�h���o�������
			if (!feedbacks.isEmpty()) {
				model.addAttribute("feedbacks", feedbacks);
			}
			// �s�@�έp���
			// ���N�^���ݨ���selection�����۩w�q��Answer Model��
			List<Answer> ansList = feedbackService.deserializeAnswer(postId);
			// ���۱N�ﶵ�P�䪺�^���ƶq�s�JMAP��
			List<StatisticsInfoVol2> staList = feedbackService.getStatisticsForMap(queslist, ansList);
			// �ǻ����e�x
			model.addAttribute("statisticsInfos", staList);
			// �ɤJ�`�����D�M��
			List<FrenquenQuestion> fqList = frenquenQuestionDao.findAll();
			// �ɤJ�e��
			model.addAttribute("frenquenQuestions", fqList);
			return "edit";
		}
		// �p�G�O�s�W�Ҧ�����
		List<Question> list = new ArrayList<>();
		// ���w�]�Ū�List��session��
		session.setAttribute("questions", list);

		return "edit";
	}

	/*
	 * �s�W�Ҧ�
	 */
	@PostMapping(value = { "/edit" })
	public String editSurvey(Model model, HttpSession session, @RequestParam("title") String title,
			@RequestParam("body") String body, @RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime,
			@RequestParam(name = "available", defaultValue = "0") Integer available) throws ParseException {
		// �ˬd�Ѽƪ���
		String errorMessage = surveyService.paramCheck(title, body, startTime, endTime);
		// �p�G�����~�T���A�Y����
		if (!errorMessage.isEmpty()) {
			model.addAttribute("errorMessage", errorMessage);
			return "edit";
		}
		// �Ыذݨ�����
		Survey survey = new Survey();
		survey.setPostId(UUID.randomUUID());
		survey.setTitle(title);
		survey.setBody(body);
		// �ɶ�String �૬Date��s�J
		survey.setStartTime(surveyService.timeParse(startTime));
		survey.setEndTime(surveyService.timeParse(endTime));
		survey.setAvailable(available);
		// �s�Jsession�A�ѤU�@�ӭ�����
		session.setAttribute("survey", survey);

		List<FrenquenQuestion> fqList = frenquenQuestionDao.findAll(); // �ɤJ�`�����D�M��
		model.addAttribute("frenquenQuestions", fqList); // �ɤJ�e��

		return "edit";
	}

	/*
	 * �ק�Ҧ�
	 */
	@PostMapping(value = { "/edit" }, params = "modify")
	public String modifySurvey(Model model, HttpSession session, RedirectAttributes redirectAttrs,
			@RequestParam("title") String title, @RequestParam("body") String body,
			@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
			@RequestParam(name = "available", defaultValue = "0") Integer available) throws ParseException {
		// ���o���ק�ݨ���session�Ψ�postId
		Survey modifySurvey = (Survey) session.getAttribute("modifySurvey");
		UUID postid = modifySurvey.getPostId();
		// �ˬd�Ѽƪ���
		String errorMessage = surveyService.paramCheck(title, body, startTime, endTime);
		// �p�G�����~�T���A�Y����
		if (!errorMessage.isEmpty()) {
			redirectAttrs.addFlashAttribute("alertMessage", errorMessage);
			return "redirect:/edit?postId=" + postid;
		}
		// �S���D���ܡA�л\session����
		modifySurvey.setTitle(title);
		modifySurvey.setBody(body);
		modifySurvey.setStartTime(surveyService.timeParse(startTime));
		modifySurvey.setEndTime(surveyService.timeParse(endTime));
		modifySurvey.setAvailable(available);
		// ���˶isession�A�ѤU�@�ӭ�����
		// ���F�e�x�P�_�ΡA�ҥH�s�^survey��session
		// �]���ק�Ҧ��s�b���session
		session.setAttribute("survey", modifySurvey);

		return "redirect:/edit?postId=" + postid;
	}

	@ResponseBody
	@GetMapping(value = { "/loadFrenquenQuestion/{id}" }) // �p�G�U�Կ����ܪ��ܧYĲ�o
	public String loadFrenquenQuestion(Model model, @PathVariable("id") int id) {
		Gson gson = new Gson();
		Optional<FrenquenQuestion> frenquenQuestionOp = frenquenQuestionDao.findById(id); // �ǥ�id�M��`�����D�����
		if (!frenquenQuestionOp.isEmpty()) {
			return gson.toJson(frenquenQuestionOp.get()); // �p�G���Ȫ��ܡA�ǦC��JSON�A�ߦܫe��
		}
		return "empty"; // �S���Ȫ��ܡA�ߦ^�h�r��
	}

	@PostMapping(value = "/edit", params = "add")
	public String addQuestion(Model model, HttpSession session, RedirectAttributes redirectAttrs,
			@RequestParam("caption") String caption, @RequestParam("selection") String selection,
			@RequestParam("type") int type, @RequestParam(name = "nullable", defaultValue = "off") String nullable) {

		Survey modifySurvey = (Survey) session.getAttribute("modifySurvey");
		if (modifySurvey != null) {
			UUID postid = modifySurvey.getPostId();
			Survey survey = (Survey) session.getAttribute("survey");
			if (survey == null) {
				redirectAttrs.addFlashAttribute("alertMessage", "�Х��T�{�ݨ���ƫ���ק���!!");
				return "redirect:/edit?postId=" + postid;
			}
			if (caption.isEmpty() || selection.isEmpty()) {
				redirectAttrs.addFlashAttribute("alertMessage", "�Цb���D�H�Φ^������J���!!!");
				return "redirect:/edit?postId=" + postid;
			}
			@SuppressWarnings("unchecked")
			List<Question> list = (List<Question>) session.getAttribute("questions");
			Question ques = new Question();
			ques.setCaption(caption);
			ques.setSelection(selection);
			ques.setType(type);
			ques.setNullable(nullable);
			ques.setPostId(postid);
			list.add(ques);

			return "redirect:/edit?postId=" + postid;
		}
		// �S���ק�Ҧ�
		Survey survey = (Survey) session.getAttribute("survey");
		if (survey == null) {
			redirectAttrs.addFlashAttribute("alertMessage", "�Х���g�ݨ����!");
			return "redirect:/edit";
		}

		if (caption.isEmpty() || selection.isEmpty()) {
			model.addAttribute("quesErrorMessage", "�Цb���D�H�Φ^������J���!!!");

			List<FrenquenQuestion> fqList = frenquenQuestionDao.findAll();
			model.addAttribute("frenquenQuestions", fqList);

			return "edit";
		}

		@SuppressWarnings("unchecked")
		List<Question> list = (List<Question>) session.getAttribute("questions");
		Question ques = new Question();
		ques.setCaption(caption);
		ques.setSelection(selection);
		ques.setType(type);
		ques.setNullable(nullable);
		ques.setPostId(survey.getPostId());
		list.add(ques);

		List<FrenquenQuestion> fqList = frenquenQuestionDao.findAll();
		model.addAttribute("frenquenQuestions", fqList);

		return "edit";
	}

	@PostMapping(value = "/edit", params = "remove")
	public String removeQuestion(Model model, RedirectAttributes redirectAttrs, HttpSession session,
			@RequestParam(name = "index", required = false) Integer[] index,
			@RequestParam(name = "id", required = false) Integer[] id) {

		List<FrenquenQuestion> fqList = frenquenQuestionDao.findAll();
		model.addAttribute("frenquenQuestions", fqList);

		@SuppressWarnings("unchecked")
		List<Question> list = (List<Question>) session.getAttribute("questions");
		Survey modifySurvey = (Survey) session.getAttribute("modifySurvey");
		if (modifySurvey != null) {
			UUID postid = modifySurvey.getPostId();
			if (index == null) {
				if (id.length > 1) {
					redirectAttrs.addFlashAttribute("alertMessage", "��ܵL�k�W�L�@��!!!");
				} else {
					for (Integer quid : id) {
						questionDao.deleteById(quid);
						list = questionDao.findByPostId(postid);
						session.setAttribute("questions", list);
					}
				}
			} else {
				if (index.length > 1) {
					redirectAttrs.addFlashAttribute("alertMessage", "��ܵL�k�W�L�@��!!!");
				} else {
					for (int i : index) {
						list.remove(i);
					}
				}
			}
			return "redirect:/edit?postId=" + postid;
		}
		if (index == null) {
			model.addAttribute("quesErrorMessage", "�R������!!!");
			return "edit";
		}
		if (index.length > 1) {
			model.addAttribute("quesErrorMessage", "��ܵL�k�W�L�@��!!!");
			return "edit";
		} else {
			for (int i : index) {
				list.remove(i);
			}
			return "edit";
		}
	}

	@PostMapping(value = "/edit", params = "create")
	public String createSurvey(Model model, HttpSession session, RedirectAttributes redirectAttrs) {
		Survey survey = (Survey) session.getAttribute("survey");
		if (survey == null) {
			redirectAttrs.addFlashAttribute("alertMessage", "�Х���g�ݨ����!");
			return "redirect:/edit";
		}

		@SuppressWarnings("unchecked")
		List<Question> questionList = (List<Question>) session.getAttribute("questions");
		boolean getRequired = questionService.checkQuestionListRequired(questionList);

		if (getRequired == false) {
			model.addAttribute("quesErrorMessage", "�аȥ��s�W�@�D����!!!");
			List<FrenquenQuestion> fqList = frenquenQuestionDao.findAll();
			model.addAttribute("frenquenQuestions", fqList);
			return "edit";
		}

		surveyDao.save(survey);
		questionDao.saveAll(questionList);

		redirectAttrs.addFlashAttribute("alertMessage", "�ݨ��s�W���\!");
		return "redirect:/backendindex";
	}

	@PostMapping(value = "/edit", params = "save")
	public String modifyQuestion(Model model, HttpSession session, RedirectAttributes redirectAttrs) {
		// ���F���୶���ұa�J��postId
		Survey modifySurveyForPostId = (Survey) session.getAttribute("modifySurvey");
		UUID modifySurveyPostId = modifySurveyForPostId.getPostId();
		Survey modifySurvey = (Survey) session.getAttribute("survey");
		if (modifySurvey==null) {
			redirectAttrs.addFlashAttribute("alertMessage", "�Х��T�{�ݨ���ƫ���ק���!!");
			return "redirect:/edit?postId=" + modifySurveyPostId;
		}
		UUID postid = modifySurvey.getPostId();

		@SuppressWarnings("unchecked")
		List<Question> questionList = (List<Question>) session.getAttribute("questions");
		boolean getRequired = questionService.checkQuestionListRequired(questionList);
		if (getRequired == false) {
			redirectAttrs.addFlashAttribute("alertMessage", "�аȥ��s�W�@�D����!");
			List<FrenquenQuestion> fqList = frenquenQuestionDao.findAll();
			model.addAttribute("frenquenQuestions", fqList);
			return "redirect:/edit?postId=" + postid;
		}

		surveyDao.save(modifySurvey); // ��Ʈw�s�J�ݨ����
		for (Question question : questionList) {
			int id = question.getQuId();
			Optional<Question> questionOp = questionDao.findById(id); // �w�����ƼW�[�A���ˬddao�O�_�s�b
			if (questionOp.isEmpty()) {
				questionDao.save(question); // �S�����ܪ����s�s�@��
			} else {
				Question target = questionOp.get();
				target.setCaption(question.getCaption());
				target.setSelection(question.getSelection());
				target.setNullable(question.getNullable());
				target.setType(question.getType());
				questionDao.save(target); // �s�b�����л\�W�h
			}
		}

		redirectAttrs.addFlashAttribute("alertMessage", "�ݨ��ק令�\!");
		return "redirect:/backendindex";
	}

	@GetMapping("/feedbacklisttoCSV")
	public void exportToCSV(HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("text/csv");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=survey_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);

		Survey modifySurvey = (Survey) session.getAttribute("modifySurvey");
		List<Feedback> list = feedbackDao.findByPostId(modifySurvey.getPostId());

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] titles = new String[] { "FeedbackID", "UserName", "UserPhone", "UserEmail", "UserAge", "CreateTime",
				"Answer", "PostID" };
		String[] propertys = new String[] { "fbId", "userName", "userPhone", "userEmail", "userAge", "createTime",
				"answer", "postId" };

		csvWriter.writeHeader(titles);

		for (Feedback feedback : list) {
			csvWriter.write(feedback, propertys);
		}
		csvWriter.close();
	}

	@ResponseBody
	@GetMapping(value = { "/loadFeedbackDetail/{id}" })
	public String loadFeedbackDetail(Model model, @PathVariable("id") int id) {
		Gson gson = new Gson();
		Optional<Feedback> feedbackOp = feedbackDao.findById(id); // �ǥ�id�M��^���ݨ�
		if (!feedbackOp.isEmpty()) {
			Feedback feedback = feedbackOp.get();
			return gson.toJson(feedback); // �p�G���Ȫ��ܡA�ǦC��JSON�A�ߦܫe��
		}
		return "empty"; // �S���Ȫ��ܡA�ߦ^�h�r��
	}

	@ResponseBody
	@GetMapping(value = { "/loadFeedbackAnswerInfo/{id}" })
	public String loadFeedbackAnswerInfo(Model model, @PathVariable("id") int id) {
		Gson gson = new Gson();
		QuestionnaireInfo questionnaireInfo = feedbackService.createQuestionnaireInfoWithFbId(id);
		return gson.toJson(questionnaireInfo);
	}

	@ResponseBody
	@GetMapping(value = { "/loadModifyQuestion/{index}" }) // ���U�s�����Ĳ�o
	public String getModifyQuestion(Model model, HttpSession session, RedirectAttributes redirectAttrs,
			@PathVariable("index") int index) {
		Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		List<Question> list = (List<Question>) session.getAttribute("questions");
		// �qsession�z�L���ޭȧ���D
		Question result = list.get(index);
		// �����D��^�ǰ��D���e�x
		return gson.toJson(result);
	}

	@PostMapping(value = "/edit", params = "modifyQuestion")
	public String modifyQuestion(Model model, HttpSession session, RedirectAttributes redirectAttrs,
			@RequestParam("caption") String caption, @RequestParam("selection") String selection,
			@RequestParam("type") int type, @RequestParam("modifyQuestion") int index,
			@RequestParam(name = "nullable", defaultValue = "off") String nullable) {

		Survey modifySurvey = (Survey) session.getAttribute("modifySurvey");
		UUID postid = modifySurvey.getPostId();
		Survey survey = (Survey) session.getAttribute("survey");
		if (survey == null) {
			redirectAttrs.addFlashAttribute("alertMessage", "�Х��T�{�ݨ���ƫ���ק���!!");
			return "redirect:/edit?postId=" + postid;
		}
		if (caption.isEmpty() || selection.isEmpty()) {
			redirectAttrs.addFlashAttribute("alertMessage", "�Цb���D�H�Φ^������J���!!!");
			return "redirect:/edit?postId=" + postid;
		}
		@SuppressWarnings("unchecked")
		List<Question> list = (List<Question>) session.getAttribute("questions");
		Question question = list.get(index);
		question.setCaption(caption);
		question.setSelection(selection);
		question.setType(type);
		question.setNullable(nullable);
		question.setPostId(postid);

		return "redirect:/edit?postId=" + postid;
	}
}
