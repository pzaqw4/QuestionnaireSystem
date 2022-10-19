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
	public String edit(Model model, @RequestParam(name = "postId", required = false) UUID postId // 取得URL中的postId
			, RedirectAttributes redirectAttrs, HttpSession session) {
		// 先判斷是否有登入
		PersonInfo person = (PersonInfo) session.getAttribute("person");
		if (person == null) {
			redirectAttrs.addFlashAttribute("alertMessage", "請先登入管理員帳號!!");
			return "redirect:/index";
		}
		// 判斷是否為編輯，若為編輯模式的話
		if (postId != null) {
			// 透過postId找到對應的問卷
			Survey survey = surveyDao.findById(postId).get();
			// 找問卷的題目以及回答
			List<Question> queslist = questionDao.findByPostId(postId);
			List<Feedback> feedbacks = feedbackDao.findByPostId(postId);
			// 將問卷資料丟到前端以及session中
			model.addAttribute("modifySurvey", survey);
			// session是為了PostMapping能夠取得postId用
			session.setAttribute("modifySurvey", survey);
			// 判定是否重新導向，若是第一次進入的話，生成問題的session
			// 若不是的話繼續使用原本的questions session
			if (session.getAttribute("questions") == null) {
				session.setAttribute("questions", queslist); // 將dao找到的值裝到List，再裝到session中
			}
			// 檢查問卷是否已經有人作答了，如果有的話，則取得相關資料
			if (!feedbacks.isEmpty()) {
				model.addAttribute("feedbacks", feedbacks);
			}
			// 製作統計資料
			// 先將回答問卷的selection欄位塞到自定義的Answer Model裡
			List<Answer> ansList = feedbackService.deserializeAnswer(postId);
			// 接著將選項與其的回答數量存入MAP中
			List<StatisticsInfoVol2> staList = feedbackService.getStatisticsForMap(queslist, ansList);
			// 傳遞給前台
			model.addAttribute("statisticsInfos", staList);
			// 導入常見問題清單
			List<FrenquenQuestion> fqList = frenquenQuestionDao.findAll();
			// 導入前端
			model.addAttribute("frenquenQuestions", fqList);
			return "edit";
		}
		// 如果是新增模式的話
		List<Question> list = new ArrayList<>();
		// 先預設空的List到session中
		session.setAttribute("questions", list);

		return "edit";
	}

	/*
	 * 新增模式
	 */
	@PostMapping(value = { "/edit" })
	public String editSurvey(Model model, HttpSession session, @RequestParam("title") String title,
			@RequestParam("body") String body, @RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime,
			@RequestParam(name = "available", defaultValue = "0") Integer available) throws ParseException {
		// 檢查參數的值
		String errorMessage = surveyService.paramCheck(title, body, startTime, endTime);
		// 如果有錯誤訊息，即提示
		if (!errorMessage.isEmpty()) {
			model.addAttribute("errorMessage", errorMessage);
			return "edit";
		}
		// 創建問卷物件
		Survey survey = new Survey();
		survey.setPostId(UUID.randomUUID());
		survey.setTitle(title);
		survey.setBody(body);
		// 時間String 轉型Date後存入
		survey.setStartTime(surveyService.timeParse(startTime));
		survey.setEndTime(surveyService.timeParse(endTime));
		survey.setAvailable(available);
		// 存入session，供下一個頁面用
		session.setAttribute("survey", survey);

		List<FrenquenQuestion> fqList = frenquenQuestionDao.findAll(); // 導入常見問題清單
		model.addAttribute("frenquenQuestions", fqList); // 導入前端

		return "edit";
	}

	/*
	 * 修改模式
	 */
	@PostMapping(value = { "/edit" }, params = "modify")
	public String modifySurvey(Model model, HttpSession session, RedirectAttributes redirectAttrs,
			@RequestParam("title") String title, @RequestParam("body") String body,
			@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
			@RequestParam(name = "available", defaultValue = "0") Integer available) throws ParseException {
		// 取得欲修改問卷的session及其postId
		Survey modifySurvey = (Survey) session.getAttribute("modifySurvey");
		UUID postid = modifySurvey.getPostId();
		// 檢查參數的值
		String errorMessage = surveyService.paramCheck(title, body, startTime, endTime);
		// 如果有錯誤訊息，即提示
		if (!errorMessage.isEmpty()) {
			redirectAttrs.addFlashAttribute("alertMessage", errorMessage);
			return "redirect:/edit?postId=" + postid;
		}
		// 沒問題的話，覆蓋session的值
		modifySurvey.setTitle(title);
		modifySurvey.setBody(body);
		modifySurvey.setStartTime(surveyService.timeParse(startTime));
		modifySurvey.setEndTime(surveyService.timeParse(endTime));
		modifySurvey.setAvailable(available);
		// 先裝進session，供下一個頁面用
		// 為了前台判斷用，所以存回survey的session
		// 因此修改模式存在兩個session
		session.setAttribute("survey", modifySurvey);

		return "redirect:/edit?postId=" + postid;
	}

	@ResponseBody
	@GetMapping(value = { "/loadFrenquenQuestion/{id}" }) // 如果下拉選單改變的話即觸發
	public String loadFrenquenQuestion(Model model, @PathVariable("id") int id) {
		Gson gson = new Gson();
		Optional<FrenquenQuestion> frenquenQuestionOp = frenquenQuestionDao.findById(id); // 藉由id尋找常見問題的資料
		if (!frenquenQuestionOp.isEmpty()) {
			return gson.toJson(frenquenQuestionOp.get()); // 如果有值的話，序列化JSON，拋至前端
		}
		return "empty"; // 沒有值的話，拋回去字串
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
				redirectAttrs.addFlashAttribute("alertMessage", "請先確認問卷資料後按修改鍵!!");
				return "redirect:/edit?postId=" + postid;
			}
			if (caption.isEmpty() || selection.isEmpty()) {
				redirectAttrs.addFlashAttribute("alertMessage", "請在問題以及回答中填入資料!!!");
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
		// 沒有修改模式
		Survey survey = (Survey) session.getAttribute("survey");
		if (survey == null) {
			redirectAttrs.addFlashAttribute("alertMessage", "請先填寫問卷資料!");
			return "redirect:/edit";
		}

		if (caption.isEmpty() || selection.isEmpty()) {
			model.addAttribute("quesErrorMessage", "請在問題以及回答中填入資料!!!");

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
					redirectAttrs.addFlashAttribute("alertMessage", "選擇無法超過一筆!!!");
				} else {
					for (Integer quid : id) {
						questionDao.deleteById(quid);
						list = questionDao.findByPostId(postid);
						session.setAttribute("questions", list);
					}
				}
			} else {
				if (index.length > 1) {
					redirectAttrs.addFlashAttribute("alertMessage", "選擇無法超過一筆!!!");
				} else {
					for (int i : index) {
						list.remove(i);
					}
				}
			}
			return "redirect:/edit?postId=" + postid;
		}
		if (index == null) {
			model.addAttribute("quesErrorMessage", "刪除失敗!!!");
			return "edit";
		}
		if (index.length > 1) {
			model.addAttribute("quesErrorMessage", "選擇無法超過一筆!!!");
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
			redirectAttrs.addFlashAttribute("alertMessage", "請先填寫問卷資料!");
			return "redirect:/edit";
		}

		@SuppressWarnings("unchecked")
		List<Question> questionList = (List<Question>) session.getAttribute("questions");
		boolean getRequired = questionService.checkQuestionListRequired(questionList);

		if (getRequired == false) {
			model.addAttribute("quesErrorMessage", "請務必新增一題必填!!!");
			List<FrenquenQuestion> fqList = frenquenQuestionDao.findAll();
			model.addAttribute("frenquenQuestions", fqList);
			return "edit";
		}

		surveyDao.save(survey);
		questionDao.saveAll(questionList);

		redirectAttrs.addFlashAttribute("alertMessage", "問卷新增成功!");
		return "redirect:/backendindex";
	}

	@PostMapping(value = "/edit", params = "save")
	public String modifyQuestion(Model model, HttpSession session, RedirectAttributes redirectAttrs) {
		// 為了跳轉頁面所帶入的postId
		Survey modifySurveyForPostId = (Survey) session.getAttribute("modifySurvey");
		UUID modifySurveyPostId = modifySurveyForPostId.getPostId();
		Survey modifySurvey = (Survey) session.getAttribute("survey");
		if (modifySurvey==null) {
			redirectAttrs.addFlashAttribute("alertMessage", "請先確認問卷資料後按修改鍵!!");
			return "redirect:/edit?postId=" + modifySurveyPostId;
		}
		UUID postid = modifySurvey.getPostId();

		@SuppressWarnings("unchecked")
		List<Question> questionList = (List<Question>) session.getAttribute("questions");
		boolean getRequired = questionService.checkQuestionListRequired(questionList);
		if (getRequired == false) {
			redirectAttrs.addFlashAttribute("alertMessage", "請務必新增一題必填!");
			List<FrenquenQuestion> fqList = frenquenQuestionDao.findAll();
			model.addAttribute("frenquenQuestions", fqList);
			return "redirect:/edit?postId=" + postid;
		}

		surveyDao.save(modifySurvey); // 資料庫存入問卷資料
		for (Question question : questionList) {
			int id = question.getQuId();
			Optional<Question> questionOp = questionDao.findById(id); // 預防重複增加，先檢查dao是否存在
			if (questionOp.isEmpty()) {
				questionDao.save(question); // 沒有的話直接新存一筆
			} else {
				Question target = questionOp.get();
				target.setCaption(question.getCaption());
				target.setSelection(question.getSelection());
				target.setNullable(question.getNullable());
				target.setType(question.getType());
				questionDao.save(target); // 存在的話覆蓋上去
			}
		}

		redirectAttrs.addFlashAttribute("alertMessage", "問卷修改成功!");
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
		Optional<Feedback> feedbackOp = feedbackDao.findById(id); // 藉由id尋找回答問卷
		if (!feedbackOp.isEmpty()) {
			Feedback feedback = feedbackOp.get();
			return gson.toJson(feedback); // 如果有值的話，序列化JSON，拋至前端
		}
		return "empty"; // 沒有值的話，拋回去字串
	}

	@ResponseBody
	@GetMapping(value = { "/loadFeedbackAnswerInfo/{id}" })
	public String loadFeedbackAnswerInfo(Model model, @PathVariable("id") int id) {
		Gson gson = new Gson();
		QuestionnaireInfo questionnaireInfo = feedbackService.createQuestionnaireInfoWithFbId(id);
		return gson.toJson(questionnaireInfo);
	}

	@ResponseBody
	@GetMapping(value = { "/loadModifyQuestion/{index}" }) // 按下編輯鍵後觸發
	public String getModifyQuestion(Model model, HttpSession session, RedirectAttributes redirectAttrs,
			@PathVariable("index") int index) {
		Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		List<Question> list = (List<Question>) session.getAttribute("questions");
		// 從session透過索引值找問題
		Question result = list.get(index);
		// 找到問題後回傳問題給前台
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
			redirectAttrs.addFlashAttribute("alertMessage", "請先確認問卷資料後按修改鍵!!");
			return "redirect:/edit?postId=" + postid;
		}
		if (caption.isEmpty() || selection.isEmpty()) {
			redirectAttrs.addFlashAttribute("alertMessage", "請在問題以及回答中填入資料!!!");
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
