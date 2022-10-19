package com.QuestionnaireProject.QuestionnaireSystem.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Answer;
import com.QuestionnaireProject.QuestionnaireSystem.entity.Feedback;
import com.QuestionnaireProject.QuestionnaireSystem.entity.Question;
import com.QuestionnaireProject.QuestionnaireSystem.entity.QuestionnaireInfo;
import com.QuestionnaireProject.QuestionnaireSystem.entity.SelectionInfo;
import com.QuestionnaireProject.QuestionnaireSystem.entity.StatisticsInfo;
import com.QuestionnaireProject.QuestionnaireSystem.entity.StatisticsInfoVol2;
import com.QuestionnaireProject.QuestionnaireSystem.repository.FeedbackDao;
import com.QuestionnaireProject.QuestionnaireSystem.repository.QuestionDao;
import com.google.gson.Gson;

@Service
public class FeedbackService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private FeedbackDao feedbackDao;

	@Autowired
	private QuestionDao questionDao;

	public Page<Feedback> getFeedbackPageList(int pageNum, int pageSize) {
		Order order = new Sort.Order(Sort.Direction.DESC, "createTime");
		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(order));
		Page<Feedback> feedbacks = feedbackDao.findAll(pageable);
		return feedbacks;
	}

	public List<Answer> deserializeAnswer(UUID postId) {
		Gson gson = new Gson();
		List<Answer> targetList = new ArrayList<Answer>();
		Answer[] ans;
		List<Feedback> feedbackList = feedbackDao.findByPostId(postId);
		for (Feedback feedback : feedbackList) {
			ans = gson.fromJson(feedback.getAnswer(), Answer[].class);
			for (Answer a : ans) {
				targetList.add(a);
			}
		}

		return targetList;
	}

	public QuestionnaireInfo createQuestionnaireInfoWithFbId(int fbId) {
		Gson gson = new Gson();
		QuestionnaireInfo result = new QuestionnaireInfo();
		List<Answer> ansList = new ArrayList<Answer>();
		UUID postId;
		Answer[] ans;
		Optional<Feedback> feedbackOp = feedbackDao.findById(fbId);
		if (!feedbackOp.isEmpty()) {
			Feedback feedback = feedbackOp.get();
			postId = feedback.getPostId();
			List<Question> quesList = questionDao.findByPostId(postId);
			result.setQuestions(quesList);

			ans = gson.fromJson(feedback.getAnswer(), Answer[].class);
			for (Answer a : ans) {
				ansList.add(a);
			}
			result.setAnswers(ansList);
		}

		return result;
	}
	public void answerTrim(List<Answer> answerList,int quId,List<String> strList) {
		for (Answer answer : answerList) {
			if (answer.getKey().equals(quId + "")) { // 判斷答案與問題是否同一題
				String[] ansArr = answer.getValue().split(","); // 分解多選答案
				for (String splitAns : ansArr) { // 檢查分解答案
					if (!splitAns.equals("")) {
						strList.add(splitAns.trim()); // 不等於""的話，去除空格裝進清單
					}
				}
			}
		}
	}

	public List<StatisticsInfo> getStatistics(List<Question> quesList, List<Answer> ansList) {
		List<StatisticsInfo> statisticsInfoList = new ArrayList<>();
		// 有幾個問題就有幾個回傳StatisticsInfo
		for (Question question : quesList) {
			// 判斷是否為文字方塊
			if (question.getType() == 2) {
				Set<SelectionInfo> set = new HashSet<>();
				StatisticsInfo statisticsInfo = new StatisticsInfo();
				// 設定標題文字
				statisticsInfo.setTitle(question.getCaption());
				SelectionInfo selectionInfo = new SelectionInfo();
				// 設定選項內容
				selectionInfo.setName("此題沒有選項");
				selectionInfo.setCount(0);
				set.add(selectionInfo);
				// 裝進set中
				statisticsInfo.setSelectionInfo(set);
				// 裝回StatisticsInfo中，完成一題
				statisticsInfoList.add(statisticsInfo);
			} else { // 不是的話
				StatisticsInfo statisticsInfo = new StatisticsInfo();
				statisticsInfo.setTitle(question.getCaption()); // 標題文字
				Set<SelectionInfo> set = new HashSet<>();
				int count = 0;
				List<String> strList = new ArrayList<>();
				answerTrim(ansList, question.getQuId(),strList);
				Set<String> uniqueWords = new HashSet<String>(strList);
				for (String string : uniqueWords) {
					count = Collections.frequency(strList, string);
					SelectionInfo selectionInfo = new SelectionInfo();
					selectionInfo.setName(string);
					selectionInfo.setCount(count);
					set.add(selectionInfo);
				}
				statisticsInfo.setSelectionInfo(set);
				statisticsInfoList.add(statisticsInfo);
			}
		}
		return statisticsInfoList;
	}

	public List<StatisticsInfoVol2> getStatisticsForMap(List<Question> quesList, List<Answer> ansList) {
		List<StatisticsInfoVol2> statisticsInfoList = new ArrayList<>();
		for (Question question : quesList) { // 有幾個問題就有幾個回傳Info
			if (question.getType() == 2) { // 判斷是否為文字方塊
				Map<String, Integer> map = new HashMap<>();
				StatisticsInfoVol2 statisticsInfo = new StatisticsInfoVol2();
				statisticsInfo.setTitle(question.getCaption()); // 標題文字
				map.put("此題沒有選項", 0);
				statisticsInfo.setSelectionMap(map);
				statisticsInfoList.add(statisticsInfo);
			} else { // 不是的話
				StatisticsInfoVol2 statisticsInfo = new StatisticsInfoVol2();
				statisticsInfo.setTitle(question.getCaption()); // 標題文字
				Map<String, Integer> map = new HashMap<>();
				int count = 0;
				List<String> strList = new ArrayList<>();
				answerTrim(ansList, question.getQuId(),strList);
				for (String string : strList) {
					count = Collections.frequency(strList, string);
					map.put(string, count);
				}
				statisticsInfo.setSelectionMap(map);
				statisticsInfoList.add(statisticsInfo);
			}
		}
		return statisticsInfoList;
	}

	public void deleteFeedbacks(UUID[] postid) {
		try {
			for (UUID uuid : postid) {
				List<Feedback> list = feedbackDao.findByPostId(uuid);
				for (Feedback feedback : list) {
					feedbackDao.delete(feedback);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
